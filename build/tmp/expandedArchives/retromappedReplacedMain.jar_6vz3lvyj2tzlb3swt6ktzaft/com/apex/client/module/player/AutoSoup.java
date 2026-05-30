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
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;
        if (mc.field_71462_r != null) return;

        float health = mc.field_71439_g.func_110143_aJ();
        if (health >= threshold.getFloatValue()) return;

        // Find food/potion in hotbar
        int bestSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != null && (stack.func_77973_b() instanceof ItemFood || stack.func_77973_b() instanceof ItemPotion)) {
                bestSlot = i;
                break;
            }
        }

        if (bestSlot != -1) {
            int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
            mc.field_71439_g.field_71071_by.field_70461_c = bestSlot;
            // Right-click to use the item
            mc.field_71442_b.func_78769_a(mc.field_71439_g, mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70448_g());
            mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
        }
    }
}
