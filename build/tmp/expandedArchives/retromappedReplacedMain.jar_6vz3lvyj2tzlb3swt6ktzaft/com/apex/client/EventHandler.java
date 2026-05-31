package com.apex.client;

import com.apex.client.gui.ApexGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class EventHandler {

    private final Minecraft mc = Minecraft.func_71410_x();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == 0) return; // Ignore unmapped/media keys

            if (key == Keyboard.KEY_RSHIFT) {
                mc.func_147108_a(new ApexGUI());
            }

            for (Module m : ApexClient.instance.getModuleManager().getModules()) {
                if (m.getKey() == key) {
                    m.toggle();
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (mc.field_71439_g != null && mc.field_71441_e != null) {
                for (Module m : ApexClient.instance.getModuleManager().getModules()) {
                    if (m.isEnabled()) {
                        m.onTick();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            com.apex.client.gui.hud.HUDRenderer.renderHUD();
        }
    }
}
