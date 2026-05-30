package com.apex.client.module.player;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class FastBreak extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", 1.5, 1.0, 5.0, 0.1);

    public FastBreak() {
        super("FastBreak", "Breaks blocks faster", Category.PLAYER);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.playerController == null) return;
        // Hook into blockHitDelay field to break faster
        try {
            java.lang.reflect.Field f = mc.playerController.getClass().getDeclaredField("field_78781_i");
            f.setAccessible(true);
            f.setInt(mc.playerController, 0);
        } catch (Exception ignored) {}
    }
}
