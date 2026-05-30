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
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static void renderHUD() {
        if (mc.field_71474_y.field_74330_P) return;

        // Find HUD module
        Module hudMod = ApexClient.instance.getModuleManager().getModuleByName("HUD");
        if (hudMod == null || !hudMod.isEnabled()) return;
        HUD hud = (HUD) hudMod;

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.func_78326_a();
        int sh = sr.func_78328_b();

        // ── Top Bar ──────────────────────────────────────
        if (hud.isTopBarEnabled()) {
            Gui.func_73734_a(0, 0, sw, 14, 0xAA0A0A0A);
            Gui.func_73734_a(0, 13, sw, 14, 0xFF330000);

            StringBuilder topText = new StringBuilder();
            topText.append("\u00a7cAPEX \u00a77v2.1");
            if (hud.isFPSEnabled()) {
                topText.append(" \u00a78| \u00a7fFPS: ").append(Minecraft.func_175610_ah());
            }
            mc.field_71466_p.func_175063_a(topText.toString(), 4, 3, 0xFFFFFFFF);
        }

        // ── Bottom Coordinates Panel ─────────────────────
        if (hud.isCoordsEnabled() && mc.field_71439_g != null) {
            int baseX = hud.getCoordsXOffset();
            int baseY = sh - 50 - hud.getCoordsYOffset();

            // Build lines dynamically
            List<String> lines = new ArrayList<>();
            lines.add("\u00a7cCoordinates");
            lines.add(String.format("\u00a77X: \u00a7f%.1f  \u00a77Y: \u00a7f%.1f  \u00a77Z: \u00a7f%.1f",
                    mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));

            StringBuilder extraLine = new StringBuilder();
            if (hud.isYawEnabled()) {
                extraLine.append(String.format("\u00a77Yaw: \u00a7f%.1f  ", mc.field_71439_g.field_70177_z));
            }
            if (hud.isPitchEnabled()) {
                extraLine.append(String.format("\u00a77Pitch: \u00a7f%.1f  ", mc.field_71439_g.field_70125_A));
            }
            if (hud.isWorldEnabled()) {
                extraLine.append("\u00a77World: \u00a7f").append(mc.field_71441_e.func_72827_u());
            }
            if (extraLine.length() > 0) {
                lines.add(extraLine.toString().trim());
            }

            // Calculate panel width
            int panelW = 10;
            for (String l : lines) {
                int w = mc.field_71466_p.func_78256_a(l);
                if (w + 16 > panelW) panelW = w + 16;
            }
            int panelH = 8 + lines.size() * 11;

            // Draw panel background
            Gui.func_73734_a(baseX, baseY, baseX + panelW, baseY + panelH, 0xAA0A0A0A);
            Gui.func_73734_a(baseX, baseY, baseX + 2, baseY + panelH, 0xFFCC0000);

            int ly = baseY + 4;
            for (String l : lines) {
                mc.field_71466_p.func_175063_a(l, baseX + 6, ly, 0xFFFFFFFF);
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
                int width = mc.field_71466_p.func_78256_a(name);
                int x = sw - width - 6;

                // Background
                Gui.func_73734_a(x - 4, y, sw, y + 12, 0x99080808);
                // Red accent line
                Gui.func_73734_a(sw - 2, y, sw, y + 12, 0xFFCC0000);

                mc.field_71466_p.func_175063_a(name, x - 2, y + 2, 0xFFFFFFFF);
                y += 12;
            }
        }
    }
}
