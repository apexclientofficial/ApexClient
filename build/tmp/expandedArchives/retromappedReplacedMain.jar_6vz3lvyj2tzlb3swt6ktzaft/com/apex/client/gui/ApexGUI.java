package com.apex.client.gui;

import com.apex.client.ApexClient;
import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.ModeSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.setting.Setting;
import com.apex.client.setting.StringSetting;
import com.apex.client.setting.ButtonSetting;
import com.apex.client.setting.KeybindSetting;
import com.apex.client.setting.ColorSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class ApexGUI extends GuiScreen {

    private Module.Category currentCategory = Module.Category.COMBAT;
    private int scrollY = 0;

    // Layout constants
    private static final int ICON_BAR_WIDTH = 70;
    private static final int PANEL_X_OFFSET = 10;
    private static final int PANEL_Y_OFFSET = 10;
    private static final int MODULE_LIST_WIDTH = 170;
    private static final int SETTINGS_PANEL_WIDTH = 165;
    private static final int MODULE_H = 22;
    private static final int SETTING_H_BOOL = 20;
    private static final int SETTING_H_NUM  = 28;
    private static final int SETTING_H_MODE = 20;
    private static final int SETTING_H_BIND = 20;
    private static final int SETTING_H_STR  = 20;
    private static final int SETTING_H_BTN  = 20;
    private static final int SETTING_H_COL  = 42;

    // Slide out
    private Module selectedModule = null;
    private float settingsSlide = 0f;

    // Slider drag
    private NumberSetting draggingSlider = null;
    private int draggingSliderX, draggingSliderW;

    // Color drag
    private ColorSetting draggingColor = null;
    private int draggingColorChannel = 0; // 0=R, 1=G, 2=B
    private int draggingColorX, draggingColorW;

    // Keybind state
    private KeybindSetting listeningBind = null;

    // String input state
    private StringSetting listeningString = null;

    @Override
    public void func_73866_w_() {
        // Load background blur shader
        try {
            if (field_146297_k.field_71460_t.func_147706_e() != null) {
                field_146297_k.field_71460_t.func_147706_e().func_148021_a();
            }
            field_146297_k.field_71460_t.func_175069_a(new ResourceLocation("shaders/post/blur.json"));
        } catch (Exception ignored) {}
    }

    @Override
    public void func_146281_b() {
        try {
            if (field_146297_k.field_71460_t.func_147706_e() != null) {
                field_146297_k.field_71460_t.func_147706_e().func_148021_a();
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(field_146297_k);
        int sw = sr.func_78326_a();
        int sh = sr.func_78328_b();

        // --- Dark overlay ---
        func_73734_a(0, 0, sw, sh, 0x99000000);

        // ----------- Panel geometry -----------
        int panelX = PANEL_X_OFFSET + ICON_BAR_WIDTH;
        int panelY = PANEL_Y_OFFSET;
        int panelH = sh - PANEL_Y_OFFSET * 2;

        int themeCol = com.apex.client.module.misc.HUDColor.getColor();
        int themeColDark = com.apex.client.module.misc.HUDColor.getColorDark();

        // ----------- Icon sidebar -----------
        func_73734_a(PANEL_X_OFFSET, PANEL_Y_OFFSET, PANEL_X_OFFSET + ICON_BAR_WIDTH, PANEL_Y_OFFSET + panelH, 0xEE0D0D0D);

        // APEX logo at top of icon bar
        drawApexLogo(PANEL_X_OFFSET + 7, PANEL_Y_OFFSET + 8, themeCol);

        // Thin accent separator below logo
        func_73734_a(PANEL_X_OFFSET + 6, PANEL_Y_OFFSET + 32, PANEL_X_OFFSET + ICON_BAR_WIDTH - 6, PANEL_Y_OFFSET + 33, themeCol);

        // Category icon buttons
        int iconY = PANEL_Y_OFFSET + 42;
        for (Module.Category cat : Module.Category.values()) {
            boolean active = cat == currentCategory;
            // Active: accent bar on left
            if (active) {
                func_73734_a(PANEL_X_OFFSET, iconY - 2, PANEL_X_OFFSET + 3, iconY + 16, themeCol);
                func_73734_a(PANEL_X_OFFSET, iconY - 2, PANEL_X_OFFSET + ICON_BAR_WIDTH, iconY + 16, 0x22FFFFFF);
            }
            // Draw category short label centered in bar
            String label = getCategoryIcon(cat);
            int lw = field_146297_k.field_71466_p.func_78256_a(label);
            int col = active ? 0xFFFFFFFF : 0xFF888888;
            field_146297_k.field_71466_p.func_78276_b(label, PANEL_X_OFFSET + (ICON_BAR_WIDTH - lw) / 2, iconY + 3, col);
            iconY += 26;
        }

        // Discord button at bottom of icon bar
        int discordY = PANEL_Y_OFFSET + panelH - 22;
        boolean discordHovered = mouseX >= PANEL_X_OFFSET && mouseX < PANEL_X_OFFSET + ICON_BAR_WIDTH
                && mouseY >= discordY && mouseY < discordY + 18;
        func_73734_a(PANEL_X_OFFSET + 4, discordY, PANEL_X_OFFSET + ICON_BAR_WIDTH - 4, discordY + 18,
                discordHovered ? 0xFF5865F2 : 0xFF3E4689);
        String dcLabel = "Discord";
        int dcLw = field_146297_k.field_71466_p.func_78256_a(dcLabel);
        field_146297_k.field_71466_p.func_78276_b(dcLabel, PANEL_X_OFFSET + (ICON_BAR_WIDTH - dcLw) / 2, discordY + 5, 0xFFFFFFFF);

        // ----------- Module list panel -----------
        func_73734_a(panelX, panelY, panelX + MODULE_LIST_WIDTH, panelY + panelH, 0xEE111111);

        // Category title header
        func_73734_a(panelX, panelY, panelX + MODULE_LIST_WIDTH, panelY + 26, 0xEE191919);
        String catName = getCategoryIcon(currentCategory);
        field_146297_k.field_71466_p.func_175063_a(catName, panelX + 8, panelY + 9, 0xFFFFFFFF);
        func_73734_a(panelX, panelY + 25, panelX + MODULE_LIST_WIDTH, panelY + 26, themeColDark);

        // Clip and draw modules
        List<Module> modules = ApexClient.instance.getModuleManager().getModulesByCategory(currentCategory);
        int modY = panelY + 28 + scrollY;
        int clipTop = panelY + 26;
        int clipBot = panelY + panelH;

        for (Module m : modules) {
            int rowBottom = modY + MODULE_H;
            if (rowBottom > clipTop && modY < clipBot) {
                boolean enabled = m.isEnabled();
                boolean hovered = mouseX >= panelX && mouseX < panelX + MODULE_LIST_WIDTH
                        && mouseY >= modY && mouseY < modY + MODULE_H;
                boolean selected = m == selectedModule;

                // Row background
                int rowBg = selected ? 0x331A0000 : (hovered ? 0x22FFFFFF : 0x00000000);
                func_73734_a(panelX, modY, panelX + MODULE_LIST_WIDTH, modY + MODULE_H, rowBg);

                if (enabled) {
                    func_73734_a(panelX, modY, panelX + MODULE_LIST_WIDTH, modY + MODULE_H, 0x33FFFFFF);
                    // indicator dot
                    func_73734_a(panelX + 4, modY + (MODULE_H - 4) / 2, panelX + 8, modY + (MODULE_H + 4) / 2, themeCol);
                } else {
                    func_73734_a(panelX + 4, modY + (MODULE_H - 4) / 2, panelX + 8, modY + (MODULE_H + 4) / 2, 0xFF555555);
                }

                // Module name
                int nameCol = enabled ? 0xFFFFFFFF : 0xFF999999;
                field_146297_k.field_71466_p.func_78276_b(m.getName(), panelX + 14, modY + 7, nameCol);

                // Right-click hint (chevron if selected)
                if (selected) {
                    field_146297_k.field_71466_p.func_78276_b(">", panelX + MODULE_LIST_WIDTH - 12, modY + 7, themeCol);
                }

                // Separator
                func_73734_a(panelX + 4, modY + MODULE_H - 1, panelX + MODULE_LIST_WIDTH - 4, modY + MODULE_H, 0x22FFFFFF);
            }
            modY += MODULE_H;
        }

        // Scroll clamping
        int totalH = modules.size() * MODULE_H;
        int visible = panelH - 28;
        int minScroll = Math.min(0, visible - totalH);
        if (scrollY < minScroll) scrollY = minScroll;
        if (scrollY > 0) scrollY = 0;

        // Mouse wheel
        int wheel = Mouse.getDWheel();
        if (wheel != 0) scrollY += (wheel > 0 ? 1 : -1) * 20;

        // ----------- Settings slide-out -----------
        if (selectedModule != null) settingsSlide = Math.min(1f, settingsSlide + 0.15f);
        else settingsSlide = Math.max(0f, settingsSlide - 0.15f);

        if (settingsSlide > 0.01f) {
            int setX = panelX + MODULE_LIST_WIDTH + (int) ((settingsSlide - 1f) * SETTINGS_PANEL_WIDTH);
            int setW = SETTINGS_PANEL_WIDTH;

            func_73734_a(setX, panelY, setX + setW, panelY + panelH, 0xEE0F0F0F);
            func_73734_a(setX, panelY, setX + setW, panelY + 26, 0xEE191919);

            Module mod = selectedModule;
            if (mod != null) {
                field_146297_k.field_71466_p.func_175063_a(mod.getName(), setX + 8, panelY + 9, themeCol);
                func_73734_a(setX, panelY + 25, setX + setW, panelY + 26, themeColDark);

                int setY = panelY + 32;
                for (Setting s : mod.getSettings()) {
                    if (s instanceof BooleanSetting) {
                        BooleanSetting b = (BooleanSetting) s;
                        boolean val = b.isEnabled();
                        // Toggle track
                        func_73734_a(setX + setW - 28, setY + 2, setX + setW - 8, setY + 12, val ? themeColDark : 0xFF333333);
                        func_73734_a(val ? setX + setW - 18 : setX + setW - 28, setY + 2, val ? setX + setW - 8 : setX + setW - 18, setY + 12, val ? themeCol : 0xFF666666);
                        field_146297_k.field_71466_p.func_78276_b(s.getName(), setX + 8, setY + 3, 0xFFDDDDDD);
                        setY += SETTING_H_BOOL;
                    } else if (s instanceof NumberSetting) {
                        NumberSetting n = (NumberSetting) s;
                        String label = String.format("%s: %.2f", s.getName(), n.getValue());
                        field_146297_k.field_71466_p.func_78276_b(label, setX + 8, setY, 0xFFDDDDDD);
                        int sliderX = setX + 8;
                        int sliderW = setW - 16;
                        // Track
                        func_73734_a(sliderX, setY + 11, sliderX + sliderW, setY + 18, 0xFF333333);
                        // Fill
                        int fill = (int) (sliderW * n.getPercentage());
                        func_73734_a(sliderX, setY + 11, sliderX + fill, setY + 18, themeCol);
                        // Thumb
                        func_73734_a(sliderX + fill - 2, setY + 9, sliderX + fill + 2, setY + 20, 0xFFFFFFFF);
                        setY += SETTING_H_NUM;
                    } else if (s instanceof ModeSetting) {
                        ModeSetting m2 = (ModeSetting) s;
                        field_146297_k.field_71466_p.func_78276_b(s.getName() + ":", setX + 8, setY + 3, 0xFFDDDDDD);
                        String val = m2.getValue();
                        int vw = field_146297_k.field_71466_p.func_78256_a(val);
                        field_146297_k.field_71466_p.func_78276_b(val + (m2.isOpen() ? " \u25b2" : " \u25bc"), setX + setW - vw - 18, setY + 3, themeCol);
                        setY += SETTING_H_MODE;
                        
                        if (m2.isOpen()) {
                            for (String mode : m2.getModes()) {
                                boolean isSelected = mode.equals(m2.getValue());
                                func_73734_a(setX + 4, setY, setX + setW - 4, setY + 16, 0xFF181818);
                                field_146297_k.field_71466_p.func_78276_b((isSelected ? "> " : "") + mode, setX + 16, setY + 4, isSelected ? themeCol : 0xFF888888);
                                setY += 16;
                            }
                        }
                    } else if (s instanceof KeybindSetting) {
                        KeybindSetting k = (KeybindSetting) s;
                        field_146297_k.field_71466_p.func_78276_b("Bind:", setX + 8, setY + 3, 0xFFDDDDDD);
                        String val = (listeningBind == k) ? "Listening..." : k.getKeyName();
                        int vw = field_146297_k.field_71466_p.func_78256_a(val);
                        field_146297_k.field_71466_p.func_78276_b(val, setX + setW - vw - 8, setY + 3, themeCol);
                        setY += SETTING_H_BIND;
                    } else if (s instanceof StringSetting) {
                        StringSetting str = (StringSetting) s;
                        field_146297_k.field_71466_p.func_78276_b(s.getName() + ":", setX + 8, setY + 3, 0xFFDDDDDD);
                        String val = str.getValue() + (listeningString == str && (System.currentTimeMillis() % 1000 < 500) ? "_" : "");
                        int vw = field_146297_k.field_71466_p.func_78256_a(val);
                        // If string is too wide, truncate
                        if (vw > setW - 60) {
                            val = "..." + val.substring(Math.max(0, val.length() - 10));
                            vw = field_146297_k.field_71466_p.func_78256_a(val);
                        }
                        field_146297_k.field_71466_p.func_78276_b(val, setX + setW - vw - 8, setY + 3, (listeningString == str) ? 0xFFFFFFFF : themeCol);
                        setY += SETTING_H_STR;
                    } else if (s instanceof ButtonSetting) {
                        // Draw a clickable button
                        boolean hov = mouseX >= setX + 8 && mouseX < setX + setW - 8
                                && mouseY >= setY + 1 && mouseY < setY + SETTING_H_BTN - 1;
                        func_73734_a(setX + 8, setY + 1, setX + setW - 8, setY + SETTING_H_BTN - 1,
                                hov ? themeCol : themeColDark);
                        int tw = field_146297_k.field_71466_p.func_78256_a(s.getName());
                        field_146297_k.field_71466_p.func_175063_a(s.getName(),
                                setX + (setW - tw) / 2, setY + 4, 0xFFFFFFFF);
                        setY += SETTING_H_BTN;
                    } else if (s instanceof ColorSetting) {
                        ColorSetting c = (ColorSetting) s;
                        field_146297_k.field_71466_p.func_78276_b(s.getName() + ":", setX + 8, setY + 3, 0xFFDDDDDD);
                        
                        // Draw Preview Box
                        func_73734_a(setX + setW - 25, setY + 3, setX + setW - 8, setY + 12, c.getRGB() | 0xFF000000);
                        
                        int sliderX = setX + 8;
                        int sliderW = setW - 16;
                        
                        // R
                        func_73734_a(sliderX, setY + 16, sliderX + sliderW, setY + 20, 0xFF333333);
                        func_73734_a(sliderX, setY + 16, sliderX + (int)(sliderW * (c.getR()/255f)), setY + 20, 0xFFFF0000);
                        // G
                        func_73734_a(sliderX, setY + 24, sliderX + sliderW, setY + 28, 0xFF333333);
                        func_73734_a(sliderX, setY + 24, sliderX + (int)(sliderW * (c.getG()/255f)), setY + 28, 0xFF00FF00);
                        // B
                        func_73734_a(sliderX, setY + 32, sliderX + sliderW, setY + 36, 0xFF333333);
                        func_73734_a(sliderX, setY + 32, sliderX + (int)(sliderW * (c.getB()/255f)), setY + 36, 0xFF0000FF);
                        
                        setY += SETTING_H_COL;
                    }
                    // Separator
                    func_73734_a(setX + 4, setY - 1, setX + setW - 4, setY, 0x22FFFFFF);
                }
            }
        }

        // Handle dragging slider
        if (draggingSlider != null && Mouse.isButtonDown(0)) {
            double pct = Math.max(0, Math.min(1, (double)(mouseX - draggingSliderX) / draggingSliderW));
            draggingSlider.setFromPercentage(pct);
        } else {
            draggingSlider = null;
        }

        // Handle dragging color
        if (draggingColor != null && Mouse.isButtonDown(0)) {
            double pct = Math.max(0, Math.min(1, (double)(mouseX - draggingColorX) / draggingColorW));
            int val = (int)(255 * pct);
            if (draggingColorChannel == 0) draggingColor.setR(val);
            else if (draggingColorChannel == 1) draggingColor.setG(val);
            else if (draggingColorChannel == 2) draggingColor.setB(val);
        } else {
            draggingColor = null;
        }

        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    /** Draw a stylized "APEX" text logo */
    private void drawApexLogo(int x, int y, int color) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(0.8f, 0.8f, 1f);
        int sx = (int)(x / 0.8f);
        int sy = (int)(y / 0.8f);
        field_146297_k.field_71466_p.func_78276_b("APEX", sx, sy, color);
        GlStateManager.func_179121_F();
    }

    private String getCategoryIcon(Module.Category cat) {
        switch (cat) {
            case COMBAT:   return "\u2694 Combat";
            case MOVEMENT: return "\ud83d\udc5f Movement";
            case PLAYER:   return "\ud83d\udc64 Player";
            case RENDER:   return "\ud83d\udc41 Render";
            case MISC:     return "\u2699 Misc";
            default:       return cat.name;
        }
    }

    @Override
    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(field_146297_k);
        int sh = sr.func_78328_b();
        int panelX = PANEL_X_OFFSET + ICON_BAR_WIDTH;
        int panelY = PANEL_Y_OFFSET;
        int panelH = sh - PANEL_Y_OFFSET * 2;

        // --- Discord button click ---
        int discordY = PANEL_Y_OFFSET + panelH - 22;
        if (mouseX >= PANEL_X_OFFSET && mouseX < PANEL_X_OFFSET + ICON_BAR_WIDTH
                && mouseY >= discordY && mouseY < discordY + 18) {
            try {
                Desktop.getDesktop().browse(new URI("https://discord.gg/T2FeVbYw4n"));
            } catch (Exception ignored) {}
            return;
        }

        // --- Icon bar category click ---
        int iconY = PANEL_Y_OFFSET + 42;
        for (Module.Category cat : Module.Category.values()) {
            if (mouseX >= PANEL_X_OFFSET && mouseX < PANEL_X_OFFSET + ICON_BAR_WIDTH
                    && mouseY >= iconY - 2 && mouseY < iconY + 16) {
                currentCategory = cat;
                scrollY = 0;
                selectedModule = null;
                settingsSlide = 0f;
                return;
            }
            iconY += 26;
        }

        // --- Module list click ---
        List<Module> modules = ApexClient.instance.getModuleManager().getModulesByCategory(currentCategory);
        int modY = panelY + 28 + scrollY;
        for (Module m : modules) {
            if (mouseX >= panelX && mouseX < panelX + MODULE_LIST_WIDTH
                    && mouseY >= modY && mouseY < modY + MODULE_H) {
                if (mouseButton == 0) {
                    m.toggle();
                } else if (mouseButton == 1) {
                    selectedModule = (selectedModule == m) ? null : m;
                }
                return;
            }
            modY += MODULE_H;
        }

        // --- Settings panel click ---
        if (selectedModule != null && settingsSlide > 0.7f) {
            int setX = panelX + MODULE_LIST_WIDTH;
            int setW = SETTINGS_PANEL_WIDTH;
            if (mouseX >= setX && mouseX < setX + setW) {
                int setY = panelY + 32;
                for (Setting s : selectedModule.getSettings()) {
                    if (s instanceof BooleanSetting) {
                        BooleanSetting b = (BooleanSetting) s;
                        if (mouseY >= setY && mouseY < setY + SETTING_H_BOOL) {
                            b.toggle();
                            return;
                        }
                        setY += SETTING_H_BOOL;
                    } else if (s instanceof NumberSetting) {
                        NumberSetting n = (NumberSetting) s;
                        int sliderX = setX + 8;
                        int sliderW = setW - 16;
                        if (mouseY >= setY + 9 && mouseY < setY + 20) {
                            draggingSlider = n;
                            draggingSliderX = sliderX;
                            draggingSliderW = sliderW;
                            double pct = Math.max(0, Math.min(1, (double)(mouseX - sliderX) / sliderW));
                            n.setFromPercentage(pct);
                            return;
                        }
                        setY += SETTING_H_NUM;
                    } else if (s instanceof ModeSetting) {
                        ModeSetting m2 = (ModeSetting) s;
                        if (mouseY >= setY && mouseY < setY + SETTING_H_MODE) {
                            m2.setOpen(!m2.isOpen());
                            return;
                        }
                        setY += SETTING_H_MODE;
                        if (m2.isOpen()) {
                            for (String mode : m2.getModes()) {
                                if (mouseY >= setY && mouseY < setY + 16 && mouseX >= setX && mouseX <= setX + setW) {
                                    m2.setValue(mode);
                                    m2.setOpen(false);
                                    // If this is the Config module's "Load Config" dropdown, trigger load
                                    if (s.getName().equals("Load Config") && selectedModule instanceof com.apex.client.module.misc.Config) {
                                        ((com.apex.client.module.misc.Config) selectedModule).loadSelected();
                                    }
                                    return;
                                }
                                setY += 16;
                            }
                        }
                    } else if (s instanceof KeybindSetting) {
                        KeybindSetting k = (KeybindSetting) s;
                        if (mouseY >= setY && mouseY < setY + SETTING_H_BIND) {
                            if (mouseButton == 0) {
                                listeningBind = k;
                            } else if (mouseButton == 1) {
                                k.setCode(0); // clear
                            }
                            return;
                        }
                        setY += SETTING_H_BIND;
                    } else if (s instanceof StringSetting) {
                        StringSetting str = (StringSetting) s;
                        if (mouseY >= setY && mouseY < setY + SETTING_H_STR) {
                            if (mouseButton == 0) {
                                listeningString = str;
                            } else if (mouseButton == 1) {
                                str.setValue(""); // clear
                            }
                            return;
                        }
                        setY += SETTING_H_STR;
                    } else if (s instanceof ButtonSetting) {
                        if (mouseY >= setY + 1 && mouseY < setY + SETTING_H_BTN - 1
                                && mouseX >= setX + 8 && mouseX < setX + setW - 8) {
                            ((ButtonSetting) s).click();
                            return;
                        }
                        setY += SETTING_H_BTN;
                    } else if (s instanceof ColorSetting) {
                        ColorSetting c = (ColorSetting) s;
                        int sliderX = setX + 8;
                        int sliderW = setW - 16;
                        if (mouseX >= sliderX && mouseX < sliderX + sliderW) {
                            if (mouseY >= setY + 16 && mouseY < setY + 20) {
                                draggingColor = c; draggingColorChannel = 0; draggingColorX = sliderX; draggingColorW = sliderW;
                            } else if (mouseY >= setY + 24 && mouseY < setY + 28) {
                                draggingColor = c; draggingColorChannel = 1; draggingColorX = sliderX; draggingColorW = sliderW;
                            } else if (mouseY >= setY + 32 && mouseY < setY + 36) {
                                draggingColor = c; draggingColorChannel = 2; draggingColorX = sliderX; draggingColorW = sliderW;
                            }
                        }
                        setY += SETTING_H_COL;
                    }
                }
            }
        }

        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (listeningBind != null) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) {
                listeningBind.setCode(0);
            } else {
                listeningBind.setCode(keyCode);
            }
            listeningBind = null;
            return;
        }

        if (listeningString != null) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
                listeningString = null;
            } else if (keyCode == Keyboard.KEY_BACK) {
                String val = listeningString.getValue();
                if (val.length() > 0) {
                    listeningString.setValue(val.substring(0, val.length() - 1));
                }
            } else if (net.minecraft.util.ChatAllowedCharacters.func_71566_a(typedChar)) {
                listeningString.setValue(listeningString.getValue() + typedChar);
            }
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }
}
