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

    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);
    private boolean registered = false;

    public Tracers() {
        super("Tracers", "Draws lines to targets", Category.RENDER);
        addSetting(players);
        addSetting(antiTeam);
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
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;

        GlStateManager.func_179094_E();
        GlStateManager.func_179090_x();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5f);

        // Calculate eye vector relative to camera
        float pt = event.partialTicks;
        double eyeX = 0;
        double eyeY = mc.field_71439_g.func_70047_e();
        double eyeZ = 0;

        GL11.glBegin(GL11.GL_LINES);
        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase)) continue;

            if (TargetUtil.isValidTarget(entity, players.isEnabled(), antiTeam.isEnabled())) {

                double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * pt - mc.func_175598_ae().field_78730_l;
                double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * pt - mc.func_175598_ae().field_78731_m;
                double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * pt - mc.func_175598_ae().field_78728_n;

                // Distance-based color: close=red, far=orange
                float dist = mc.field_71439_g.func_70032_d(entity);
                float r = 0.85f;
                float g = Math.min(0.6f, dist / 40f);
                GL11.glColor4f(r, g, 0.1f, 0.9f);

                // Line from eye position to entity center
                GL11.glVertex3d(eyeX, eyeY, eyeZ);
                GL11.glVertex3d(x, y + entity.field_70131_O / 2.0, z);
            }
        }
        GL11.glEnd();

        // FULLY restore GL state
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179145_e();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
        GlStateManager.func_179117_G();
        GlStateManager.func_179121_F();
    }
}
