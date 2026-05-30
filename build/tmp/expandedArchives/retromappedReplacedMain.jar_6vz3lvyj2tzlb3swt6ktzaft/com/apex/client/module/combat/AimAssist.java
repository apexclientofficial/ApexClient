package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class AimAssist extends Module {
    
    private final NumberSetting range = new NumberSetting("Range", 4.2, 1.0, 8.0, 0.1);
    private final NumberSetting speed = new NumberSetting("Speed", 2.0, 0.1, 10.0, 0.1);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);
    private final BooleanSetting clickOnly = new BooleanSetting("ClickOnly", true);

    public AimAssist() {
        super("AimAssist", "Smoothly aims at targets", Category.COMBAT);
        addSetting(range);
        addSetting(speed);
        addSetting(players);
        addSetting(antiTeam);
        addSetting(clickOnly);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;
        if (clickOnly.isEnabled() && !mc.field_71474_y.field_74312_F.func_151470_d()) return;

        EntityLivingBase target = getBestTarget();
        if (target != null) {
            faceTarget(target, (float) speed.getValue(), (float) speed.getValue());
        }
    }

    private EntityLivingBase getBestTarget() {
        EntityLivingBase target = null;
        double closestDist = range.getValue();

        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase)) continue;
            
            if (TargetUtil.isValidTarget(entity, players.isEnabled(), antiTeam.isEnabled())) {
                double dist = mc.field_71439_g.func_70032_d(entity);
                if (dist <= closestDist) {
                    closestDist = dist;
                    target = (EntityLivingBase) entity;
                }
            }
        }
        return target;
    }

    private void faceTarget(Entity target, float yawSpeed, float pitchSpeed) {
        double diffX = target.field_70165_t - mc.field_71439_g.field_70165_t;
        double diffZ = target.field_70161_v - mc.field_71439_g.field_70161_v;
        double diffY = (target.field_70163_u + target.func_70047_e()) - (mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e());
        
        double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
        
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        
        mc.field_71439_g.field_70177_z += MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z) / (11.0f - yawSpeed);
        mc.field_71439_g.field_70125_A += MathHelper.func_76142_g(pitch - mc.field_71439_g.field_70125_A) / (11.0f - pitchSpeed);
    }
}
