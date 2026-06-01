package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Module {

    private final NumberSetting slowPercent = new NumberSetting("Speed %", 100, 50, 100, 5);
    private final BooleanSetting swordOnly = new BooleanSetting("Sword Only", false);

    public NoSlow() {
        super("NoSlow", "Prevents slowdown from items and blocks", Category.MOVEMENT);
        addSetting(slowPercent);
        addSetting(swordOnly);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;

        if (mc.thePlayer.isUsingItem()) {
            boolean isSword = mc.thePlayer.getCurrentEquippedItem() != null
                    && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof net.minecraft.item.ItemSword;

            if (swordOnly.isEnabled() && !isSword) return;

            // Keep sprinting
            if (mc.gameSettings.keyBindForward.isKeyDown()) {
                mc.thePlayer.setSprinting(true);
            }

            // Override movement multiplier by applying direct motion
            float speedMult = (float) slowPercent.getValue() / 100f;
            float forward = mc.thePlayer.moveForward;
            float strafe = mc.thePlayer.moveStrafing;

            if (forward != 0 || strafe != 0) {
                float yaw = mc.thePlayer.rotationYaw;
                float f = forward * forward + strafe * strafe;
                if (f >= 1.0E-4F) {
                    f = (float) Math.sqrt(f);
                    if (f < 1.0F) f = 1.0F;
                    f = speedMult / f;
                    forward *= f;
                    strafe *= f;
                    float sin = (float) Math.sin(yaw * Math.PI / 180.0D);
                    float cos = (float) Math.cos(yaw * Math.PI / 180.0D);
                    mc.thePlayer.motionX += strafe * cos - forward * sin;
                    mc.thePlayer.motionZ += forward * cos + strafe * sin;
                }
            }

            // NCP/Vanilla bypass: send digging packets to "cancel" the blocking state server-side
            // This tricks the server into not applying the slowdown
            if (isSword && mc.thePlayer.ticksExisted % 3 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(
                    new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN, EnumFacing.DOWN
                    )
                );
                // Re-block immediately
                mc.thePlayer.sendQueue.addToSendQueue(
                    new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem())
                );
            }
        }
    }
}
