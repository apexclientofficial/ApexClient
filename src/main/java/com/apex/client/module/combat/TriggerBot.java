package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;

public class TriggerBot extends Module {

    private final NumberSetting aps = new NumberSetting("APS", 10, 1, 20, 1);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);

    private long lastAttackTime;

    public TriggerBot() {
        super("TriggerBot", "Automatically attacks when looking at an entity", Category.COMBAT);
        addSetting(aps);
        addSetting(players);
        addSetting(antiTeam);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.objectMouseOver == null) return;

        Entity target = mc.objectMouseOver.entityHit;
        if (target != null && TargetUtil.isValidTarget(target, players.isEnabled(), antiTeam.isEnabled())) {
            
            long delay = (long) (1000.0 / aps.getValue());
            if (System.currentTimeMillis() - lastAttackTime >= delay) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, target);
                lastAttackTime = System.currentTimeMillis();
            }
        }
    }
}
