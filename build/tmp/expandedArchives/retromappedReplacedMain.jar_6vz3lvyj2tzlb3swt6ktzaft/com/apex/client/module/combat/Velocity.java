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
        if (mc.field_71439_g == null) return;

        if (mc.field_71439_g.field_70737_aN == mc.field_71439_g.field_70738_aO && mc.field_71439_g.field_70738_aO > 0) {
            double hMult = horizontal.getValue() / 100.0;
            double vMult = vertical.getValue() / 100.0;

            mc.field_71439_g.field_70159_w *= hMult;
            mc.field_71439_g.field_70181_x *= vMult;
            mc.field_71439_g.field_70179_y *= hMult;
        }
    }
}
