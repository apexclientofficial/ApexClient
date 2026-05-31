package com.apex.client.module.misc;

import com.apex.client.Module;
import com.apex.client.setting.ModeSetting;

public class ClickGUIModule extends Module {
    private static ClickGUIModule instance;
    private final ModeSetting theme = new ModeSetting("Theme", "Red", "Red", "Purple", "Gold", "White", "Green", "Rainbow", "Custom");
    private final com.apex.client.setting.NumberSetting customHue = new com.apex.client.setting.NumberSetting("Hue", 0.0, 0.0, 1.0, 0.01);

    public ClickGUIModule() {
        super("ClickGUI", "Opens the Apex Client GUI", Category.MISC);
        addSetting(theme);
        addSetting(customHue);
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
            case "Custom":  return java.awt.Color.HSBtoRGB((float)instance.customHue.getValue(), 1f, 1f) | 0xFF000000;
            case "Red": default: return 0xFFCC0000;
        }
    }

    public static int getThemeColorDark() {
        if (instance == null) return 0xFF330000;
        switch (instance.theme.getValue()) {
            case "Purple": return 0xFF3E1F4A;
            case "Gold":   return 0xFF5C4A00;
            case "White":  return 0xFF666666;
            case "Green":  return 0xFF0E4A2A;
            case "Rainbow": return java.awt.Color.HSBtoRGB((System.currentTimeMillis() % 4000L) / 4000f, 1f, 0.3f) | 0xFF000000;
            case "Custom":  return java.awt.Color.HSBtoRGB((float)instance.customHue.getValue(), 1f, 0.3f) | 0xFF000000;
            case "Red": default: return 0xFF330000;
        }
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            mc.displayGuiScreen(new com.apex.client.gui.ApexGUI());
        }
        setEnabled(false); // Turn off immediately; GUI stays open until ESC
    }
}
