package com.apex.client.module.movement;

import com.apex.client.Module;
import net.minecraft.client.entity.EntityPlayerSP;

public class Freecam extends Module {

    private double savedX, savedY, savedZ;
    private float savedYaw, savedPitch;

    public Freecam() {
        super("Freecam", "Detach camera from your body", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (mc.field_71439_g == null) return;
        savedX = mc.field_71439_g.field_70165_t;
        savedY = mc.field_71439_g.field_70163_u;
        savedZ = mc.field_71439_g.field_70161_v;
        savedYaw = mc.field_71439_g.field_70177_z;
        savedPitch = mc.field_71439_g.field_70125_A;
        mc.field_71439_g.field_70145_X = true;
    }

    @Override
    public void onDisable() {
        if (mc.field_71439_g == null) return;
        mc.field_71439_g.field_70145_X = false;
        mc.field_71439_g.func_70634_a(savedX, savedY, savedZ);
        mc.field_71439_g.field_70177_z = savedYaw;
        mc.field_71439_g.field_70125_A = savedPitch;
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;
        mc.field_71439_g.field_70145_X = true;
        mc.field_71439_g.field_70122_E = false;
        mc.field_71439_g.field_70181_x = 0;

        if (mc.field_71474_y.field_74314_A.func_151470_d()) mc.field_71439_g.field_70181_x = 0.1;
        if (mc.field_71474_y.field_74311_E.func_151470_d()) mc.field_71439_g.field_70181_x = -0.1;

        double yaw = Math.toRadians(mc.field_71439_g.field_70177_z);
        double fwd = mc.field_71439_g.field_71158_b.field_78900_b;
        double str = mc.field_71439_g.field_71158_b.field_78902_a;
        double spd = 0.15;

        mc.field_71439_g.field_70159_w = (-Math.sin(yaw) * fwd + Math.cos(yaw) * str) * spd;
        mc.field_71439_g.field_70179_y = (Math.cos(yaw) * fwd + Math.sin(yaw) * str) * spd;
    }
}
