package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class Phase extends Module {
    public Phase() {
        super("Phase", "Phase through blocks using noClip", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (mc.field_71439_g != null) mc.field_71439_g.field_70145_X = true;
    }

    @Override
    public void onDisable() {
        if (mc.field_71439_g != null) mc.field_71439_g.field_70145_X = false;
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g != null) {
            mc.field_71439_g.field_70145_X = true;
            mc.field_71439_g.field_70143_R = 0;
        }
    }
}
