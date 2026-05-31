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
            Blocks.field_150482_ag, Blocks.field_150352_o, Blocks.field_150366_p, Blocks.field_150365_q,
            Blocks.field_150369_x, Blocks.field_150412_bA, Blocks.field_150450_ax, Blocks.field_150439_ay,
            Blocks.field_150449_bY
    ));
    private static final Set<Block> CONTAINER_BLOCKS = new HashSet<>(Arrays.asList(
            Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB
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
        oldGamma = mc.field_71474_y.field_74333_Y;
        mc.field_71474_y.field_74333_Y = 100f; // Max brightness
        MinecraftForge.EVENT_BUS.register(this);
        scanning = true;
        startScanner();
    }

    @Override
    public void onDisable() {
        mc.field_71474_y.field_74333_Y = oldGamma;
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
        if (!isEnabled() || mc.field_71439_g == null) return;

        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        GlStateManager.func_179120_a(770, 771, 1, 0);

        GL11.glLineWidth(1.5f);

        double viewerX = mc.func_175598_ae().field_78730_l;
        double viewerY = mc.func_175598_ae().field_78731_m;
        double viewerZ = mc.func_175598_ae().field_78728_n;

        synchronized (foundBlocks) {
            for (BlockPos pos : foundBlocks) {
                Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
                if (block == Blocks.field_150482_ag) GL11.glColor4f(0.0f, 1.0f, 1.0f, 0.4f);
                else if (block == Blocks.field_150352_o) GL11.glColor4f(1.0f, 0.8f, 0.0f, 0.4f);
                else if (block == Blocks.field_150366_p) GL11.glColor4f(0.9f, 0.8f, 0.7f, 0.4f);
                else if (block == Blocks.field_150412_bA) GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.4f);
                else if (block == Blocks.field_150450_ax || block == Blocks.field_150439_ay) GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.4f);
                else if (CONTAINER_BLOCKS.contains(block)) GL11.glColor4f(0.8f, 0.5f, 0.2f, 0.4f);
                else GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.4f); // coal/lapis

                double x = pos.func_177958_n() - viewerX;
                double y = pos.func_177956_o() - viewerY;
                double z = pos.func_177952_p() - viewerZ;
                AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

                // Draw filled box
                net.minecraft.client.renderer.RenderGlobal.func_181561_a(bb);
            }
        }

        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179117_G();
        GlStateManager.func_179121_F();
    }
}
