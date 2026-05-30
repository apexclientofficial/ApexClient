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
        if (mc.field_71439_g == null) return;
        mc.field_71439_g.func_70690_d(new PotionEffect(Potion.field_76439_r.field_76415_H, 300, 5, false, false));
    }

    @Override
    public void onDisable() {
        if (mc.field_71439_g != null) {
            mc.field_71439_g.func_82170_o(Potion.field_76439_r.field_76415_H);
        }
    }
}
