package com.apex.client.module.movement;

import com.apex.client.Module;
import net.minecraft.client.settings.KeyBinding;

public class Sneak extends Module {
    public Sneak() {
        super("Sneak", "Auto-sneaks all the time", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;
        KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
    }

    @Override
    public void onDisable() {
        if (mc.field_71439_g != null) KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
    }
}
