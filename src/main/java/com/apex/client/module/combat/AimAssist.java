package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class AimAssist extends Module {
    
    private final NumberSetting range = new NumberSetting("Range", 4.2, 1.0, 6.0, 0.1);
    private final NumberSetting fov = new NumberSetting("FOV", 90, 10, 360, 10);
    private final com.apex.client.setting.ModeSetting targetMode = new com.apex.client.setting.ModeSetting("Target", "Players", "Players", "Mobs", "Animals", "All");
    private final com.apex.client.setting.ModeSetting priority = new com.apex.client.setting.ModeSetting("Priority", "Distance", "Distance", "Health");
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);
    private final BooleanSetting clickOnly = new BooleanSetting("ClickOnly", true);
    private final NumberSetting speed = new NumberSetting("Speed", 2.0, 0.1, 10.0, 0.1);
    private final NumberSetting jitter = new NumberSetting("Jitter", 0.8, 0.0, 3.0, 0.1);
    private final BooleanSetting verticalOnly = new BooleanSetting("VertOnly", false);
    private final BooleanSetting breakBlocks = new BooleanSetting("BreakBlocks", false);

    private final Random rng = new Random();

    public AimAssist() {
        super("AimAssist", "Smoothly aims at targets", Category.COMBAT);
        addSetting(range);
        addSetting(fov);
        addSetting(targetMode);
        addSetting(priority);
        addSetting(antiTeam);
        addSetting(clickOnly);
        addSetting(speed);
        addSetting(jitter);
        addSetting(verticalOnly);
        addSetting(breakBlocks);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (clickOnly.isEnabled() && !mc.gameSettings.keyBindAttack.isKeyDown()) return;
        // Don't aim assist if player is breaking a block (unless breakBlocks is on)
        if (!breakBlocks.isEnabled() && mc.playerController.getIsHittingBlock()) return;

        EntityLivingBase target = getBestTarget();
        if (target != null) {
            faceTarget(target);
        }
    }

    private EntityLivingBase getBestTarget() {
        EntityLivingBase target = null;
        double bestValue = Double.MAX_VALUE;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase)) continue;
            
            if (TargetUtil.isValidTarget(entity, targetMode.getValue(), antiTeam.isEnabled())) {
                double dist = mc.thePlayer.getDistanceToEntity(entity);
                if (dist <= range.getValue()) {
                    // FOV check - only aim at targets within your field of view
                    float[] angles = getAngles(entity);
                    float yawDiff = MathHelper.wrapAngleTo180_float(angles[0] - mc.thePlayer.rotationYaw);
                    if (Math.abs(yawDiff) > fov.getValue() / 2f) continue;

                    double value = priority.is("Distance") ? dist : ((EntityLivingBase)entity).getHealth();
                    if (value < bestValue) {
                        bestValue = value;
                        target = (EntityLivingBase) entity;
                    }
                }
            }
        }
        return target;
    }

    private float[] getAngles(Entity target) {
        double diffX = target.posX - mc.thePlayer.posX;
        double diffZ = target.posZ - mc.thePlayer.posZ;
        double diffY = (target.posY + target.getEyeHeight()) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[] { yaw, pitch };
    }

    private void faceTarget(Entity target) {
        float[] angles = getAngles(target);
        float targetYaw = angles[0];
        float targetPitch = angles[1];

        float spd = (float) speed.getValue();
        float jitterAmt = (float) jitter.getValue();

        // Add micro-jitter for humanization
        targetYaw += (rng.nextFloat() - 0.5f) * jitterAmt;
        targetPitch += (rng.nextFloat() - 0.5f) * jitterAmt * 0.4f;

        float yawDiff = MathHelper.wrapAngleTo180_float(targetYaw - mc.thePlayer.rotationYaw);
        float pitchDiff = MathHelper.wrapAngleTo180_float(targetPitch - mc.thePlayer.rotationPitch);

        // Smooth bezier-like ease: faster corrections for big differences, slower as you get close
        float yawStep = yawDiff / (11.0f - spd);
        float pitchStep = pitchDiff / (11.0f - spd);

        // Variable speed: add slight randomness to step size for humanization  
        float speedVariation = 0.85f + rng.nextFloat() * 0.3f;
        yawStep *= speedVariation;
        pitchStep *= speedVariation;

        if (!verticalOnly.isEnabled()) {
            mc.thePlayer.rotationYaw += yawStep;
        }
        mc.thePlayer.rotationPitch += pitchStep;
        mc.thePlayer.rotationPitch = MathHelper.clamp_float(mc.thePlayer.rotationPitch, -90f, 90f);
    }
}
