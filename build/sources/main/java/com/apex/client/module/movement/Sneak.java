package com.apex.client.module.movement;

import com.apex.client.Module;
import net.minecraft.client.settings.KeyBinding;

public class Sneak extends Module {
    public Sneak() {
        super("Sneak", "Auto-sneaks all the time", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
    }
}
