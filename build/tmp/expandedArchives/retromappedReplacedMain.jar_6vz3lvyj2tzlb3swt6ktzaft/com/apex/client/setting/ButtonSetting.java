package com.apex.client.setting;

/**
 * A setting that acts as a clickable button. When clicked, the
 * 'clicked' flag is set to true for one tick, and the module
 * can consume it in onTick().
 */
public class ButtonSetting extends Setting {

    private boolean clicked = false;

    public ButtonSetting(String name) {
        super(name);
    }

    public boolean wasClicked() {
        if (clicked) {
            clicked = false;
            return true;
        }
        return false;
    }

    public void click() {
        this.clicked = true;
    }
}
