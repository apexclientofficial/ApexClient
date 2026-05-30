package com.apex.client.module.player;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;
import net.minecraft.item.ItemStack;

public class Regen extends Module {
    private final NumberSetting interval = new NumberSetting("Interval", 40, 5, 100, 5);
    private int timer = 0;

    public Regen() {
        super("Regen", "Sends regeneration-related ticks to heal faster", Category.PLAYER);
        addSetting(interval);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;
        timer++;
        if (timer >= interval.getIntValue()) {
            timer = 0;
            // Sends a player tick to the server to nudge food/health regen
            mc.field_71439_g.field_71174_a.func_147297_a(new net.minecraft.network.play.client.C03PacketPlayer(mc.field_71439_g.field_70122_E));
        }
    }
}
