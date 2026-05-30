package com.apex.client.module.player;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;

public class AutoRespawn extends Module {
    private final BooleanSetting immediate = new BooleanSetting("Immediate", true);

    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawn on death", Category.PLAYER);
        addSetting(immediate);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g != null && mc.field_71462_r instanceof net.minecraft.client.gui.GuiGameOver) {
            mc.field_71439_g.func_71004_bE();
            mc.func_147108_a(null);
        }
    }
}
