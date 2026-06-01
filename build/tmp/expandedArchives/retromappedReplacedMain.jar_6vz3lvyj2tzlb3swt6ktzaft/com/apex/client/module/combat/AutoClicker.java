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
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;
        if (mc.field_71462_r != null) return; // Don't click in GUIs

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
        mc.field_71439_g.func_71038_i();
        Entity target = mc.field_71476_x != null ? mc.field_71476_x.field_72308_g : null;
        if (target != null) {
            mc.field_71442_b.func_78764_a(mc.field_71439_g, target);
        }
        lastClick = System.currentTimeMillis();
    }
}
