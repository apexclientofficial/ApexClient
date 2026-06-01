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

    private final com.apex.client.setting.ModeSetting mode = new com.apex.client.setting.ModeSetting("Mode", "Legit", "Legit", "Blatant");

    private int tickCounter = 0;
    // Legit fastbridge state machine: 0=walking, 1=sneak, 2=place, 3=unsneak
    private int bridgeState = 0;

    public Scaffold() {
        super("Scaffold", "Places blocks under you automatically", Category.MOVEMENT);
        addSetting(mode);
    }

    @Override
    public void onEnable() {
        bridgeState = 0;
        tickCounter = 0;
    }

    @Override
    public void onDisable() {
        // Restore real sneak state
        if (mc.thePlayer != null) {
            net.minecraft.client.settings.KeyBinding.setKeyBindState(
                mc.gameSettings.keyBindSneak.getKeyCode(),
                org.lwjgl.input.Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())
            );
        }
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (mode.is("Legit")) {
            handleLegitBridge();
        } else {
            handleBlatant();
        }
    }

    /**
     * Legit FastBridge: mimics the manual technique of
     * walking to the edge → sneak → place block → unsneak → repeat
     */
    private void handleLegitBridge() {
        if (!mc.thePlayer.onGround) return;

        // Only bridge if the player is moving forward
        boolean movingForward = mc.gameSettings.keyBindForward.isKeyDown();
        if (!movingForward) {
            // Release sneak if not walking
            net.minecraft.client.settings.KeyBinding.setKeyBindState(
                mc.gameSettings.keyBindSneak.getKeyCode(),
                org.lwjgl.input.Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())
            );
            bridgeState = 0;
            return;
        }

        BlockPos under = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        boolean needsBlock = mc.theWorld.getBlockState(under).getBlock() == Blocks.air;

        // Check the block directly under AND slightly ahead of the player
        double motionX = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
        double motionZ = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
        BlockPos aheadUnder = new BlockPos(
            mc.thePlayer.posX + motionX * 0.3,
            mc.thePlayer.posY - 1.0D,
            mc.thePlayer.posZ + motionZ * 0.3
        );
        boolean edgeAhead = mc.theWorld.getBlockState(aheadUnder).getBlock() == Blocks.air;

        if (needsBlock || edgeAhead) {
            // We're at the edge — sneak and place
            net.minecraft.client.settings.KeyBinding.setKeyBindState(
                mc.gameSettings.keyBindSneak.getKeyCode(), true
            );

            if (needsBlock) {
                // Find and place a block
                int slot = findBlockSlot();
                if (slot != -1) {
                    int oldSlot = mc.thePlayer.inventory.currentItem;
                    mc.thePlayer.inventory.currentItem = slot;

                    // Place on the block below the gap
                    placeBlockBelow(under);
                    mc.thePlayer.swingItem();

                    mc.thePlayer.inventory.currentItem = oldSlot;
                }
            }
        } else {
            // Solid ground — unsneak and walk normally
            net.minecraft.client.settings.KeyBinding.setKeyBindState(
                mc.gameSettings.keyBindSneak.getKeyCode(),
                org.lwjgl.input.Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())
            );
        }
    }

    private void handleBlatant() {
        BlockPos under = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        boolean needsBlock = mc.theWorld.getBlockState(under).getBlock() == Blocks.air;

        if (needsBlock) {
            int slot = findBlockSlot();
            if (slot != -1) {
                int oldSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;

                placeBlockBelow(under);
                mc.thePlayer.swingItem();

                mc.thePlayer.inventory.currentItem = oldSlot;
            }
        }
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                // Skip non-solid or problematic blocks
                if (block == Blocks.tnt || block == Blocks.chest || block == Blocks.crafting_table) continue;
                if (!block.isFullBlock()) continue;
                return i;
            }
        }
        return -1;
    }

    /**
     * Attempts to place a block by right-clicking on adjacent solid blocks
     */
    private void placeBlockBelow(BlockPos airPos) {
        // Try each face to find a solid neighbor to click on
        EnumFacing[] facings = { EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST };
        for (EnumFacing face : facings) {
            BlockPos neighbor = airPos.offset(face);
            if (mc.theWorld.getBlockState(neighbor).getBlock() != Blocks.air) {
                Vec3 hitVec = new Vec3(
                    neighbor.getX() + 0.5D + face.getOpposite().getFrontOffsetX() * 0.5D,
                    neighbor.getY() + 0.5D + face.getOpposite().getFrontOffsetY() * 0.5D,
                    neighbor.getZ() + 0.5D + face.getOpposite().getFrontOffsetZ() * 0.5D
                );
                mc.playerController.onPlayerRightClick(
                    mc.thePlayer, mc.theWorld,
                    mc.thePlayer.inventory.getCurrentItem(),
                    neighbor, face.getOpposite(), hitVec
                );
                return;
            }
        }
    }
}
