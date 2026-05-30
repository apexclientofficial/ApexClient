package com.apex.client.module.movement;

import com.apex.client.Module;
import com.apex.client.setting.NumberSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Jesus extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", 1.0, 0.1, 3.0, 0.1);

    public Jesus() {
        super("Jesus", "Walk on water", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;

        BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.01, mc.field_71439_g.field_70161_v);
        boolean onWater = mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150355_j
                || mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150358_i;

        if (onWater && mc.field_71439_g.field_70181_x < 0) {
            mc.field_71439_g.field_70181_x = 0.04;
        }
    }
}
