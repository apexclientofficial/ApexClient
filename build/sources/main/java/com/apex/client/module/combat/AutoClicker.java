package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {
    private final com.apex.client.setting.ModeSetting mode = new com.apex.client.setting.ModeSetting("Mode", "Stable", "Stable", "Random");
    private final NumberSetting minCps = new NumberSetting("Min CPS", 8, 1, 20, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", 12, 1, 20, 1);
    private final BooleanSetting holdOnly = new BooleanSetting("HoldOnly", true);
    private long lastClick = 0;
    private long nextDelay = 0;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you", Category.COMBAT);
        addSetting(mode);
        addSetting(minCps);
        addSetting(maxCps);
        addSetting(holdOnly);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.currentScreen != null) return; // Don't click in GUIs

        // Only click if left mouse is held (or holdOnly is off)
        if (holdOnly.isEnabled() && !Mouse.isButtonDown(0)) return;

        if (System.currentTimeMillis() - lastClick < nextDelay) return;

        // Calculate next delay
        if (mode.is("Stable")) {
            nextDelay = (long)(1000.0 / maxCps.getValue());
        } else {
            double min = Math.min(minCps.getValue(), maxCps.getValue());
            double max = Math.max(minCps.getValue(), maxCps.getValue());
            double currentCps = min + Math.random() * (max - min);
            nextDelay = (long)(1000.0 / currentCps);
        }

        // Swing + attack whatever we're looking at
        mc.thePlayer.swingItem();
        Entity target = mc.objectMouseOver != null ? mc.objectMouseOver.entityHit : null;
        if (target != null) {
            mc.playerController.attackEntity(mc.thePlayer, target);
        }
        lastClick = System.currentTimeMillis();
    }
}
