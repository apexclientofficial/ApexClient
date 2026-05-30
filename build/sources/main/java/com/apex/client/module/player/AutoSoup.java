package com.apex.client.module.player;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class AutoSoup extends Module {
    private final NumberSetting threshold = new NumberSetting("HP", 14.0, 1.0, 19.0, 1.0);

    public AutoSoup() {
        super("AutoSoup", "Auto eats food when low HP", Category.PLAYER);
        addSetting(threshold);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.currentScreen != null) return;

        float health = mc.thePlayer.getHealth();
        if (health >= threshold.getFloatValue()) return;

        // Find food/potion in hotbar
        int bestSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemPotion)) {
                bestSlot = i;
                break;
            }
        }

        if (bestSlot != -1) {
            int oldSlot = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = bestSlot;
            // Right-click to use the item
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
            mc.thePlayer.inventory.currentItem = oldSlot;
        }
    }
}
