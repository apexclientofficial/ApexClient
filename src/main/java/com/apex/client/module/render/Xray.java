package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Xray extends Module {

    private final BooleanSetting ores = new BooleanSetting("Ores", true);
    private final BooleanSetting chests = new BooleanSetting("Chests", true);

    private static final Set<Block> ORE_BLOCKS = new HashSet<>(Arrays.asList(
            Blocks.diamond_ore, Blocks.gold_ore, Blocks.iron_ore, Blocks.coal_ore,
            Blocks.lapis_ore, Blocks.emerald_ore, Blocks.redstone_ore, Blocks.lit_redstone_ore,
            Blocks.quartz_ore
    ));
    private static final Set<Block> CONTAINER_BLOCKS = new HashSet<>(Arrays.asList(
            Blocks.chest, Blocks.trapped_chest, Blocks.ender_chest
    ));

    private float oldGamma;
    private final List<BlockPos> foundBlocks = new ArrayList<>();
    private Thread scanThread;
    private boolean scanning = false;

    public Xray() {
        super("Xray", "Highlights ores and chests through walls", Category.RENDER);
        addSetting(ores);
        addSetting(chests);
    }

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100f; // Max brightness
        MinecraftForge.EVENT_BUS.register(this);
        scanning = true;
        startScanner();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
        MinecraftForge.EVENT_BUS.unregister(this);
        scanning = false;
        foundBlocks.clear();
    }

    private void startScanner() {
        scanThread = new Thread(() -> {
            while (scanning) {
                if (mc.thePlayer != null && mc.theWorld != null) {
                    List<BlockPos> temp = new ArrayList<>();
                    int px = (int) mc.thePlayer.posX;
                    int py = (int) mc.thePlayer.posY;
                    int pz = (int) mc.thePlayer.posZ;
                    int rad = 30;

                    for (int x = px - rad; x <= px + rad; x++) {
                        for (int y = Math.max(0, py - rad); y <= py + rad; y++) {
                            for (int z = pz - rad; z <= pz + rad; z++) {
                                BlockPos pos = new BlockPos(x, y, z);
                                Block block = mc.theWorld.getBlockState(pos).getBlock();
                                if ((ores.isEnabled() && ORE_BLOCKS.contains(block)) || 
                                    (chests.isEnabled() && CONTAINER_BLOCKS.contains(block))) {
                                    temp.add(pos);
                                }
                            }
                        }
                    }
                    synchronized (foundBlocks) {
                        foundBlocks.clear();
                        foundBlocks.addAll(temp);
                    }
                }
                try {
                    Thread.sleep(1000); // scan every 1s
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        scanThread.setDaemon(true);
        scanThread.start();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!isEnabled() || mc.thePlayer == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        GL11.glLineWidth(1.5f);

        double viewerX = mc.getRenderManager().viewerPosX;
        double viewerY = mc.getRenderManager().viewerPosY;
        double viewerZ = mc.getRenderManager().viewerPosZ;

        synchronized (foundBlocks) {
            for (BlockPos pos : foundBlocks) {
                Block block = mc.theWorld.getBlockState(pos).getBlock();
                if (block == Blocks.diamond_ore) GL11.glColor4f(0.0f, 1.0f, 1.0f, 0.4f);
                else if (block == Blocks.gold_ore) GL11.glColor4f(1.0f, 0.8f, 0.0f, 0.4f);
                else if (block == Blocks.iron_ore) GL11.glColor4f(0.9f, 0.8f, 0.7f, 0.4f);
                else if (block == Blocks.emerald_ore) GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.4f);
                else if (block == Blocks.redstone_ore || block == Blocks.lit_redstone_ore) GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.4f);
                else if (CONTAINER_BLOCKS.contains(block)) GL11.glColor4f(0.8f, 0.5f, 0.2f, 0.4f);
                else GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.4f); // coal/lapis

                double x = pos.getX() - viewerX;
                double y = pos.getY() - viewerY;
                double z = pos.getZ() - viewerZ;
                AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

                // Draw filled box
                net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox(bb);
            }
        }

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }
}
