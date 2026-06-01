package com.apex.client.module.misc;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.MathHelper;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PacketSpoof extends Module {

    private final BooleanSetting pingSpoof = new BooleanSetting("Ping Spoof", true);
    private final NumberSetting pingDelay = new NumberSetting("Ping Delay", 250, 50, 1000, 10);

    private final BooleanSetting groundSpoof = new BooleanSetting("Ground Spoof", true);
    
    private final BooleanSetting silentRots = new BooleanSetting("Silent Rotations", true);

    private final ConcurrentLinkedQueue<DelayedPacket> packetBuffer = new ConcurrentLinkedQueue<>();
    private Field onGroundField;
    private Field yawField;
    private Field pitchField;

    public PacketSpoof() {
        super("PacketSpoof", "Disrupts server-side anticheat tracking", Category.MISC);
        addSetting(pingSpoof);
        addSetting(pingDelay);
        addSetting(groundSpoof);
        addSetting(silentRots);

        try {
            onGroundField = C03PacketPlayer.class.getDeclaredField("field_149474_g"); // onGround
            onGroundField.setAccessible(true);
        } catch (Exception e) {
            try {
                onGroundField = C03PacketPlayer.class.getDeclaredField("onGround");
                onGroundField.setAccessible(true);
            } catch (Exception ignored) {}
        }
        
        try {
            yawField = C03PacketPlayer.class.getDeclaredField("field_149476_e"); // yaw
            yawField.setAccessible(true);
            pitchField = C03PacketPlayer.class.getDeclaredField("field_149473_f"); // pitch
            pitchField.setAccessible(true);
        } catch (Exception e) {
            try {
                yawField = C03PacketPlayer.class.getDeclaredField("yaw");
                yawField.setAccessible(true);
                pitchField = C03PacketPlayer.class.getDeclaredField("pitch");
                pitchField.setAccessible(true);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onDisable() {
        // Flush all delayed packets immediately
        DelayedPacket dp;
        while ((dp = packetBuffer.poll()) != null) {
            if (mc.func_147114_u() != null && mc.func_147114_u().func_147298_b() != null) {
                mc.func_147114_u().func_147298_b().func_179290_a(dp.packet);
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.field_71439_g == null || mc.field_71441_e == null) {
            packetBuffer.clear();
            return;
        }

        // Release delayed packets
        long now = System.currentTimeMillis();
        while (!packetBuffer.isEmpty()) {
            DelayedPacket dp = packetBuffer.peek();
            if (dp != null && now >= dp.releaseTime) {
                packetBuffer.poll();
                if (mc.func_147114_u() != null && mc.func_147114_u().func_147298_b() != null) {
                    mc.func_147114_u().func_147298_b().func_179290_a(dp.packet);
                }
            } else {
                break;
            }
        }
    }

    /**
     * Called by EventHandler's pipeline injection for OUTBOUND packets.
     * @return true to cancel the original transmission
     */
    public boolean onPacketSend(Packet<?> packet) {
        if (!isEnabled()) return false;

        // Method A: Ping Delay
        if (pingSpoof.isEnabled()) {
            if (packet instanceof C00PacketKeepAlive || packet instanceof C0FPacketConfirmTransaction) {
                packetBuffer.add(new DelayedPacket(packet, System.currentTimeMillis() + (long) pingDelay.getValue()));
                return true; // Cancel original send
            }
        }

        // Method B: Ground State Identity Spoofing (NoFall)
        if (groundSpoof.isEnabled() && packet instanceof C03PacketPlayer) {
            if (mc.field_71439_g != null && mc.field_71439_g.field_70143_R > 1.5f && onGroundField != null) {
                try {
                    onGroundField.setBoolean(packet, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Method C: Silent Rotation Overrides
        if (silentRots.isEnabled() && packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer) packet;
            if (c03.func_149463_k() && yawField != null && pitchField != null) {
                // If a combat module is actively attacking...
                // We'll check if KillAura is enabled and has a target, or just aim at the entity we are attacking
                com.apex.client.Module killaura = com.apex.client.ApexClient.instance.getModuleManager().getModuleByName("KillAura");
                if (killaura != null && killaura.isEnabled() && ((com.apex.client.module.combat.KillAura)killaura).getTarget() != null) {
                    net.minecraft.entity.EntityLivingBase target = ((com.apex.client.module.combat.KillAura)killaura).getTarget();
                    
                    double diffX = target.field_70165_t - mc.field_71439_g.field_70165_t;
                    double diffZ = target.field_70161_v - mc.field_71439_g.field_70161_v;
                    double diffY = (target.field_70163_u + target.func_70047_e()) - (mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e());
                    double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
                    
                    float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
                    float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

                    try {
                        yawField.setFloat(packet, yaw);
                        pitchField.setFloat(packet, pitch);
                    } catch (Exception e) {}
                }
            }
        }

        return false;
    }

    private static class DelayedPacket {
        Packet<?> packet;
        long releaseTime;

        DelayedPacket(Packet<?> packet, long releaseTime) {
            this.packet = packet;
            this.releaseTime = releaseTime;
        }
    }
}
