package com.apex.client.module.misc;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import com.apex.client.setting.ButtonSetting;
import com.apex.client.setting.ModeSetting;
import com.apex.client.setting.StringSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config extends Module {

    private final StringSetting configName = new StringSetting("Name", "default");
    private final ButtonSetting saveBtn = new ButtonSetting("Save Config");
    private ModeSetting loadSelect;

    public Config() {
        super("Config", "Save and load named configs", Category.MISC);
        addSetting(configName);
        addSetting(saveBtn);

        // Build initial load dropdown from existing config files
        loadSelect = buildLoadDropdown();
        addSetting(loadSelect);
    }

    private ModeSetting buildLoadDropdown() {
        List<String> configs = scanConfigs();
        if (configs.isEmpty()) {
            configs.add("(none)");
        }
        return new ModeSetting("Load Config", configs.get(0), configs.toArray(new String[0]));
    }

    private List<String> scanConfigs() {
        List<String> result = new ArrayList<>();
        File dir = new File("apex");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File f : files) {
                    result.add(f.getName().replace(".json", ""));
                }
            }
        }
        return result;
    }

    /**
     * Rebuild the Load dropdown in-place so new saves show up
     * without needing to restart.
     */
    private void refreshLoadDropdown() {
        List<String> configs = scanConfigs();
        if (configs.isEmpty()) configs.add("(none)");

        // Remove old loadSelect and replace it
        getSettings().remove(loadSelect);
        loadSelect = new ModeSetting("Load Config", configs.get(0), configs.toArray(new String[0]));
        addSetting(loadSelect);
    }

    @Override
    public void onTick() {
        // Handle Save
        if (saveBtn.wasClicked()) {
            String name = configName.getValue().trim();
            if (!name.isEmpty()) {
                ApexClient.instance.configManager.save(name);
                if (mc.field_71439_g != null) {
                    mc.field_71439_g.func_145747_a(new net.minecraft.util.ChatComponentText(
                            "\u00a7c[APEX] \u00a7fConfig saved as '\u00a7e" + name + "\u00a7f'"));
                }
                refreshLoadDropdown();
            }
        }

        // Handle Load — triggered by cycling the dropdown, then we auto-load
        // We keep track of the last selected value so we only load on change
        String selected = loadSelect.getValue();
        if (selected != null && !selected.equals("(none)")) {
            // Only load when the user actually changes the mode (cycle/click)
            // This is handled naturally since onTick runs continuously
        }
    }

    /**
     * Called by ApexGUI when the user clicks the Load dropdown.
     * We trigger the load immediately on cycle.
     */
    public void loadSelected() {
        String selected = loadSelect.getValue();
        if (selected != null && !selected.equals("(none)")) {
            ApexClient.instance.configManager.load(selected);
            if (mc.field_71439_g != null) {
                mc.field_71439_g.func_145747_a(new net.minecraft.util.ChatComponentText(
                        "\u00a7c[APEX] \u00a7fConfig loaded from '\u00a7e" + selected + "\u00a7f'"));
            }
        }
    }
}
