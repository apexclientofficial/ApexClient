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

    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting antiTeam = new BooleanSetting("AntiTeam", true);
    private boolean registered = false;

    public ESP() {
        super("ESP", "Draws boxes around targets through walls", Category.RENDER);
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

        // Save GL state BEFORE we change anything
        GlStateManager.func_179094_E();
        GlStateManager.func_179090_x();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5f);

        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase)) continue;

            if (TargetUtil.isValidTarget(entity, players.isEnabled(), antiTeam.isEnabled())) {

                double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * event.partialTicks - mc.func_175598_ae().field_78730_l;
                double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * event.partialTicks - mc.func_175598_ae().field_78731_m;
                double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * event.partialTicks - mc.func_175598_ae().field_78728_n;

                AxisAlignedBB bb = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x, y, z);

                GL11.glColor4f(0.85f, 0.1f, 0.1f, 1.0f);

                // Bottom ring
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
                GL11.glEnd();

                // Top ring
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c);
                GL11.glEnd();

                // Vertical pillars
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f);
                GL11.glVertex3d(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f);
                GL11.glVertex3d(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f);
                GL11.glEnd();
            }
        }

        // FULLY restore GL state so the game doesn't stay red
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
