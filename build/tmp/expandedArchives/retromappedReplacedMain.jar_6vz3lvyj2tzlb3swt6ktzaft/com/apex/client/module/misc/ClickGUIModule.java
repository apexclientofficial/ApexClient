package com.apex.client.module.misc;

import com.apex.client.Module;
import com.apex.client.setting.ModeSetting;

public class ClickGUIModule extends Module {
    private static ClickGUIModule instance;
    private final ModeSetting theme = new ModeSetting("Theme", "Red", "Red", "Purple", "Gold", "White", "Green", "Rainbow", "Custom");
    private final com.apex.client.setting.ColorSetting customColor = new com.apex.client.setting.ColorSetting("CustomColor", 255, 0, 0);

    public ClickGUIModule() {
        super("ClickGUI", "Opens the Apex Client GUI", Category.MISC);
        addSetting(theme);
        addSetting(customColor);
        instance = this;
    }

    public static int getThemeColor() {
        if (instance == null) return 0xFFCC0000;
        switch (instance.theme.getValue()) {
            case "Purple": return 0xFF9B59B6;
            case "Gold":   return 0xFFF1C40F;
            case "White":  return 0xFFFFFFFF;
            case "Green":  return 0xFF2ECC71;
            case "Rainbow": return java.awt.Color.HSBtoRGB((System.currentTimeMillis() % 4000L) / 4000f, 1f, 1f) | 0xFF000000;
            case "Custom":  return instance.customColor.getRGB() | 0xFF000000;
            case "Red": default: return 0xFFCC0000;
        }
    }

    public static int getThemeColorDark() {
        if (instance == null) return 0xFF330000;
        int rgb = getThemeColor();
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        // Darken by 70%
        return 0xFF000000 | ((int)(r * 0.3f) << 16) | ((int)(g * 0.3f) << 8) | (int)(b * 0.3f);
    }

    @Override
    public void onEnable() {
        if (mc.field_71439_g != null) {
            mc.func_147108_a(new com.apex.client.gui.ApexGUI());
        }
        setEnabled(false); // Turn off immediately; GUI stays open until ESC
    }
}
