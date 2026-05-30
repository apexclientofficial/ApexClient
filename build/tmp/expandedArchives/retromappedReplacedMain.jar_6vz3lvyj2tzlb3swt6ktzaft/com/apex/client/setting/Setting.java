package com.apex.client.setting;

import com.apex.client.Module;

public abstract class Setting {
    private final String name;
    private Module parent;
    private boolean hidden;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Module getParent() {
        return parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
