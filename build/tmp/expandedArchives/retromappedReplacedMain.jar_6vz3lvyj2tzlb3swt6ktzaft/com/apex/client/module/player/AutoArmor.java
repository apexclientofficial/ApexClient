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
        if (mc.field_71439_g == null) return;

        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(slot);
            if (stack == null) continue;
            if (!(stack.func_77973_b() instanceof ItemArmor)) continue;

            ItemArmor armor = (ItemArmor) stack.func_77973_b();
            int armorSlot = 3 - armor.field_77881_a; // Map to equipment slots 0-3

            ItemStack current = mc.field_71439_g.field_71071_by.field_70460_b[armorSlot];
            if (current == null) {
                mc.field_71439_g.field_71071_by.field_70461_c = slot;
                mc.field_71442_b.func_78769_a(mc.field_71439_g, mc.field_71441_e, stack);
                break;
            }

            if (preferBetter.isEnabled()) {
                ItemArmor cur = (ItemArmor) current.func_77973_b();
                if (armor.field_77879_b > cur.field_77879_b) {
                    mc.field_71439_g.field_71071_by.field_70461_c = slot;
                    mc.field_71442_b.func_78769_a(mc.field_71439_g, mc.field_71441_e, stack);
                    break;
                }
            }
        }
    }
}
