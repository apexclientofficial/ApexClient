package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Aimbot extends Module {
    
    private final NumberSetting range = new NumberSetting("Range", 4.2, 1.0, 6.0, 0.1);
    private final NumberSetting fov = new NumberSetting("FOV", 90, 10, 360, 10);
    private final com.apex.client.setting.ModeSetting targetMode = new com.apex.client.setting.ModeSetting("Target", "Players", "Players", "Mobs", "Animals", "All");
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);

    public Aimbot() {
        super("Aimbot", "Locks onto targets instantly", Category.COMBAT);
        addSetting(range);
        addSetting(fov);
        addSetting(targetMode);
        addSetting(antiTeam);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;

        EntityLivingBase target = getBestTarget();
        if (target != null) {
            faceTarget(target);
        }
    }

    private EntityLivingBase getBestTarget() {
        EntityLivingBase target = null;
        double closestDist = range.getValue();

        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase)) continue;
            
            if (TargetUtil.isValidTarget(entity, targetMode.getValue(), antiTeam.isEnabled())) {
                double dist = mc.field_71439_g.func_70032_d(entity);
                if (dist <= closestDist) {
                    closestDist = dist;
                    target = (EntityLivingBase) entity;
                }
            }
        }
        return target;
    }

    private void faceTarget(Entity target) {
        double diffX = target.field_70165_t - mc.field_71439_g.field_70165_t;
        double diffZ = target.field_70161_v - mc.field_71439_g.field_70161_v;
        double diffY = (target.field_70163_u + target.func_70047_e()) - (mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e());
        
        double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
        
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        
        mc.field_71439_g.field_70177_z = yaw;
        mc.field_71439_g.field_70125_A = pitch;
    }
}
