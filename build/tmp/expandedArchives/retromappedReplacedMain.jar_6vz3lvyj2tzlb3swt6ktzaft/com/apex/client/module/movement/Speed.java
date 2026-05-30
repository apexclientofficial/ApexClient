package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;

public class Speed extends Module {
    private final NumberSetting speedMultiplier = new NumberSetting("Speed", 1.5, 1.0, 5.0, 0.1);

    public Speed() {
        super("Speed", "Move faster than normal", Category.MOVEMENT);
        addSetting(speedMultiplier);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;

        if (mc.field_71439_g.field_70122_E && mc.field_71439_g.field_70701_bs != 0) {
            double yaw = Math.toRadians(mc.field_71439_g.field_70177_z);
            double forward = mc.field_71439_g.field_71158_b.field_78900_b;
            double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
            double mult = speedMultiplier.getValue() * 0.1;

            mc.field_71439_g.field_70159_w += -Math.sin(yaw) * forward * mult + Math.cos(yaw) * strafe * mult;
            mc.field_71439_g.field_70179_y += Math.cos(yaw) * forward * mult + Math.sin(yaw) * strafe * mult;

            // Cap velocity
            double horizontalSpeed = Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
            double maxSpeed = speedMultiplier.getValue() * 0.2;
            if (horizontalSpeed > maxSpeed) {
                mc.field_71439_g.field_70159_w = (mc.field_71439_g.field_70159_w / horizontalSpeed) * maxSpeed;
                mc.field_71439_g.field_70179_y = (mc.field_71439_g.field_70179_y / horizontalSpeed) * maxSpeed;
            }
        }
    }
}
