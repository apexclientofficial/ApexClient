package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Xray extends Module {

    private final BooleanSetting ores = new BooleanSetting("Ores", true);
    private final BooleanSetting chests = new BooleanSetting("Chests", true);

    private static final Set<Block> ORE_BLOCKS = new HashSet<>(Arrays.asList(
            Blocks.diamond_ore, Blocks.gold_ore, Blocks.iron_ore, Blocks.coal_ore,
            Blocks.lapis_ore, Blocks.emerald_ore, Blocks.redstone_ore, Blocks.lit_redstone_ore,
            Blocks.quartz_ore
    ));
    private static final Set<Block> CONTAINER_BLOCKS = new HashSet<>(Arrays.asList(
            Blocks.chest, Blocks.trapped_chest, Blocks.ender_chest, Blocks.obsidian
    ));

    private float oldGamma;

    public Xray() {
        super("Xray", "See ores and chests through walls", Category.RENDER);
        addSetting(ores);
        addSetting(chests);
    }

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100f; // Max brightness so you can see underground
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
        mc.renderGlobal.loadRenderers();
    }

    public static boolean shouldRender(Block block) {
        return ORE_BLOCKS.contains(block) || CONTAINER_BLOCKS.contains(block);
    }
}
