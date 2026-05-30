package com.apex.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean isValidTarget(Entity entity, boolean playersOnly, boolean antiTeam) {
        if (entity == null || entity == mc.field_71439_g) return false;
        if (entity.field_70128_L || !entity.func_70089_S()) return false;
        
        if (playersOnly && !(entity instanceof EntityPlayer)) return false;

        if (antiTeam && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (isOnSameTeam(player)) return false;
        }

        return true;
    }

    private static boolean isOnSameTeam(EntityPlayer other) {
        if (mc.field_71439_g.func_96124_cp() != null && other.func_96124_cp() != null) {
            return mc.field_71439_g.func_96124_cp().func_142054_a(other.func_96124_cp());
        }
        
        // Hypixel/Server generic team color check
        String myName = mc.field_71439_g.func_145748_c_().func_150254_d();
        String otherName = other.func_145748_c_().func_150254_d();
        if (myName.length() > 2 && otherName.length() > 2) {
            if (myName.charAt(0) == '\u00a7' && otherName.charAt(0) == '\u00a7') {
                return myName.charAt(1) == otherName.charAt(1);
            }
        }
        
        return false;
    }
}
