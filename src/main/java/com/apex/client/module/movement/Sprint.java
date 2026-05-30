package com.apex.client.module.movement;

import com.apex.client.Module;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Automatically sprints when walking forward", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
