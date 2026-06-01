package com.apex.client.module.combat;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class KillAura extends Module {

    private final NumberSetting range = new NumberSetting("Range", 4.2, 1.0, 6.0, 0.1);
    private final NumberSetting aps = new NumberSetting("APS", 10, 1, 20, 1);
    private final com.apex.client.setting.ModeSetting targetMode = new com.apex.client.setting.ModeSetting("Target", "Players", "Players", "Mobs", "Animals", "All");
    private final com.apex.client.setting.ModeSetting priority = new com.apex.client.setting.ModeSetting("Priority", "Distance", "Distance", "Health");
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);

    private long lastAttackTime;

    public KillAura() {
        super("Aura", "Automatically attacks entities around you", Category.COMBAT);
        addSetting(range);
        addSetting(aps);
        addSetting(targetMode);
        addSetting(priority);
        addSetting(antiTeam);
    }

    private EntityLivingBase currentTarget;

    public EntityLivingBase getTarget() {
        return currentTarget;
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) {
            currentTarget = null;
            return;
        }

        long delay = (long) (1000.0 / aps.getValue());
        if (System.currentTimeMillis() - lastAttackTime < delay) return;

        EntityLivingBase target = null;
        double bestValue = Double.MAX_VALUE;

        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof net.minecraft.entity.EntityLivingBase)) continue;
            
            if (TargetUtil.isValidTarget(entity, targetMode.getValue(), antiTeam.isEnabled())) {
                double dist = mc.field_71439_g.func_70032_d(entity);
                if (dist <= range.getValue()) {
                    double value = priority.is("Distance") ? dist : ((net.minecraft.entity.EntityLivingBase)entity).func_110143_aJ();
                    if (value < bestValue) {
                        bestValue = value;
                        target = (net.minecraft.entity.EntityLivingBase) entity;
                    }
                }
            }
        }

        currentTarget = target;

        if (target != null) {
            Criticals crit = (Criticals) ApexClient.instance.getModuleManager().getModuleByName("Criticals");
            if (crit != null) crit.doCrit();

            mc.field_71439_g.func_71038_i();
            mc.field_71442_b.func_78764_a(mc.field_71439_g, target);
            lastAttackTime = System.currentTimeMillis();
        }
    }
}
