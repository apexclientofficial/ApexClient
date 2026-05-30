package com.apex.client.module.movement;

import com.apex.client.Module;
import net.minecraft.client.entity.EntityPlayerSP;

public class Freecam extends Module {

    private double savedX, savedY, savedZ;
    private float savedYaw, savedPitch;

    public Freecam() {
        super("Freecam", "Detach camera from your body", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;
        savedX = mc.thePlayer.posX;
        savedY = mc.thePlayer.posY;
        savedZ = mc.thePlayer.posZ;
        savedYaw = mc.thePlayer.rotationYaw;
        savedPitch = mc.thePlayer.rotationPitch;
        mc.thePlayer.noClip = true;
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;
        mc.thePlayer.noClip = false;
        mc.thePlayer.setPositionAndUpdate(savedX, savedY, savedZ);
        mc.thePlayer.rotationYaw = savedYaw;
        mc.thePlayer.rotationPitch = savedPitch;
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;
        mc.thePlayer.noClip = true;
        mc.thePlayer.onGround = false;
        mc.thePlayer.motionY = 0;

        if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = 0.1;
        if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -0.1;

        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double fwd = mc.thePlayer.movementInput.moveForward;
        double str = mc.thePlayer.movementInput.moveStrafe;
        double spd = 0.15;

        mc.thePlayer.motionX = (-Math.sin(yaw) * fwd + Math.cos(yaw) * str) * spd;
        mc.thePlayer.motionZ = (Math.cos(yaw) * fwd + Math.sin(yaw) * str) * spd;
    }
}
