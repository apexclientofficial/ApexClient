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
        if (mc.field_71439_g == null) return;

        if (mc.field_71439_g.func_71039_bw()) {
            boolean isSword = mc.field_71439_g.func_71045_bC() != null
                    && mc.field_71439_g.func_71045_bC().func_77973_b() instanceof net.minecraft.item.ItemSword;

            if (swordOnly.isEnabled() && !isSword) return;

            // Keep sprinting
            if (mc.field_71474_y.field_74351_w.func_151470_d()) {
                mc.field_71439_g.func_70031_b(true);
            }

            // Override movement multiplier by applying direct motion
            float speedMult = (float) slowPercent.getValue() / 100f;
            float forward = mc.field_71439_g.field_70701_bs;
            float strafe = mc.field_71439_g.field_70702_br;

            if (forward != 0 || strafe != 0) {
                float yaw = mc.field_71439_g.field_70177_z;
                float f = forward * forward + strafe * strafe;
                if (f >= 1.0E-4F) {
                    f = (float) Math.sqrt(f);
                    if (f < 1.0F) f = 1.0F;
                    f = speedMult / f;
                    forward *= f;
                    strafe *= f;
                    float sin = (float) Math.sin(yaw * Math.PI / 180.0D);
                    float cos = (float) Math.cos(yaw * Math.PI / 180.0D);
                    mc.field_71439_g.field_70159_w += strafe * cos - forward * sin;
                    mc.field_71439_g.field_70179_y += forward * cos + strafe * sin;
                }
            }

            // NCP/Vanilla bypass: send digging packets to "cancel" the blocking state server-side
            // This tricks the server into not applying the slowdown
            if (isSword && mc.field_71439_g.field_70173_aa % 3 == 0) {
                mc.field_71439_g.field_71174_a.func_147297_a(
                    new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.field_177992_a, EnumFacing.DOWN
                    )
                );
                // Re-block immediately
                mc.field_71439_g.field_71174_a.func_147297_a(
                    new C08PacketPlayerBlockPlacement(mc.field_71439_g.func_71045_bC())
                );
            }
        }
    }
}
