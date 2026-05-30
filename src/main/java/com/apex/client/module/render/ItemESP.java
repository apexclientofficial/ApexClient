package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ItemESP extends Module {
    private boolean registered = false;

    public ItemESP() {
        super("ItemESP", "Draws boxes around dropped items", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (!registered) { MinecraftForge.EVENT_BUS.register(this); registered = true; }
    }

    @Override
    public void onDisable() {
        if (registered) { MinecraftForge.EVENT_BUS.unregister(this); registered = false; }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null) return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityItem)) continue;

            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks - mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks - mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks - mc.getRenderManager().viewerPosZ;

            AxisAlignedBB bb = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GL11.glLineWidth(1f);
            GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.8f); // Yellow for items

            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ); GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ); GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glEnd();

            GlStateManager.enableTexture2D(); GlStateManager.enableDepth(); GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
