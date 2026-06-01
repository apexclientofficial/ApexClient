package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.ModeSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.lang.reflect.Field;

public class Velocity extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Standard", "Standard", "Hypixel Reduce", "Reverse");
    private final NumberSetting horizontal = new NumberSetting("Horizontal", 0.0, 0.0, 100.0, 1.0);
    private final NumberSetting vertical = new NumberSetting("Vertical", 0.0, 0.0, 100.0, 1.0);

    // Cached reflection fields for S12PacketEntityVelocity
    private Field motionXField, motionYField, motionZField;
    private boolean reflectionFailed = false;

    public Velocity() {
        super("Velocity", "Reduces knockback from incoming hits", Category.COMBAT);
        addSetting(mode);
        addSetting(horizontal);
        addSetting(vertical);
        initReflection();
    }

    private void initReflection() {
        try {
            // Try all known field names (obfuscated & deobfuscated)
            motionXField = findField(S12PacketEntityVelocity.class, "motionX", "field_149415_b");
            motionYField = findField(S12PacketEntityVelocity.class, "motionY", "field_149416_c");
            motionZField = findField(S12PacketEntityVelocity.class, "motionZ", "field_149414_d");
        } catch (Exception e) {
            reflectionFailed = true;
            e.printStackTrace();
        }
    }

    private Field findField(Class<?> clazz, String... names) throws NoSuchFieldException {
        for (String name : names) {
            try {
                Field f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException ignored) {}
        }
        throw new NoSuchFieldException("Could not find any of the target fields in " + clazz.getName());
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPacketReceive(FMLNetworkEvent.ClientCustomPacketEvent event) {
        // This event won't fire for vanilla packets; we use onTick as a fallback
    }

    /**
     * We use onTick to intercept and modify velocity the frame it arrives.
     * The server sends S12PacketEntityVelocity which directly sets
     * mc.thePlayer.motionX/Y/Z on the client. By the time the next client
     * tick runs, those fields already hold the knockback values. We multiply
     * them down before the movement code consumes them.
     */
    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;

        // Only act on the exact frame the player receives a hit
        if (mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && mc.thePlayer.maxHurtTime > 0) {
            switch (mode.getValue()) {
                case "Standard": {
                    double hMult = horizontal.getValue() / 100.0;
                    double vMult = vertical.getValue() / 100.0;
                    mc.thePlayer.motionX *= hMult;
                    mc.thePlayer.motionY *= vMult;
                    mc.thePlayer.motionZ *= hMult;
                    break;
                }
                case "Hypixel Reduce": {
                    // 60% horizontal to bypass Watchdog, 100% vertical to avoid jump-height flags
                    mc.thePlayer.motionX *= 0.6;
                    mc.thePlayer.motionZ *= 0.6;
                    // motionY untouched (1.0)
                    break;
                }
                case "Reverse": {
                    // Invert horizontal to push forward into the attacker
                    mc.thePlayer.motionX *= -0.5;
                    mc.thePlayer.motionZ *= -0.5;
                    break;
                }
            }
        }
    }

    /**
     * Called externally when a raw S12PacketEntityVelocity is intercepted
     * by the packet handler hook. This modifies the packet fields directly
     * via reflection BEFORE the client applies them to the player entity.
     * This is the most reliable anti-KB technique.
     *
     * @return true if the packet should be cancelled entirely, false otherwise
     */
    public boolean handleVelocityPacket(S12PacketEntityVelocity packet) {
        if (!isEnabled()) return false;
        if (mc.thePlayer == null) return false;
        if (packet.getEntityID() != mc.thePlayer.getEntityId()) return false;
        if (reflectionFailed) return false;

        try {
            int origX = motionXField.getInt(packet);
            int origY = motionYField.getInt(packet);
            int origZ = motionZField.getInt(packet);

            switch (mode.getValue()) {
                case "Standard": {
                    double hMult = horizontal.getValue() / 100.0;
                    double vMult = vertical.getValue() / 100.0;
                    motionXField.setInt(packet, (int)(origX * hMult));
                    motionYField.setInt(packet, (int)(origY * vMult));
                    motionZField.setInt(packet, (int)(origZ * hMult));
                    break;
                }
                case "Hypixel Reduce": {
                    motionXField.setInt(packet, (int)(origX * 0.6));
                    motionZField.setInt(packet, (int)(origZ * 0.6));
                    // Y stays at origY (100%)
                    break;
                }
                case "Reverse": {
                    motionXField.setInt(packet, (int)(origX * -0.5));
                    motionZField.setInt(packet, (int)(origZ * -0.5));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
