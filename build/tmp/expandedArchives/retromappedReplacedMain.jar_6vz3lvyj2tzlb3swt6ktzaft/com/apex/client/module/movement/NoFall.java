package com.apex.client.module.movement;

import com.apex.client.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Prevents fall damage", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;
        if (mc.field_71439_g.field_70143_R > 2.0F) {
            mc.field_71439_g.field_71174_a.func_147297_a(new C03PacketPlayer(true));
        }
    }
}
