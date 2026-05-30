package com.apex.client.module.movement;

import com.apex.client.Module;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Automatically sprints when walking forward", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;
        if (mc.field_71439_g.field_70701_bs > 0 && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.field_70123_F && mc.field_71439_g.func_71024_bL().func_75116_a() > 6) {
            mc.field_71439_g.func_70031_b(true);
        }
    }
}
