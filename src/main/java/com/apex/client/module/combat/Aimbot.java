package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class Aimbot extends Module {
    
    private final NumberSetting range = new NumberSetting("Range", 4.2, 1.0, 6.0, 0.1);
    private final NumberSetting fov = new NumberSetting("FOV", 90, 10, 360, 10);
    private final NumberSetting speed = new NumberSetting("Speed", 5.0, 1.0, 10.0, 0.5);
    private final NumberSetting jitter = new NumberSetting("Jitter", 1.5, 0.0, 5.0, 0.1);
    private final com.apex.client.setting.ModeSetting targetMode = new com.apex.client.setting.ModeSetting("Target", "Players", "Players", "Mobs", "Animals", "All");
    private final com.apex.client.setting.ModeSetting priority = new com.apex.client.setting.ModeSetting("Priority", "Distance", "Distance", "Health");
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);
    private final BooleanSetting clickOnly = new BooleanSetting("ClickOnly", true);
    private final BooleanSetting bodyAim = new BooleanSetting("BodyAim", false);

    private final Random rng = new Random();

    public Aimbot() {
        super("Aimbot", "Locks onto targets with humanization", Category.COMBAT);
        addSetting(range);
        addSetting(fov);
        addSetting(speed);
        addSetting(jitter);
        addSetting(targetMode);
        addSetting(priority);
        addSetting(antiTeam);
        addSetting(clickOnly);
        addSetting(bodyAim);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (clickOnly.isEnabled() && !mc.gameSettings.keyBindAttack.isKeyDown()) return;

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
                    // FOV check
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
        double targetY = bodyAim.isEnabled() 
            ? (target.posY + target.height * 0.5) 
            : (target.posY + target.getEyeHeight());
        double diffY = targetY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[] { yaw, pitch };
    }

    private void faceTarget(Entity target) {
        float[] angles = getAngles(target);
        float targetYaw = angles[0];
        float targetPitch = angles[1];

        // Add human-like jitter
        float jitterAmt = (float) jitter.getValue();
        targetYaw += (rng.nextFloat() - 0.5f) * jitterAmt;
        targetPitch += (rng.nextFloat() - 0.5f) * jitterAmt * 0.5f;

        // Smooth interpolation based on speed setting
        float smoothFactor = (float) speed.getValue();
        float yawDiff = MathHelper.wrapAngleTo180_float(targetYaw - mc.thePlayer.rotationYaw);
        float pitchDiff = MathHelper.wrapAngleTo180_float(targetPitch - mc.thePlayer.rotationPitch);

        // Ease-out interpolation (faster when far, slower when close)
        float yawStep = yawDiff * (smoothFactor / 10f);
        float pitchStep = pitchDiff * (smoothFactor / 10f);

        // Clamp minimum step to prevent micro-jittering
        if (Math.abs(yawStep) < 0.1f && Math.abs(yawDiff) > 0.1f) yawStep = Math.signum(yawDiff) * 0.1f;
        if (Math.abs(pitchStep) < 0.05f && Math.abs(pitchDiff) > 0.05f) pitchStep = Math.signum(pitchDiff) * 0.05f;

        mc.thePlayer.rotationYaw += yawStep;
        mc.thePlayer.rotationPitch += pitchStep;
        mc.thePlayer.rotationPitch = MathHelper.clamp_float(mc.thePlayer.rotationPitch, -90f, 90f);
    }
}
