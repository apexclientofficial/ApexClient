package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;

public class TriggerBot extends Module {

    private final NumberSetting aps = new NumberSetting("APS", 10, 1, 20, 1);
    private final NumberSetting reactionTime = new NumberSetting("ReactionTime", 50, 0, 500, 10);
    private final com.apex.client.setting.ModeSetting targetMode = new com.apex.client.setting.ModeSetting("Target", "Players", "Players", "Mobs", "Animals", "All");
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);

    private long lastAttackTime;
    private Entity lastTarget = null;
    private long firstSeenTime = 0;

    public TriggerBot() {
        super("TriggerBot", "Automatically attacks when looking at an entity", Category.COMBAT);
        addSetting(aps);
        addSetting(reactionTime);
        addSetting(targetMode);
        addSetting(antiTeam);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.objectMouseOver == null) {
            lastTarget = null;
            return;
        }

        Entity target = mc.objectMouseOver.entityHit;
        if (target != null && TargetUtil.isValidTarget(target, targetMode.getValue(), antiTeam.isEnabled())) {
            if (target != lastTarget) {
                lastTarget = target;
                firstSeenTime = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - firstSeenTime < reactionTime.getValue()) return;
            
            long delay = (long) (1000.0 / aps.getValue());
            if (System.currentTimeMillis() - lastAttackTime >= delay) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, target);
                lastAttackTime = System.currentTimeMillis();
            }
        } else {
            lastTarget = null;
        }
    }
}
