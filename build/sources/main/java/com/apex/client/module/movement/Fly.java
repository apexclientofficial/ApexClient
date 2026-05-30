package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.ModeSetting;
import com.apex.client.setting.NumberSetting;

public class Fly extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Velocity", "Velocity", "Vanilla");
    private final NumberSetting speed = new NumberSetting("Speed", 2.0, 0.5, 5.0, 0.1);

    public Fly() {
        super("Fly", "Allows you to fly", Category.MOVEMENT);
        addSetting(mode);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;

        if (mode.is("Vanilla")) {
            mc.thePlayer.capabilities.isFlying = true;
            mc.thePlayer.capabilities.setFlySpeed(speed.getFloatValue() * 0.05f);
        } else if (mode.is("Velocity")) {
            mc.thePlayer.capabilities.isFlying = false;
            mc.thePlayer.motionY = 0.0D;
            
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.motionY = speed.getValue() * 0.5;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.thePlayer.motionY = -speed.getValue() * 0.5;
            }

            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;

            if (forward != 0 || strafe != 0) {
                double speedVal = speed.getValue() * 0.2;
                mc.thePlayer.motionX = -Math.sin(yaw) * forward * speedVal + Math.cos(yaw) * strafe * speedVal;
                mc.thePlayer.motionZ = Math.cos(yaw) * forward * speedVal + Math.sin(yaw) * strafe * speedVal;
            } else {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.isFlying = false;
            mc.thePlayer.capabilities.setFlySpeed(0.05f);
        }
    }
}
