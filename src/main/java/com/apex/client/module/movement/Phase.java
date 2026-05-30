package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class Phase extends Module {
    public Phase() {
        super("Phase", "Phase through blocks using noClip", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) mc.thePlayer.noClip = true;
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) mc.thePlayer.noClip = false;
    }

    @Override
    public void onTick() {
        if (mc.thePlayer != null) {
            mc.thePlayer.noClip = true;
            mc.thePlayer.fallDistance = 0;
        }
    }
}
