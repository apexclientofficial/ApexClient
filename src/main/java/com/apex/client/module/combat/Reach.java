package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class Reach extends Module {
    private final NumberSetting range = new NumberSetting("Range", 3.5, 3.0, 6.0, 0.1);

    public Reach() {
        super("Reach", "Extends your attack range", Category.COMBAT);
        addSetting(range);
    }

    public double getRange() { return range.getValue(); }
    // Note: actual range extension requires a packet-level mixin or NBT modification.
    // This module stores the setting and can be hooked into attack events.
}
