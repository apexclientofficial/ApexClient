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

    public static boolean isOnSameTeam(EntityPlayer target) {
        if (mc.field_71439_g == null || target == null) return false;

        if (mc.field_71439_g.func_96124_cp() != null && target.func_96124_cp() != null) {
            if (mc.field_71439_g.func_96124_cp().func_142054_a(target.func_96124_cp())) {
                return true;
            }
        }
        
        // Backup color check
        String myName = mc.field_71439_g.func_145748_c_().func_150254_d();
        String targetName = target.func_145748_c_().func_150254_d();
        if (myName.length() >= 2 && targetName.length() >= 2) {
            if (myName.startsWith("\u00a7") && targetName.startsWith("\u00a7")) {
                char myColor = myName.charAt(1);
                if (myColor == targetName.charAt(1)) {
                    if (myColor != 'f' && myColor != 'r' && myColor != '7' && myColor != '8') {
                        return true;
                    }
                }
            }
        }

        // Leather Armor Dye check (for Bedwars/Skywars etc.)
        if (mc.field_71439_g.field_71071_by != null && target.field_71071_by != null) {
            for (net.minecraft.item.ItemStack myArmor : mc.field_71439_g.field_71071_by.field_70460_b) {
                if (myArmor != null && myArmor.func_77973_b() instanceof net.minecraft.item.ItemArmor) {
                    net.minecraft.item.ItemArmor myItemArmor = (net.minecraft.item.ItemArmor) myArmor.func_77973_b();
                    if (myItemArmor.func_82812_d() == net.minecraft.item.ItemArmor.ArmorMaterial.LEATHER) {
                        int myColor = myItemArmor.func_82814_b(myArmor);
                        if (myColor != -1 && myColor != 10511680) { // 10511680 is default un-dyed leather
                            for (net.minecraft.item.ItemStack targetArmor : target.field_71071_by.field_70460_b) {
                                if (targetArmor != null && targetArmor.func_77973_b() instanceof net.minecraft.item.ItemArmor) {
                                    net.minecraft.item.ItemArmor targetItemArmor = (net.minecraft.item.ItemArmor) targetArmor.func_77973_b();
                                    if (targetItemArmor.func_82812_d() == net.minecraft.item.ItemArmor.ArmorMaterial.LEATHER) {
                                        if (targetItemArmor.func_82814_b(targetArmor) == myColor) {
                                            return true; // Match found
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
}
