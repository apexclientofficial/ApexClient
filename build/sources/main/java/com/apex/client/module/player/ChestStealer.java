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
        if (mc.thePlayer == null || !(mc.thePlayer.openContainer instanceof ContainerChest)) return;

        tick++;
        if (tick < delay.getIntValue() + 1) return;
        tick = 0;

        ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
        int chestSlots = chest.getLowerChestInventory().getSizeInventory();

        for (int i = 0; i < chestSlots; i++) {
            ItemStack stack = chest.getSlot(i).getStack();
            if (stack != null) {
                mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                return; // steal one per tick based on delay
            }
        }
    }
}
