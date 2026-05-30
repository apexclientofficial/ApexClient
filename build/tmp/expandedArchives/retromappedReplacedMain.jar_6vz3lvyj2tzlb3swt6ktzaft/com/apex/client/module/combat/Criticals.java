package com.apex.client.module.combat;

import com.apex.client.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {

    public Criticals() {
        super("Criticals", "Forces critical hits on attack", Category.COMBAT);
    }

    private boolean registered = false;

    @Override
    public void onEnable() {
        if (!registered) {
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
            registered = true;
        }
    }

    @Override
    public void onDisable() {
        if (registered) {
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(this);
            registered = false;
        }
    }

    public void doCrit() {
        if (mc.field_71439_g == null || !isEnabled()) return;
        if (mc.field_71439_g.field_70122_E) {
            double x = mc.field_71439_g.field_70165_t;
            double y = mc.field_71439_g.field_70163_u;
            double z = mc.field_71439_g.field_70161_v;

            mc.field_71439_g.field_71174_a.func_147297_a(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, false));
            mc.field_71439_g.field_71174_a.func_147297_a(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            mc.field_71439_g.field_71174_a.func_147297_a(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-5, z, false));
            mc.field_71439_g.field_71174_a.func_147297_a(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        }
    }

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onAttack(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
        if (event.entityPlayer == mc.field_71439_g) {
            doCrit();
        }
    }
}
