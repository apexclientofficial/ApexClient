package com.apex.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean isValidTarget(Entity entity, String targetMode, boolean antiTeam) {
        if (entity == null || entity == mc.field_71439_g) return false;
        if (entity.field_70128_L || !entity.func_70089_S()) return false;
        
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
