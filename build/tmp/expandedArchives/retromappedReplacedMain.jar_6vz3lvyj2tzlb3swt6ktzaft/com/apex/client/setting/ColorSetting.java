package com.apex.client.setting;

public class ColorSetting extends Setting {
    private int r, g, b;

    public ColorSetting(String name, int r, int g, int b) {
        super(name);
        this.r = Math.max(0, Math.min(255, r));
        this.g = Math.max(0, Math.min(255, g));
        this.b = Math.max(0, Math.min(255, b));
    }

    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }

    public void setR(int r) { this.r = Math.max(0, Math.min(255, r)); }
    public void setG(int g) { this.g = Math.max(0, Math.min(255, g)); }
    public void setB(int b) { this.b = Math.max(0, Math.min(255, b)); }

    public int getRGB() {
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }
}
