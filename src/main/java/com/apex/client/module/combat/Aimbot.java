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
        if (mc.thePlayer == null || mc.theWorld == null) return;

        EntityLivingBase target = getBestTarget();
        if (target != null) {
            faceTarget(target);
        }
    }

    private EntityLivingBase getBestTarget() {
        EntityLivingBase target = null;
        double closestDist = range.getValue();

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase)) continue;
            
            if (TargetUtil.isValidTarget(entity, targetMode.getValue(), antiTeam.isEnabled())) {
                double dist = mc.thePlayer.getDistanceToEntity(entity);
                if (dist <= closestDist) {
                    closestDist = dist;
                    target = (EntityLivingBase) entity;
                }
            }
        }
        return target;
    }

    private void faceTarget(Entity target) {
        double diffX = target.posX - mc.thePlayer.posX;
        double diffZ = target.posZ - mc.thePlayer.posZ;
        double diffY = (target.posY + target.getEyeHeight()) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }
}
