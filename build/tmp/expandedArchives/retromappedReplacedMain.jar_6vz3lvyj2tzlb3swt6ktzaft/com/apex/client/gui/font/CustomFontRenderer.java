package com.apex.client.gui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CustomFontRenderer {
    private final Font font;
    private final boolean antiAlias;
    private final boolean fractionalMetrics;
    private final CharData[] charData = new CharData[256];
    private DynamicTexture tex;
    private int fontHeight = -1;

    public CustomFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        setupMinecraftColorCodes();
        generateFont();
    }

    private void generateFont() {
        BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        
        FontMetrics fontMetrics = g.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        
        for (int i = 0; i < 256; i++) {
            char ch = (char) i;
            CharData charData = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
            charData.width = (dimensions.getBounds().width + 8);
            charData.height = dimensions.getBounds().height;
            
            if (positionX + charData.width >= 512) {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }
            if (charData.height > charHeight) charHeight = charData.height;
            
            charData.storedX = positionX;
            charData.storedY = positionY;
            if (charData.height > this.fontHeight) this.fontHeight = charData.height;
            
            this.charData[i] = charData;
            g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
        }
        tex = new DynamicTexture(img);
    }

    public int drawString(String text, float x, float y, int color) {
        return drawString(text, x, y, color, false);
    }
    
    public int drawStringWithShadow(String text, float x, float y, int color) {
        drawString(text, x + 1, y + 1, (color & 0xFCFCFC) >> 2 | color & 0xFF000000, true);
        return drawString(text, x, y, color, false);
    }

    public int drawString(String text, float x, float y, int color, boolean shadow) {
        if (text == null) return 0;
        x -= 2;
        y -= 2;
        if ((color & 0xFC000000) == 0) color |= 0xFF000000;
        if (shadow) color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.func_179131_c(red, green, blue, alpha);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(tex.func_110552_b());

        GL11.glBegin(GL11.GL_TRIANGLES);
        float currentX = x;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 256) {
                drawChar(charData[c], currentX, y);
                currentX += charData[c].width - 8;
            }
        }
        GL11.glEnd();

        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
        return (int) currentX;
    }

    private void drawChar(CharData charData, float x, float y) {
        float renderX = x;
        float renderY = y;
        float renderWidth = charData.width;
        float renderHeight = charData.height;
        float srcX = charData.storedX;
        float srcY = charData.storedY;
        float srcWidth = charData.width;
        float srcHeight = charData.height;
        float imgSize = 512.0f;

        // Triangle 1
        GL11.glTexCoord2f(srcX / imgSize, srcY / imgSize);
        GL11.glVertex2f(renderX, renderY);
        GL11.glTexCoord2f(srcX / imgSize, (srcY + srcHeight) / imgSize);
        GL11.glVertex2f(renderX, renderY + renderHeight);
        GL11.glTexCoord2f((srcX + srcWidth) / imgSize, (srcY + srcHeight) / imgSize);
        GL11.glVertex2f(renderX + renderWidth, renderY + renderHeight);

        // Triangle 2
        GL11.glTexCoord2f(srcX / imgSize, srcY / imgSize);
        GL11.glVertex2f(renderX, renderY);
        GL11.glTexCoord2f((srcX + srcWidth) / imgSize, (srcY + srcHeight) / imgSize);
        GL11.glVertex2f(renderX + renderWidth, renderY + renderHeight);
        GL11.glTexCoord2f((srcX + srcWidth) / imgSize, srcY / imgSize);
        GL11.glVertex2f(renderX + renderWidth, renderY);
    }

    public int getStringWidth(String text) {
        if (text == null) return 0;
        int width = 0;
        for (char c : text.toCharArray()) {
            if (c < 256) width += charData[c].width - 8;
        }
        return width;
    }

    private void setupMinecraftColorCodes() {
        // Simple implementation, skipped color code parsing for brevity in base logic
    }

    class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }
}
