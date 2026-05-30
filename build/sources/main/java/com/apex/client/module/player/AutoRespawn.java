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
        if (mc.thePlayer != null && mc.currentScreen instanceof net.minecraft.client.gui.GuiGameOver) {
            mc.thePlayer.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }
}
