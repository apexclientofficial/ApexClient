package com.apex.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ApexClient.MODID, name = ApexClient.NAME, version = ApexClient.VERSION)
public class ApexClient {
    public static final String MODID = "apex";
    public static final String NAME = "Apex Client";
    public static final String VERSION = "2.1";

    @Mod.Instance
    public static ApexClient instance;

    private ModuleManager moduleManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moduleManager = new ModuleManager();
        moduleManager.init();

        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
