package com.apex.client.setting;

import org.lwjgl.input.Keyboard;

public class KeybindSetting extends Setting {
    private int code;

    public KeybindSetting(int defaultCode) {
        super("Keybind");
        this.code = defaultCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getKeyName() {
        if (code == 0) return "NONE";
        return Keyboard.getKeyName(code);
    }
}
