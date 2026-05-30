package com.apex.client.module.misc;

import com.apex.client.Module;

public class ClickGUIModule extends Module {
    public ClickGUIModule() {
        super("ClickGUI", "Opens the Apex Client GUI", Category.MISC);
    }

    @Override
    public void onEnable() {
        if (mc.field_71439_g != null) {
            mc.func_147108_a(new com.apex.client.gui.ApexGUI());
        }
        setEnabled(false); // Turn off immediately; GUI stays open until ESC
    }
}
