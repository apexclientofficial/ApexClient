package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {
    private final NumberSetting cps = new NumberSetting("CPS", 12, 1, 20, 1);
    private final BooleanSetting holdOnly = new BooleanSetting("HoldOnly", true);
    private long lastClick = 0;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you", Category.COMBAT);
        addSetting(cps);
        addSetting(holdOnly);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;
        if (mc.field_71462_r != null) return; // Don't click in GUIs

        // Only click if left mouse is held (or holdOnly is off)
        if (holdOnly.isEnabled() && !Mouse.isButtonDown(0)) return;

        long delay = (long)(1000.0 / cps.getValue());
        if (System.currentTimeMillis() - lastClick < delay) return;

        // Swing + attack whatever we're looking at
        mc.field_71439_g.func_71038_i();
        Entity target = mc.field_71476_x != null ? mc.field_71476_x.field_72308_g : null;
        if (target != null) {
            mc.field_71442_b.func_78764_a(mc.field_71439_g, target);
        }
        lastClick = System.currentTimeMillis();
    }
}
