package com.apex.client;

import com.apex.client.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.func_71410_x();
    
    private final String name;
    private final String description;
    private final Category category;
    private int key;
    private boolean enabled;
    
    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    protected void addSetting(Setting setting) {
        setting.setParent(this);
        this.settings.add(setting);
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
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }

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
