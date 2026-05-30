package com.apex.client.module.render;

import com.apex.client.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {

    public FullBright() {
        super("FullBright", "Makes everything fully bright", Category.RENDER);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 300, 5, false, false));
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.removePotionEffect(Potion.nightVision.id);
        }
    }
}
