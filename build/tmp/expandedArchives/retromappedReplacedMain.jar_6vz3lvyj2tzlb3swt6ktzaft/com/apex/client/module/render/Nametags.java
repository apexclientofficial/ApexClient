package com.apex.client.module.render;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
    private final BooleanSetting health = new BooleanSetting("ShowHealth", true);
    private final BooleanSetting distance = new BooleanSetting("ShowDistance", true);
    private boolean registered = false;

    public Nametags() {
        super("Nametags", "Renders improved nametags on players", Category.RENDER);
        addSetting(health);
        addSetting(distance);
    }

    @Override
    public void onEnable() {
        if (!registered) { MinecraftForge.EVENT_BUS.register(this); registered = true; }
    }

    @Override
    public void onDisable() {
        if (registered) { MinecraftForge.EVENT_BUS.unregister(this); registered = false; }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.field_71439_g == null || mc.field_71441_e == null) return;
        for (Entity entity : mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityPlayer) || entity == mc.field_71439_g) continue;
            EntityPlayer player = (EntityPlayer) entity;

            double x = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * event.partialTicks - mc.func_175598_ae().field_78730_l;
            double y = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * event.partialTicks - mc.func_175598_ae().field_78731_m + player.field_70131_O + 0.5;
            double z = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * event.partialTicks - mc.func_175598_ae().field_78728_n;

            String name = player.func_70005_c_();
            if (health.isEnabled()) name += " \u00a7c" + (int)player.func_110143_aJ() + "\u00a77hp";
            if (distance.isEnabled()) {
                int dist = (int)mc.field_71439_g.func_70032_d(player);
                name += " \u00a77[" + dist + "m]";
            }

            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b(x, y, z);
            GlStateManager.func_179114_b(-mc.func_175598_ae().field_78735_i, 0, 1, 0);
            GlStateManager.func_179114_b(mc.func_175598_ae().field_78732_j, 1, 0, 0);
            GlStateManager.func_179152_a(-0.025f, -0.025f, 0.025f);
            GlStateManager.func_179097_i();
            GlStateManager.func_179147_l();
            GlStateManager.func_179132_a(false);
            int sw = mc.field_71466_p.func_78256_a(name);
            
            // Draw background
            net.minecraft.client.gui.Gui.func_73734_a(-sw / 2 - 2, -2, sw / 2 + 2, 9, 0x55000000);
            
            mc.field_71466_p.func_175063_a(name, -sw / 2f, 0, 0xFFFFFFFF);
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179121_F();
        }
    }

    @SubscribeEvent
    public void onRenderLivingPre(net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (event.entity instanceof EntityPlayer && event.entity != mc.field_71439_g) {
            event.setCanceled(true); // Cancel vanilla nametag rendering
        }
    }
}
