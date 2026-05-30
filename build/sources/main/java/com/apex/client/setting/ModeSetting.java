package com.apex.client.setting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {

    private String value;
    private final List<String> modes;

    public ModeSetting(String name, String defaultValue, String... modes) {
        super(name);
        this.value = defaultValue;
        this.modes = Arrays.asList(modes);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (modes.contains(value)) {
            this.value = value;
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public void cycle() {
        int index = modes.indexOf(value);
        index = (index + 1) % modes.size();
        this.value = modes.get(index);
    }

    public boolean is(String mode) {
        return this.value.equalsIgnoreCase(mode);
    }
}
