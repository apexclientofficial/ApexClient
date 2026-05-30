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
            Blocks.field_150482_ag, Blocks.field_150352_o, Blocks.field_150366_p, Blocks.field_150365_q,
            Blocks.field_150369_x, Blocks.field_150412_bA, Blocks.field_150450_ax, Blocks.field_150439_ay,
            Blocks.field_150449_bY
    ));
    private static final Set<Block> CONTAINER_BLOCKS = new HashSet<>(Arrays.asList(
            Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_150343_Z
    ));

    private float oldGamma;

    public Xray() {
        super("Xray", "See ores and chests through walls", Category.RENDER);
        addSetting(ores);
        addSetting(chests);
    }

    @Override
    public void onEnable() {
        oldGamma = mc.field_71474_y.field_74333_Y;
        mc.field_71474_y.field_74333_Y = 100f; // Max brightness so you can see underground
        mc.field_71438_f.func_72712_a();
    }

    @Override
    public void onDisable() {
        mc.field_71474_y.field_74333_Y = oldGamma;
        mc.field_71438_f.func_72712_a();
    }

    public static boolean shouldRender(Block block) {
        return ORE_BLOCKS.contains(block) || CONTAINER_BLOCKS.contains(block);
    }
}
