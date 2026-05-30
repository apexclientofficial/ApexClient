package com.apex.client.gui.hud;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import com.apex.client.module.misc.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

public class HUDRenderer {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void renderHUD() {
        if (mc.gameSettings.showDebugInfo) return;

        // Find HUD module
        Module hudMod = ApexClient.instance.getModuleManager().getModuleByName("HUD");
        if (hudMod == null || !hudMod.isEnabled()) return;
        HUD hud = (HUD) hudMod;

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        // ── Top Bar ──────────────────────────────────────
        if (hud.isTopBarEnabled()) {
            Gui.drawRect(0, 0, sw, 14, 0xAA0A0A0A);
            Gui.drawRect(0, 13, sw, 14, 0xFF330000);

            StringBuilder topText = new StringBuilder();
            topText.append("\u00a7cAPEX \u00a77v2.1");
            if (hud.isFPSEnabled()) {
                topText.append(" \u00a78| \u00a7fFPS: ").append(Minecraft.getDebugFPS());
            }
            mc.fontRendererObj.drawStringWithShadow(topText.toString(), 4, 3, 0xFFFFFFFF);
        }

        // ── Bottom Coordinates Panel ─────────────────────
        if (hud.isCoordsEnabled() && mc.thePlayer != null) {
            int baseX = hud.getCoordsXOffset();
            int baseY = sh - 50 - hud.getCoordsYOffset();

            // Build lines dynamically
            List<String> lines = new ArrayList<>();
            lines.add("\u00a7cCoordinates");
            lines.add(String.format("\u00a77X: \u00a7f%.1f  \u00a77Y: \u00a7f%.1f  \u00a77Z: \u00a7f%.1f",
                    mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

            StringBuilder extraLine = new StringBuilder();
            if (hud.isYawEnabled()) {
                extraLine.append(String.format("\u00a77Yaw: \u00a7f%.1f  ", mc.thePlayer.rotationYaw));
            }
            if (hud.isPitchEnabled()) {
                extraLine.append(String.format("\u00a77Pitch: \u00a7f%.1f  ", mc.thePlayer.rotationPitch));
            }
            if (hud.isWorldEnabled()) {
                extraLine.append("\u00a77World: \u00a7f").append(mc.theWorld.getProviderName());
            }
            if (extraLine.length() > 0) {
                lines.add(extraLine.toString().trim());
            }

            // Calculate panel width
            int panelW = 10;
            for (String l : lines) {
                int w = mc.fontRendererObj.getStringWidth(l);
                if (w + 16 > panelW) panelW = w + 16;
            }
            int panelH = 8 + lines.size() * 11;

            // Draw panel background
            Gui.drawRect(baseX, baseY, baseX + panelW, baseY + panelH, 0xAA0A0A0A);
            Gui.drawRect(baseX, baseY, baseX + 2, baseY + panelH, 0xFFCC0000);

            int ly = baseY + 4;
            for (String l : lines) {
                mc.fontRendererObj.drawStringWithShadow(l, baseX + 6, ly, 0xFFFFFFFF);
                ly += 11;
            }
        }

        // ── Right Side ArrayList ─────────────────────────
        if (hud.isArrayListEnabled()) {
            List<Module> enabledModules = new ArrayList<>();
            for (Module m : ApexClient.instance.getModuleManager().getModules()) {
                if (m.isEnabled() && !m.getName().equals("HUD") && !m.getName().equals("ClickGUI")) {
                    enabledModules.add(m);
                }
            }

            // Sort by string width descending
            enabledModules.sort((m1, m2) -> {
                int w1 = mc.fontRendererObj.getStringWidth(m1.getName());
                int w2 = mc.fontRendererObj.getStringWidth(m2.getName());
                return Integer.compare(w2, w1);
            });

            int y = hud.isTopBarEnabled() ? 16 : 2;
            for (Module m : enabledModules) {
                String name = m.getName();
                int width = mc.fontRendererObj.getStringWidth(name);
                int x = sw - width - 6;

                // Background
                Gui.drawRect(x - 4, y, sw, y + 12, 0x99080808);
                // Red accent line
                Gui.drawRect(sw - 2, y, sw, y + 12, 0xFFCC0000);

                mc.fontRendererObj.drawStringWithShadow(name, x - 2, y + 2, 0xFFFFFFFF);
                y += 12;
            }
        }
    }
}
