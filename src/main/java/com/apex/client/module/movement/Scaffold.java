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
        if (mc.thePlayer == null || mc.theWorld == null) return;

        BlockPos under = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(under).getBlock() == Blocks.air) {
            
            // Find a block in hotbar
            int slot = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemBlock) {
                    slot = i;
                    break;
                }
            }

            if (slot != -1) {
                int oldSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;
                
                // Silently place block
                BlockPos targetPos = under.down();
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), targetPos, EnumFacing.UP, new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D));
                mc.thePlayer.swingItem();

                mc.thePlayer.inventory.currentItem = oldSlot;
            }
        }
    }
}
