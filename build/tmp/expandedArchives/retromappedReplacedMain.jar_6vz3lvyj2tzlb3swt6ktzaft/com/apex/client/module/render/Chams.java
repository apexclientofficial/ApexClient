package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.ModeSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Flat", "Flat", "Textured", "Wireframe");
    private boolean registered = false;

    public Chams() {
        super("Chams", "Renders entities through walls", Category.RENDER);
        addSetting(mode);
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
    public void onRenderPre(RenderLivingEvent.Pre event) {
        if (event.entity == mc.field_71439_g) return;
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        if (mode.is("Flat")) {
            GlStateManager.func_179090_x();
            GlStateManager.func_179131_c(1.0f, 0.1f, 0.1f, 0.6f);
        } else if (mode.is("Wireframe")) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }
    }

    @SubscribeEvent
    public void onRenderPost(RenderLivingEvent.Post event) {
        if (event.entity == mc.field_71439_g) return;
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
        GlStateManager.func_179131_c(1f, 1f, 1f, 1f);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }
}
