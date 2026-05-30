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
        if (mc.field_71439_g == null || mc.field_71442_b == null) return;
        // Hook into blockHitDelay field to break faster
        try {
            java.lang.reflect.Field f = mc.field_71442_b.getClass().getDeclaredField("field_78781_i");
            f.setAccessible(true);
            f.setInt(mc.field_71442_b, 0);
        } catch (Exception ignored) {}
    }
}
