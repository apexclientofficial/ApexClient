package com.apex.client.module.misc;

import com.apex.client.Module;
import com.apex.client.setting.ColorSetting;
import com.apex.client.setting.ModeSetting;

public class HUDColor extends Module {

    private static HUDColor instance;

    private final ModeSetting colorMode = new ModeSetting("Mode", "Theme", "Theme", "Rainbow", "Custom");
    private final ColorSetting customColor = new ColorSetting("CustomColor", 255, 255, 255);

    public HUDColor() {
        super("HUDColor", "Customizes the HUD colors", Category.MISC);
        addSetting(colorMode);
        addSetting(customColor);
        instance = this;
    }

    public static int getColor() {
        if (instance == null) return ClickGUIModule.getThemeColor();
        switch (instance.colorMode.getValue()) {
            case "Rainbow": return java.awt.Color.HSBtoRGB((System.currentTimeMillis() % 4000L) / 4000f, 1f, 1f) | 0xFF000000;
            case "Custom":  return instance.customColor.getRGB() | 0xFF000000;
            case "Theme": 
            default: return ClickGUIModule.getThemeColor();
        }
    }

    public static int getColorDark() {
        if (instance == null) return ClickGUIModule.getThemeColorDark();
        int rgb = getColor();
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return 0xFF000000 | ((int)(r * 0.3f) << 16) | ((int)(g * 0.3f) << 8) | (int)(b * 0.3f);
    }
}
