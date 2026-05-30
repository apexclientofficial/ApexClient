package com.apex.client.module.movement;

import com.apex.client.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {

    public InventoryMove() {
        super("InventoryMove", "Move while in GUIs", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.field_71462_r != null && !(mc.field_71462_r instanceof GuiChat)) {
            KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74351_w.func_151463_i()));
            KeyBinding.func_74510_a(mc.field_71474_y.field_74368_y.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74368_y.func_151463_i()));
            KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74370_x.func_151463_i()));
            KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74366_z.func_151463_i()));
            KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74314_A.func_151463_i()));
        }
    }
}
