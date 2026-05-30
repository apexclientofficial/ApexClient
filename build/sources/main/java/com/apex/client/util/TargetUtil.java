package com.apex.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isValidTarget(Entity entity, boolean playersOnly, boolean antiTeam) {
        if (entity == null || entity == mc.thePlayer) return false;
        if (entity.isDead || !entity.isEntityAlive()) return false;
        
        if (playersOnly && !(entity instanceof EntityPlayer)) return false;

        if (antiTeam && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (isOnSameTeam(player)) return false;
        }

        return true;
    }

    private static boolean isOnSameTeam(EntityPlayer other) {
        if (mc.thePlayer.getTeam() != null && other.getTeam() != null) {
            return mc.thePlayer.getTeam().isSameTeam(other.getTeam());
        }
        
        // Hypixel/Server generic team color check
        String myName = mc.thePlayer.getDisplayName().getFormattedText();
        String otherName = other.getDisplayName().getFormattedText();
        if (myName.length() > 2 && otherName.length() > 2) {
            if (myName.charAt(0) == '\u00a7' && otherName.charAt(0) == '\u00a7') {
                return myName.charAt(1) == otherName.charAt(1);
            }
        }
        
        return false;
    }
}
