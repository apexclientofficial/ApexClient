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

    public static boolean isOnSameTeam(EntityPlayer target) {
        if (mc.thePlayer == null || target == null) return false;

        if (mc.thePlayer.getTeam() != null && target.getTeam() != null) {
            if (mc.thePlayer.getTeam().isSameTeam(target.getTeam())) {
                return true;
            }
        }
        
        // Backup color check
        String myName = mc.thePlayer.getDisplayName().getFormattedText();
        String targetName = target.getDisplayName().getFormattedText();
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
        if (mc.thePlayer.inventory != null && target.inventory != null) {
            for (net.minecraft.item.ItemStack myArmor : mc.thePlayer.inventory.armorInventory) {
                if (myArmor != null && myArmor.getItem() instanceof net.minecraft.item.ItemArmor) {
                    net.minecraft.item.ItemArmor myItemArmor = (net.minecraft.item.ItemArmor) myArmor.getItem();
                    if (myItemArmor.getArmorMaterial() == net.minecraft.item.ItemArmor.ArmorMaterial.LEATHER) {
                        int myColor = myItemArmor.getColor(myArmor);
                        if (myColor != -1 && myColor != 10511680) { // 10511680 is default un-dyed leather
                            for (net.minecraft.item.ItemStack targetArmor : target.inventory.armorInventory) {
                                if (targetArmor != null && targetArmor.getItem() instanceof net.minecraft.item.ItemArmor) {
                                    net.minecraft.item.ItemArmor targetItemArmor = (net.minecraft.item.ItemArmor) targetArmor.getItem();
                                    if (targetItemArmor.getArmorMaterial() == net.minecraft.item.ItemArmor.ArmorMaterial.LEATHER) {
                                        if (targetItemArmor.getColor(targetArmor) == myColor) {
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
