package com.apex.client.module.movement;

import com.apex.client.Module;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", "Prevents slowdown from items and blocks", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;
        // Prevents cobweb and item-use slowdown by overriding movement speeds.
        // Requires a mixin into EntityPlayerSP.pushOutOfBlocks / getAIMoveSpeed for full effect.
        // Client-side: simply keep sprinting state active.
        if (mc.thePlayer.isUsingItem()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
