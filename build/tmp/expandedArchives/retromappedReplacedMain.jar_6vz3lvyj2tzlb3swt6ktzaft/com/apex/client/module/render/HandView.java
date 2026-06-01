package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.Field;

public class HandView extends Module {

    private static HandView instance;

    private final NumberSetting xOffset = new NumberSetting("X Offset", 0.0, -2.0, 2.0, 0.05);
    private final NumberSetting yOffset = new NumberSetting("Y Offset", 0.0, -2.0, 2.0, 0.05);
    private final NumberSetting zOffset = new NumberSetting("Z Offset", 0.0, -2.0, 2.0, 0.05);
    private final NumberSetting tilt = new NumberSetting("Sideways Tilt", 0.0, -180.0, 180.0, 1.0);
    private final NumberSetting swingSpeed = new NumberSetting("Swing Slowdown", 6, 6, 30, 1);
    private final BooleanSetting noSwing = new BooleanSetting("No Swing", false);

    private Field swingProgressIntField;
    private Field isSwingInProgressField;

    public HandView() {
        super("HandView", "Customize first-person hand position & swing", Category.RENDER);
        addSetting(xOffset);
        addSetting(yOffset);
        addSetting(zOffset);
        addSetting(tilt);
        addSetting(swingSpeed);
        addSetting(noSwing);
        instance = this;

        // Resolve swingProgressInt field via reflection (handles obfuscated & dev names)
        try {
            swingProgressIntField = EntityLivingBase.class.getDeclaredField("field_70773_bE");
            swingProgressIntField.setAccessible(true);
        } catch (Exception e) {
            try {
                swingProgressIntField = EntityLivingBase.class.getDeclaredField("swingProgressInt");
                swingProgressIntField.setAccessible(true);
            } catch (Exception ignored) {}
        }

        try {
            isSwingInProgressField = EntityLivingBase.class.getDeclaredField("field_82175_bq");
            isSwingInProgressField.setAccessible(true);
        } catch (Exception e) {
            try {
                isSwingInProgressField = EntityLivingBase.class.getDeclaredField("isSwingInProgress");
                isSwingInProgressField.setAccessible(true);
            } catch (Exception ignored) {}
        }
    }

    public static HandView getInstance() {
        return instance;
    }

    public float getXOffset() { return (float) xOffset.getValue(); }
    public float getYOffset() { return (float) yOffset.getValue(); }
    public float getZOffset() { return (float) zOffset.getValue(); }
    public float getTilt()    { return (float) tilt.getValue(); }
    public int getSwingSpeed() { return swingSpeed.getIntValue(); }
    public boolean isNoSwing() { return noSwing.isEnabled(); }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null) return;

        // No Swing: cancel the swing animation entirely
        if (noSwing.isEnabled()) {
            mc.field_71439_g.field_70733_aJ = 0f;
            mc.field_71439_g.field_70732_aI = 0f;
            if (isSwingInProgressField != null) {
                try {
                    isSwingInProgressField.setBoolean(mc.field_71439_g, false);
                } catch (Exception ignored) {}
            }
            if (swingProgressIntField != null) {
                try {
                    swingProgressIntField.setInt(mc.field_71439_g, 0);
                } catch (Exception ignored) {}
            }
            return;
        }

        // Swing slowdown
        if (swingProgressIntField == null) return;
        int customDuration = getSwingSpeed();
        if (customDuration <= 6) return; // Vanilla speed, no override needed

        try {
            int currentSwing = swingProgressIntField.getInt(mc.field_71439_g);
            if (mc.field_71439_g.field_82175_bq && currentSwing >= 0) {
                float vanillaProgress = (float) currentSwing / 6f;
                int scaledSwing = (int) (vanillaProgress * customDuration);
                
                if (vanillaProgress < 1.0f) {
                    mc.field_71439_g.field_70733_aJ = (float) scaledSwing / (float) customDuration;
                    mc.field_71439_g.field_70732_aI = Math.max(0, mc.field_71439_g.field_70733_aJ - (1.0f / customDuration));
                }
            }
        } catch (Exception ignored) {}
    }
}
