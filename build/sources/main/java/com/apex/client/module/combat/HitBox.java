package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import com.apex.client.util.TargetUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class HitBox extends Module {
    private final NumberSetting expand = new NumberSetting("Expand", 0.15, 0.0, 0.5, 0.01);

    public HitBox() {
        super("HitBox", "Expands entity hitboxes for easier hits", Category.COMBAT);
        addSetting(expand);
    }

    public double getExpand() { return expand.getValue(); }
    // Actual expansion is done per-entity in getEntityBoundingBox hook (future mixin).
}
