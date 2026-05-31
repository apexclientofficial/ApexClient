package com.apex.client;

import com.apex.client.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    
    private final String name;
    private final String description;
    private final Category category;
    private final com.apex.client.setting.KeybindSetting keybind = new com.apex.client.setting.KeybindSetting(0);
    private boolean enabled;
    
    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        addSetting(keybind);
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public int getKey() { return keybind.getCode(); }
    public void setKey(int key) { keybind.setCode(key); }

    public enum Category {
        COMBAT("Combat", "Swords"),
        MOVEMENT("Movement", "Speed"),
        PLAYER("Player", "Armor"),
        RENDER("Render", "Eye"),
        MISC("Misc", "List");

        public final String name;
        public final String iconName;

        Category(String name, String iconName) {
            this.name = name;
            this.iconName = iconName;
        }
    }
}
