package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
    private final BooleanSetting health = new BooleanSetting("ShowHealth", true);
    private final BooleanSetting distance = new BooleanSetting("ShowDistance", true);
    private boolean registered = false;

    public Nametags() {
        super("Nametags", "Renders improved nametags on players", Category.RENDER);
        addSetting(health);
        addSetting(distance);
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
        if (mc.thePlayer == null || mc.theWorld == null) return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityPlayer) || entity == mc.thePlayer) continue;
            EntityPlayer player = (EntityPlayer) entity;

            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks - mc.getRenderManager().viewerPosX;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks - mc.getRenderManager().viewerPosY + player.height + 0.5;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks - mc.getRenderManager().viewerPosZ;

            String name = player.getName();
            if (health.isEnabled()) name += " \u00a7c" + (int)player.getHealth() + "\u00a77hp";
            if (distance.isEnabled()) {
                int dist = (int)mc.thePlayer.getDistanceToEntity(player);
                name += " \u00a77[" + dist + "m]";
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1, 0, 0);
            GlStateManager.scale(-0.025f, -0.025f, 0.025f);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.depthMask(false);
            int sw = mc.fontRendererObj.getStringWidth(name);
            
            // Draw background
            net.minecraft.client.gui.Gui.drawRect(-sw / 2 - 2, -2, sw / 2 + 2, 9, 0x55000000);
            
            mc.fontRendererObj.drawStringWithShadow(name, -sw / 2f, 0, 0xFFFFFFFF);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public void onRenderLivingPre(net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (event.entity instanceof EntityPlayer && event.entity != mc.thePlayer) {
            event.setCanceled(true); // Cancel vanilla nametag rendering
        }
    }
}
