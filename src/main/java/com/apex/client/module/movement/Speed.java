package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class Speed extends Module {
    private final NumberSetting speedMultiplier = new NumberSetting("Speed", 1.5, 1.0, 5.0, 0.1);

    public Speed() {
        super("Speed", "Move faster than normal", Category.MOVEMENT);
        addSetting(speedMultiplier);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;

        if (mc.thePlayer.onGround && mc.thePlayer.moveForward != 0) {
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;
            double mult = speedMultiplier.getValue() * 0.1;

            mc.thePlayer.motionX += -Math.sin(yaw) * forward * mult + Math.cos(yaw) * strafe * mult;
            mc.thePlayer.motionZ += Math.cos(yaw) * forward * mult + Math.sin(yaw) * strafe * mult;

            // Cap velocity
            double horizontalSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
            double maxSpeed = speedMultiplier.getValue() * 0.2;
            if (horizontalSpeed > maxSpeed) {
                mc.thePlayer.motionX = (mc.thePlayer.motionX / horizontalSpeed) * maxSpeed;
                mc.thePlayer.motionZ = (mc.thePlayer.motionZ / horizontalSpeed) * maxSpeed;
            }
        }
    }
}
