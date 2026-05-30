package com.apex.client.module.movement;

import com.apex.client.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold", "Places blocks under you automatically", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;

        BlockPos under = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0D, mc.field_71439_g.field_70161_v);
        if (mc.field_71441_e.func_180495_p(under).func_177230_c() == Blocks.field_150350_a) {
            
            // Find a block in hotbar
            int slot = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
                if (stack != null && stack.func_77973_b() instanceof ItemBlock) {
                    slot = i;
                    break;
                }
            }

            if (slot != -1) {
                int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
                mc.field_71439_g.field_71071_by.field_70461_c = slot;
                
                // Silently place block
                BlockPos targetPos = under.func_177977_b();
                mc.field_71442_b.func_178890_a(mc.field_71439_g, mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70448_g(), targetPos, EnumFacing.UP, new Vec3(targetPos.func_177958_n() + 0.5D, targetPos.func_177956_o() + 0.5D, targetPos.func_177952_p() + 0.5D));
                mc.field_71439_g.func_71038_i();

                mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
            }
        }
    }
}
