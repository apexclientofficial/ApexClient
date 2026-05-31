package com.apex.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isValidTarget(Entity entity, String targetMode, boolean antiTeam) {
        if (entity == null || entity == mc.thePlayer) return false;
        if (entity.isDead || !entity.isEntityAlive()) return false;
        
        boolean isPlayer = entity instanceof EntityPlayer;
        boolean isAnimal = entity instanceof net.minecraft.entity.passive.EntityAnimal || entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof net.minecraft.entity.passive.EntitySquid || entity instanceof net.minecraft.entity.passive.EntityBat;
        boolean isMob = entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.boss.IBossDisplayData;

        switch (targetMode.toLowerCase()) {
            case "players":
                if (!isPlayer) return false;
                break;
            case "mobs":
                if (!isMob) return false;
                break;
            case "animals":
                if (!isAnimal) return false;
                break;
            case "all":
            default:
                if (!isPlayer && !isAnimal && !isMob) return false;
                break;
        }

        if (antiTeam && isPlayer) {
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
