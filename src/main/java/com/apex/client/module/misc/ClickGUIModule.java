package com.apex.client.module.misc;

import com.apex.client.Module;

public class ClickGUIModule extends Module {
    public ClickGUIModule() {
        super("ClickGUI", "Opens the Apex Client GUI", Category.MISC);
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            mc.displayGuiScreen(new com.apex.client.gui.ApexGUI());
        }
        setEnabled(false); // Turn off immediately; GUI stays open until ESC
    }
}
