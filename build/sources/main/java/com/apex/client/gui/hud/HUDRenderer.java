package com.apex.client.gui.hud;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import com.apex.client.module.misc.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class HUDRenderer {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final List<Long> clicks = new ArrayList<>();

    public static void renderHUD() {
        if (mc.gameSettings.showDebugInfo) return;

        // CPS tracking
        if (Mouse.isButtonDown(0)) {
            if (clicks.isEmpty() || System.currentTimeMillis() - clicks.get(clicks.size() - 1) > 50) {
                clicks.add(System.currentTimeMillis());
            }
        }
        clicks.removeIf(time -> System.currentTimeMillis() - time > 1000);

        Module hudMod = ApexClient.instance.getModuleManager().getModuleByName("HUD");
        if (hudMod == null || !hudMod.isEnabled()) return;
        HUD hud = (HUD) hudMod;

        int themeCol = com.apex.client.module.misc.HUDColor.getColor();
        int themeColDark = com.apex.client.module.misc.HUDColor.getColorDark();

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        // ── Top Bar ──────────────────────────────────────
        if (hud.isTopBarEnabled()) {
            Gui.drawRect(0, 0, sw, 14, 0xAA0A0A0A);
            Gui.drawRect(0, 13, sw, 14, themeColDark);

            StringBuilder topText = new StringBuilder();
            topText.append("\u00a7cAPEX \u00a77v4.0");
            if (hud.isFPSEnabled()) {
                topText.append(" \u00a78| \u00a7fFPS: ").append(Minecraft.getDebugFPS());
            }
            if (hud.isCPSEnabled()) {
                topText.append(" \u00a78| \u00a7fCPS: ").append(clicks.size());
            }
            mc.fontRendererObj.drawStringWithShadow(topText.toString(), 4, 3, 0xFFFFFFFF);
        }

        // ── Keystrokes ───────────────────────────────────
        if (hud.isKeystrokesEnabled() && mc.thePlayer != null) {
            int baseX = hud.getKeysXOffset();
            int baseY = hud.getKeysYOffset();

            // W A S D
            drawKey(baseX + 22, baseY, 20, 20, Keyboard.KEY_W, "W", themeCol);
            drawKey(baseX, baseY + 22, 20, 20, Keyboard.KEY_A, "A", themeCol);
            drawKey(baseX + 22, baseY + 22, 20, 20, Keyboard.KEY_S, "S", themeCol);
            drawKey(baseX + 44, baseY + 22, 20, 20, Keyboard.KEY_D, "D", themeCol);

            // LMB RMB
            drawMouse(baseX, baseY + 44, 31, 20, 0, "LMB", themeCol);
            drawMouse(baseX + 33, baseY + 44, 31, 20, 1, "RMB", themeCol);
        }

        // ── Spotify ──────────────────────────────────────
        if (hud.isSpotifyEnabled() && mc.thePlayer != null) {
            String track = com.apex.client.util.SpotifyManager.getCurrentTrack();
            if (track != null && !track.isEmpty()) {
                int sx = hud.getSpotXOffset();
                int sy = hud.getSpotYOffset();
                String display = "\u00a7a\u266B \u00a7f" + track;
                int tw = mc.fontRendererObj.getStringWidth(display);
                
                Gui.drawRect(sx, sy, sx + tw + 8, sy + 14, 0xAA0A0A0A);
                Gui.drawRect(sx, sy, sx + 2, sy + 14, 0xFF1DB954); // Spotify green
                mc.fontRendererObj.drawStringWithShadow(display, sx + 5, sy + 3, 0xFFFFFFFF);
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

                Gui.drawRect(x - 4, y, sw, y + 12, 0x99080808);
                Gui.drawRect(sw - 2, y, sw, y + 12, themeCol);

                mc.fontRendererObj.drawStringWithShadow(name, x - 2, y + 2, 0xFFFFFFFF);
                y += 12;
            }
        }

        // ── Discord Link (Bottom Left) ──────────────────
        if (mc.thePlayer != null) {
            String dcText = "\u00a78[\u00a75Discord\u00a78] \u00a77discord.gg/T2FeVbYw4n";
            int dcTw = mc.fontRendererObj.getStringWidth(dcText);
            Gui.drawRect(2, sh - 16, dcTw + 10, sh - 2, 0x99080808);
            Gui.drawRect(2, sh - 16, 4, sh - 2, 0xFF5865F2);
            mc.fontRendererObj.drawStringWithShadow(dcText, 6, sh - 13, 0xFFFFFFFF);
        }
    }

    private static void drawKey(int x, int y, int w, int h, int key, String name, int accent) {
        boolean pressed = Keyboard.isKeyDown(key);
        Gui.drawRect(x, y, x + w, y + h, pressed ? accent : 0x77000000);
        int fw = mc.fontRendererObj.getStringWidth(name);
        mc.fontRendererObj.drawStringWithShadow(name, x + (w - fw) / 2, y + (h - 8) / 2, pressed ? 0xFFFFFFFF : 0xFFAAAAAA);
    }

    private static void drawMouse(int x, int y, int w, int h, int btn, String name, int accent) {
        boolean pressed = Mouse.isButtonDown(btn);
        Gui.drawRect(x, y, x + w, y + h, pressed ? accent : 0x77000000);
        int fw = mc.fontRendererObj.getStringWidth(name);
        mc.fontRendererObj.drawStringWithShadow(name, x + (w - fw) / 2, y + (h - 8) / 2, pressed ? 0xFFFFFFFF : 0xFFAAAAAA);
    }
}
