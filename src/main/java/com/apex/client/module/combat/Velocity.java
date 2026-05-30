package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class Velocity extends Module {

    private final NumberSetting horizontal = new NumberSetting("Horizontal", 0.0, 0.0, 100.0, 10.0);
    private final NumberSetting vertical = new NumberSetting("Vertical", 0.0, 0.0, 100.0, 10.0);

    public Velocity() {
        super("Velocity", "Reduces knockback", Category.COMBAT);
        addSetting(horizontal);
        addSetting(vertical);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;

        if (mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && mc.thePlayer.maxHurtTime > 0) {
            double hMult = horizontal.getValue() / 100.0;
            double vMult = vertical.getValue() / 100.0;

            mc.thePlayer.motionX *= hMult;
            mc.thePlayer.motionY *= vMult;
            mc.thePlayer.motionZ *= hMult;
        }
    }
}
