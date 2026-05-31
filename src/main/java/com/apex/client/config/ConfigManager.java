package com.apex.client.config;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.KeybindSetting;
import com.apex.client.setting.ModeSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.setting.Setting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class ConfigManager {

    private final File dir;
    private final Gson gson;

    public ConfigManager() {
        this.dir = new File("apex");
        if (!dir.exists()) dir.mkdirs();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save() { save("default"); }
    public void load() { load("default"); }

    public void save(String name) {
        File configFile = new File(dir, name + ".json");
        JsonObject json = new JsonObject();
        for (Module m : ApexClient.instance.getModuleManager().getModules()) {
            JsonObject modJson = new JsonObject();
            modJson.addProperty("enabled", m.isEnabled());
            modJson.addProperty("keybind", m.getKey());

            JsonObject settingsJson = new JsonObject();
            for (Setting s : m.getSettings()) {
                if (s instanceof BooleanSetting) {
                    settingsJson.addProperty(s.getName(), ((BooleanSetting) s).isEnabled());
                } else if (s instanceof NumberSetting) {
                    settingsJson.addProperty(s.getName(), ((NumberSetting) s).getValue());
                } else if (s instanceof ModeSetting) {
                    settingsJson.addProperty(s.getName(), ((ModeSetting) s).getValue());
                } else if (s instanceof com.apex.client.setting.StringSetting) {
                    settingsJson.addProperty(s.getName(), ((com.apex.client.setting.StringSetting) s).getValue());
                }
            }
            modJson.add("settings", settingsJson);
            json.add(m.getName(), modJson);
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(json, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(String name) {
        File configFile = new File(dir, name + ".json");
        if (!configFile.exists()) return;

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                String modName = entry.getKey();
                JsonObject modJson = entry.getValue().getAsJsonObject();

                Module module = ApexClient.instance.getModuleManager().getModuleByName(modName);
                if (module != null) {
                    if (modJson.has("enabled")) {
                        boolean enabled = modJson.get("enabled").getAsBoolean();
                        if (enabled != module.isEnabled() && !module.getName().equals("ClickGUI")) {
                            module.toggle();
                        }
                    }
                    if (modJson.has("keybind")) {
                        module.setKey(modJson.get("keybind").getAsInt());
                    }

                    if (modJson.has("settings")) {
                        JsonObject settingsJson = modJson.getAsJsonObject("settings");
                        for (Setting s : module.getSettings()) {
                            if (settingsJson.has(s.getName())) {
                                if (s instanceof BooleanSetting) {
                                    ((BooleanSetting) s).setValue(settingsJson.get(s.getName()).getAsBoolean());
                                } else if (s instanceof NumberSetting) {
                                    ((NumberSetting) s).setValue(settingsJson.get(s.getName()).getAsDouble());
                                } else if (s instanceof ModeSetting) {
                                    ((ModeSetting) s).setValue(settingsJson.get(s.getName()).getAsString());
                                } else if (s instanceof com.apex.client.setting.StringSetting) {
                                    ((com.apex.client.setting.StringSetting) s).setValue(settingsJson.get(s.getName()).getAsString());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
