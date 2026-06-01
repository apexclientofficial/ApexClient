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
        if (mc.field_71439_g != null) {
            net.minecraft.client.settings.KeyBinding.func_74510_a(
                mc.field_71474_y.field_74311_E.func_151463_i(),
                org.lwjgl.input.Keyboard.isKeyDown(mc.field_71474_y.field_74311_E.func_151463_i())
            );
        }
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;

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
        if (!mc.field_71439_g.field_70122_E) return;

        // Only bridge if the player is moving forward
        boolean movingForward = mc.field_71474_y.field_74351_w.func_151470_d();
        if (!movingForward) {
            // Release sneak if not walking
            net.minecraft.client.settings.KeyBinding.func_74510_a(
                mc.field_71474_y.field_74311_E.func_151463_i(),
                org.lwjgl.input.Keyboard.isKeyDown(mc.field_71474_y.field_74311_E.func_151463_i())
            );
            bridgeState = 0;
            return;
        }

        BlockPos under = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0D, mc.field_71439_g.field_70161_v);
        boolean needsBlock = mc.field_71441_e.func_180495_p(under).func_177230_c() == Blocks.field_150350_a;

        // Check the block directly under AND slightly ahead of the player
        double motionX = -Math.sin(Math.toRadians(mc.field_71439_g.field_70177_z));
        double motionZ = Math.cos(Math.toRadians(mc.field_71439_g.field_70177_z));
        BlockPos aheadUnder = new BlockPos(
            mc.field_71439_g.field_70165_t + motionX * 0.3,
            mc.field_71439_g.field_70163_u - 1.0D,
            mc.field_71439_g.field_70161_v + motionZ * 0.3
        );
        boolean edgeAhead = mc.field_71441_e.func_180495_p(aheadUnder).func_177230_c() == Blocks.field_150350_a;

        if (needsBlock || edgeAhead) {
            // We're at the edge — sneak and place
            net.minecraft.client.settings.KeyBinding.func_74510_a(
                mc.field_71474_y.field_74311_E.func_151463_i(), true
            );

            if (needsBlock) {
                // Find and place a block
                int slot = findBlockSlot();
                if (slot != -1) {
                    int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
                    mc.field_71439_g.field_71071_by.field_70461_c = slot;

                    // Place on the block below the gap
                    placeBlockBelow(under);
                    mc.field_71439_g.func_71038_i();

                    mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
                }
            }
        } else {
            // Solid ground — unsneak and walk normally
            net.minecraft.client.settings.KeyBinding.func_74510_a(
                mc.field_71474_y.field_74311_E.func_151463_i(),
                org.lwjgl.input.Keyboard.isKeyDown(mc.field_71474_y.field_74311_E.func_151463_i())
            );
        }
    }

    private void handleBlatant() {
        BlockPos under = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0D, mc.field_71439_g.field_70161_v);
        boolean needsBlock = mc.field_71441_e.func_180495_p(under).func_177230_c() == Blocks.field_150350_a;

        if (needsBlock) {
            int slot = findBlockSlot();
            if (slot != -1) {
                int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
                mc.field_71439_g.field_71071_by.field_70461_c = slot;

                placeBlockBelow(under);
                mc.field_71439_g.func_71038_i();

                mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
            }
        }
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != null && stack.func_77973_b() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.func_77973_b()).func_179223_d();
                // Skip non-solid or problematic blocks
                if (block == Blocks.field_150335_W || block == Blocks.field_150486_ae || block == Blocks.field_150462_ai) continue;
                if (!block.func_149730_j()) continue;
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
            BlockPos neighbor = airPos.func_177972_a(face);
            if (mc.field_71441_e.func_180495_p(neighbor).func_177230_c() != Blocks.field_150350_a) {
                Vec3 hitVec = new Vec3(
                    neighbor.func_177958_n() + 0.5D + face.func_176734_d().func_82601_c() * 0.5D,
                    neighbor.func_177956_o() + 0.5D + face.func_176734_d().func_96559_d() * 0.5D,
                    neighbor.func_177952_p() + 0.5D + face.func_176734_d().func_82599_e() * 0.5D
                );
                mc.field_71442_b.func_178890_a(
                    mc.field_71439_g, mc.field_71441_e,
                    mc.field_71439_g.field_71071_by.func_70448_g(),
                    neighbor, face.func_176734_d(), hitVec
                );
                return;
            }
        }
    }
}
