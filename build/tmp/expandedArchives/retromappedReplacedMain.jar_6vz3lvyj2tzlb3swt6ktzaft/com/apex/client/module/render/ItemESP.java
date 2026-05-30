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
        if (mc.field_71441_e == null) return;
        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityItem)) continue;

            double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * event.partialTicks - mc.func_175598_ae().field_78730_l;
            double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * event.partialTicks - mc.func_175598_ae().field_78731_m;
            double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * event.partialTicks - mc.func_175598_ae().field_78728_n;

            AxisAlignedBB bb = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x, y, z);

            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179090_x();
            GL11.glLineWidth(1f);
            GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.8f); // Yellow for items

            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c); GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c);
            GL11.glVertex3d(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f); GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f);
            GL11.glVertex3d(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
            GL11.glEnd();

            GlStateManager.func_179098_w(); GlStateManager.func_179126_j(); GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }
}
