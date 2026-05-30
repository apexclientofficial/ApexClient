package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class Step extends Module {
    private final NumberSetting height = new NumberSetting("Height", 1.0, 0.5, 2.5, 0.5);

    public Step() {
        super("Step", "Step up blocks automatically", Category.MOVEMENT);
        addSetting(height);
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) mc.thePlayer.stepHeight = (float) height.getValue();
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) mc.thePlayer.stepHeight = 0.5f;
    }

    @Override
    public void onTick() {
        if (mc.thePlayer != null) mc.thePlayer.stepHeight = (float) height.getValue();
    }
}
