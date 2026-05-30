package com.apex.client.module.player;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
    private final BooleanSetting preferBetter = new BooleanSetting("PreferBetter", true);

    public AutoArmor() {
        super("AutoArmor", "Automatically equip the best armor", Category.PLAYER);
        addSetting(preferBetter);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;

        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
            if (stack == null) continue;
            if (!(stack.getItem() instanceof ItemArmor)) continue;

            ItemArmor armor = (ItemArmor) stack.getItem();
            int armorSlot = 3 - armor.armorType; // Map to equipment slots 0-3

            ItemStack current = mc.thePlayer.inventory.armorInventory[armorSlot];
            if (current == null) {
                mc.thePlayer.inventory.currentItem = slot;
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);
                break;
            }

            if (preferBetter.isEnabled()) {
                ItemArmor cur = (ItemArmor) current.getItem();
                if (armor.damageReduceAmount > cur.damageReduceAmount) {
                    mc.thePlayer.inventory.currentItem = slot;
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);
                    break;
                }
            }
        }
    }
}
