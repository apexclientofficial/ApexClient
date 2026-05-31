package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {

    private final com.apex.client.setting.ModeSetting targetMode = new com.apex.client.setting.ModeSetting("Target", "Players", "Players", "Mobs", "Animals", "All");
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);
    private final com.apex.client.setting.ModeSetting colorMode = new com.apex.client.setting.ModeSetting("Color", "Distance", "Distance", "Red", "Blue", "Green", "White");
    private boolean registered = false;

    public Tracers() {
        super("Tracers", "Draws lines to targets", Category.RENDER);
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

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5f);

        // Calculate eye vector relative to camera
        float pt = event.partialTicks;
        double eyeX = 0;
        double eyeY = mc.thePlayer.getEyeHeight();
        double eyeZ = 0;

        GL11.glBegin(GL11.GL_LINES);
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase)) continue;

            if (TargetUtil.isValidTarget(entity, targetMode.getValue(), antiTeam.isEnabled())) {

                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * pt - mc.getRenderManager().viewerPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * pt - mc.getRenderManager().viewerPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * pt - mc.getRenderManager().viewerPosZ;

                switch (colorMode.getValue()) {
                    case "Blue": GL11.glColor4f(0.1f, 0.1f, 0.85f, 0.9f); break;
                    case "Green": GL11.glColor4f(0.1f, 0.85f, 0.1f, 0.9f); break;
                    case "White": GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.9f); break;
                    case "Red": GL11.glColor4f(0.85f, 0.1f, 0.1f, 0.9f); break;
                    case "Distance": 
                    default:
                        float dist = mc.thePlayer.getDistanceToEntity(entity);
                        float r = 0.85f;
                        float g = Math.min(0.6f, dist / 40f);
                        GL11.glColor4f(r, g, 0.1f, 0.9f);
                        break;
                }

                // Line from eye position to entity center
                GL11.glVertex3d(eyeX, eyeY, eyeZ);
                GL11.glVertex3d(x, y + entity.height / 2.0, z);
            }
        }
        GL11.glEnd();

        // FULLY restore GL state
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
