package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class ESP extends Module {

    private final com.apex.client.setting.ModeSetting targetMode = new com.apex.client.setting.ModeSetting("Target", "Players", "Players", "Mobs", "Animals", "All");
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);
    private final com.apex.client.setting.ModeSetting colorMode = new com.apex.client.setting.ModeSetting("Color", "Red", "Red", "Blue", "Green", "White");
    private boolean registered = false;

    public ESP() {
        super("ESP", "Draws boxes around targets through walls", Category.RENDER);
        addSetting(targetMode);
        addSetting(antiTeam);
        addSetting(colorMode);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            MinecraftForge.EVENT_BUS.register(this);
            registered = true;
        }
    }

    @Override
    public void onDisable() {
        if (registered) {
            MinecraftForge.EVENT_BUS.unregister(this);
            registered = false;
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // Save GL state BEFORE we change anything
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5f);

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase)) continue;

            if (TargetUtil.isValidTarget(entity, targetMode.getValue(), antiTeam.isEnabled())) {

                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks - mc.getRenderManager().viewerPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks - mc.getRenderManager().viewerPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks - mc.getRenderManager().viewerPosZ;

                AxisAlignedBB bb = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

                switch (colorMode.getValue()) {
                    case "Blue": GL11.glColor4f(0.1f, 0.1f, 0.85f, 1.0f); break;
                    case "Green": GL11.glColor4f(0.1f, 0.85f, 0.1f, 1.0f); break;
                    case "White": GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f); break;
                    case "Red": default: GL11.glColor4f(0.85f, 0.1f, 0.1f, 1.0f); break;
                }

                // Bottom ring
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glEnd();

                // Top ring
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
                GL11.glEnd();

                // Vertical pillars
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
                GL11.glEnd();
            }
        }

        // FULLY restore GL state so the game doesn't stay red
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }
}
