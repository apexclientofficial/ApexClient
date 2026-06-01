package com.apex.client.module.render;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles the rendering hooks for HandView:
 * - Applies GlStateManager transforms before the hand is drawn
 * - Overrides swing animation duration via a tick-based approach
 */
public class HandViewRenderer {

    private static boolean registered = false;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void register() {
        if (!registered) {
            MinecraftForge.EVENT_BUS.register(new HandViewRenderer());
            registered = true;
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        HandView hv = HandView.getInstance();
        if (hv == null || !hv.isEnabled()) return;
        if (mc.thePlayer == null) return;

        // Apply custom transformations before the hand is rendered
        GlStateManager.translate(hv.getXOffset(), hv.getYOffset(), hv.getZOffset());
        GlStateManager.rotate(hv.getTilt(), 0f, 0f, 1f);
    }
}
