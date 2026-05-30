package com.apex.client.module.combat;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {
    private final BooleanSetting groundCheck = new BooleanSetting("GroundCheck", true);
    private final BooleanSetting nameCheck = new BooleanSetting("NameCheck", true);
    private final BooleanSetting pinpointCheck = new BooleanSetting("PinpointCheck", true);

    public AntiBot() {
        super("AntiBot", "Filters out bots from targeting", Category.COMBAT);
        addSetting(groundCheck);
        addSetting(nameCheck);
        addSetting(pinpointCheck);
    }

    public boolean isBot(Entity entity) {
        if (!(entity instanceof EntityPlayer)) return false;
        EntityPlayer player = (EntityPlayer) entity;

        if (groundCheck.isEnabled() && !player.field_70122_E && player.field_70159_w == 0 && player.field_70179_y == 0) return true;
        if (nameCheck.isEnabled() && player.func_70005_c_().isEmpty()) return true;
        if (pinpointCheck.isEnabled() && player.field_70165_t == Math.floor(player.field_70165_t) && player.field_70161_v == Math.floor(player.field_70161_v)) return true;

        return false;
    }
}
