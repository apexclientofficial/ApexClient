package com.apex.client.module.player;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastPlace extends Module {
    private final NumberSetting delay = new NumberSetting("Delay", 0, 0, 4, 1);

    public FastPlace() {
        super("FastPlace", "Remove placement delay", Category.PLAYER);
        addSetting(delay);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;
        // Reduce rightClickDelayTimer to allow faster placing
        try {
            java.lang.reflect.Field f = PlayerControllerMP.class.getDeclaredField("field_78781_i"); // blockHitDelay / rightClickDelayTimer
            f.setAccessible(true);
            int val = f.getInt(mc.field_71442_b);
            if (val > delay.getIntValue()) f.setInt(mc.field_71442_b, delay.getIntValue());
        } catch (Exception ignored) {}
    }
}
