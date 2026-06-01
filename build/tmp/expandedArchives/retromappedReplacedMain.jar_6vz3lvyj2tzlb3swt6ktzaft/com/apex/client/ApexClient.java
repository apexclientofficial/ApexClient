package com.apex.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ApexClient.MODID, name = ApexClient.NAME, version = ApexClient.VERSION)
public class ApexClient {
    public static final String MODID = "apex";
    public static final String NAME = "Apex Client";
    public static final String VERSION = "4.0";

    @Mod.Instance
    public static ApexClient instance;

    private ModuleManager moduleManager;
    public com.apex.client.config.ConfigManager configManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moduleManager = new ModuleManager();
        moduleManager.init();

        configManager = new com.apex.client.config.ConfigManager();
        configManager.load();

        com.apex.client.util.SpotifyManager.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            configManager.save();
        }));

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        com.apex.client.module.render.HandViewRenderer.register();
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
