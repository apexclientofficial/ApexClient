package com.apex.client.module.misc;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;

import com.apex.client.setting.StringSetting;

public class Config extends Module {
    private final StringSetting configName = new StringSetting("Name", "default");
    private final BooleanSetting save = new BooleanSetting("SaveConfig", false);
    private final BooleanSetting load = new BooleanSetting("LoadConfig", false);

    public Config() {
        super("Config", "Manages your settings", Category.MISC);
        addSetting(configName);
        addSetting(save);
        addSetting(load);
    }

    @Override
    public void onTick() {
        if (save.isEnabled()) {
            ApexClient.instance.configManager.save(configName.getValue());
            save.setValue(false);
            if (mc.field_71439_g != null) mc.field_71439_g.func_145747_a(new net.minecraft.util.ChatComponentText("\u00a7c[APEX]\u00a7f Config Saved as '" + configName.getValue() + "'!"));
        }
        if (load.isEnabled()) {
            ApexClient.instance.configManager.load(configName.getValue());
            load.setValue(false);
            if (mc.field_71439_g != null) mc.field_71439_g.func_145747_a(new net.minecraft.util.ChatComponentText("\u00a7c[APEX]\u00a7f Config Loaded from '" + configName.getValue() + "'!"));
        }
    }
}
