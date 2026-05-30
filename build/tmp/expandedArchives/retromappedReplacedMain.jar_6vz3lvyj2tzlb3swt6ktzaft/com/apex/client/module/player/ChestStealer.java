package com.apex.client.module.player;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class ChestStealer extends Module {
    private final NumberSetting delay = new NumberSetting("Delay", 2, 0, 10, 1);
    private int tick = 0;

    public ChestStealer() {
        super("ChestStealer", "Automatically steals items from chests", Category.PLAYER);
        addSetting(delay);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || !(mc.field_71439_g.field_71070_bA instanceof ContainerChest)) return;

        tick++;
        if (tick < delay.getIntValue() + 1) return;
        tick = 0;

        ContainerChest chest = (ContainerChest) mc.field_71439_g.field_71070_bA;
        int chestSlots = chest.func_85151_d().func_70302_i_();

        for (int i = 0; i < chestSlots; i++) {
            ItemStack stack = chest.func_75139_a(i).func_75211_c();
            if (stack != null) {
                mc.field_71442_b.func_78753_a(chest.field_75152_c, i, 0, 1, mc.field_71439_g);
                return; // steal one per tick based on delay
            }
        }
    }
}
