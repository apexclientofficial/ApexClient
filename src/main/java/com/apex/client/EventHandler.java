package com.apex.client;

import com.apex.client.gui.ApexGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

public class EventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean packetHookInstalled = false;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == 0) return; // Ignore unmapped/media keys

            if (key == Keyboard.KEY_RSHIFT) {
                mc.displayGuiScreen(new ApexGUI());
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
            if (mc.thePlayer != null && mc.theWorld != null) {
                // Install packet hook on the player's network channel (once)
                if (!packetHookInstalled) {
                    try {
                        ChannelPipeline pipeline = mc.thePlayer.sendQueue.getNetworkManager().channel().pipeline();
                        if (pipeline.get("apex_packet_handler") == null) {
                            pipeline.addBefore("packet_handler", "apex_packet_handler", new ChannelDuplexHandler() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    if (msg instanceof S12PacketEntityVelocity) {
                                        Module velModule = ApexClient.instance.getModuleManager().getModuleByName("Velocity");
                                        if (velModule instanceof com.apex.client.module.combat.Velocity) {
                                            boolean cancel = ((com.apex.client.module.combat.Velocity) velModule)
                                                    .handleVelocityPacket((S12PacketEntityVelocity) msg);
                                            if (cancel) return; // Drop packet entirely
                                        }
                                    }
                                    super.channelRead(ctx, msg);
                                }
                                
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    if (msg instanceof Packet) {
                                        Module spoofMod = ApexClient.instance.getModuleManager().getModuleByName("PacketSpoof");
                                        if (spoofMod instanceof com.apex.client.module.misc.PacketSpoof) {
                                            boolean cancel = ((com.apex.client.module.misc.PacketSpoof) spoofMod)
                                                    .onPacketSend((Packet<?>) msg);
                                            if (cancel) return; // Cancel original send
                                        }
                                    }
                                    super.write(ctx, msg, promise);
                                }
                            });
                        }
                        packetHookInstalled = true;
                    } catch (Exception e) {
                        // Channel not ready yet, try again next tick
                    }
                }

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
