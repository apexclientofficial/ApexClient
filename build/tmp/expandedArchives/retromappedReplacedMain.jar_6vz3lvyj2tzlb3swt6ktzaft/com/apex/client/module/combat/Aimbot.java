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
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;
        if (clickOnly.isEnabled() && !mc.field_71474_y.field_74312_F.func_151470_d()) return;

        EntityLivingBase target = getBestTarget();
        if (target != null) {
            faceTarget(target);
        }
    }

    private EntityLivingBase getBestTarget() {
        EntityLivingBase target = null;
        double bestValue = Double.MAX_VALUE;

        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase)) continue;
            
            if (TargetUtil.isValidTarget(entity, targetMode.getValue(), antiTeam.isEnabled())) {
                double dist = mc.field_71439_g.func_70032_d(entity);
                if (dist <= range.getValue()) {
                    // FOV check
                    float[] angles = getAngles(entity);
                    float yawDiff = MathHelper.func_76142_g(angles[0] - mc.field_71439_g.field_70177_z);
                    if (Math.abs(yawDiff) > fov.getValue() / 2f) continue;

                    double value = priority.is("Distance") ? dist : ((EntityLivingBase)entity).func_110143_aJ();
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
        double diffX = target.field_70165_t - mc.field_71439_g.field_70165_t;
        double diffZ = target.field_70161_v - mc.field_71439_g.field_70161_v;
        double targetY = bodyAim.isEnabled() 
            ? (target.field_70163_u + target.field_70131_O * 0.5) 
            : (target.field_70163_u + target.func_70047_e());
        double diffY = targetY - (mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e());
        
        double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
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
        float yawDiff = MathHelper.func_76142_g(targetYaw - mc.field_71439_g.field_70177_z);
        float pitchDiff = MathHelper.func_76142_g(targetPitch - mc.field_71439_g.field_70125_A);

        // Ease-out interpolation (faster when far, slower when close)
        float yawStep = yawDiff * (smoothFactor / 10f);
        float pitchStep = pitchDiff * (smoothFactor / 10f);

        // Clamp minimum step to prevent micro-jittering
        if (Math.abs(yawStep) < 0.1f && Math.abs(yawDiff) > 0.1f) yawStep = Math.signum(yawDiff) * 0.1f;
        if (Math.abs(pitchStep) < 0.05f && Math.abs(pitchDiff) > 0.05f) pitchStep = Math.signum(pitchDiff) * 0.05f;

        mc.field_71439_g.field_70177_z += yawStep;
        mc.field_71439_g.field_70125_A += pitchStep;
        mc.field_71439_g.field_70125_A = MathHelper.func_76131_a(mc.field_71439_g.field_70125_A, -90f, 90f);
    }
}
