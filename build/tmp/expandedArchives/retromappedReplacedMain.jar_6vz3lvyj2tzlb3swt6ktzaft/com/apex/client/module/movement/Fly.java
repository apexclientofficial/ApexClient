package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.ModeSetting;
import com.apex.client.setting.NumberSetting;

public class Fly extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Velocity", "Velocity", "Vanilla");
    private final NumberSetting speed = new NumberSetting("Speed", 2.0, 0.5, 5.0, 0.1);

    public Fly() {
        super("Fly", "Allows you to fly", Category.MOVEMENT);
        addSetting(mode);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;

        if (mode.is("Vanilla")) {
            mc.field_71439_g.field_71075_bZ.field_75100_b = true;
            mc.field_71439_g.field_71075_bZ.func_75092_a(speed.getFloatValue() * 0.05f);
        } else if (mode.is("Velocity")) {
            mc.field_71439_g.field_71075_bZ.field_75100_b = false;
            mc.field_71439_g.field_70181_x = 0.0D;
            
            if (mc.field_71474_y.field_74314_A.func_151470_d()) {
                mc.field_71439_g.field_70181_x = speed.getValue() * 0.5;
            } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
                mc.field_71439_g.field_70181_x = -speed.getValue() * 0.5;
            }

            double yaw = Math.toRadians(mc.field_71439_g.field_70177_z);
            double forward = mc.field_71439_g.field_71158_b.field_78900_b;
            double strafe = mc.field_71439_g.field_71158_b.field_78902_a;

            if (forward != 0 || strafe != 0) {
                double speedVal = speed.getValue() * 0.2;
                mc.field_71439_g.field_70159_w = -Math.sin(yaw) * forward * speedVal + Math.cos(yaw) * strafe * speedVal;
                mc.field_71439_g.field_70179_y = Math.cos(yaw) * forward * speedVal + Math.sin(yaw) * strafe * speedVal;
            } else {
                mc.field_71439_g.field_70159_w = 0;
                mc.field_71439_g.field_70179_y = 0;
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.field_71439_g != null) {
            mc.field_71439_g.field_71075_bZ.field_75100_b = false;
            mc.field_71439_g.field_71075_bZ.func_75092_a(0.05f);
        }
    }
}
