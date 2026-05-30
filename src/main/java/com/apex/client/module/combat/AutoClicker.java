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
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.currentScreen != null) return; // Don't click in GUIs

        // Only click if left mouse is held (or holdOnly is off)
        if (holdOnly.isEnabled() && !Mouse.isButtonDown(0)) return;

        long delay = (long)(1000.0 / cps.getValue());
        if (System.currentTimeMillis() - lastClick < delay) return;

        // Swing + attack whatever we're looking at
        mc.thePlayer.swingItem();
        Entity target = mc.objectMouseOver != null ? mc.objectMouseOver.entityHit : null;
        if (target != null) {
            mc.playerController.attackEntity(mc.thePlayer, target);
        }
        lastClick = System.currentTimeMillis();
    }
}
