package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Jesus extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", 1.0, 0.1, 3.0, 0.1);

    public Jesus() {
        super("Jesus", "Walk on water", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ);
        boolean onWater = mc.theWorld.getBlockState(pos).getBlock() == Blocks.water
                || mc.theWorld.getBlockState(pos).getBlock() == Blocks.flowing_water;

        if (onWater && mc.thePlayer.motionY < 0) {
            mc.thePlayer.motionY = 0.04;
        }
    }
}
