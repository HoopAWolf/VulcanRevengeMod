package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.items.weapons.DeathSwordItem;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class VRMEventHandler
{
    @SubscribeEvent
    public static void DeathEvent(LivingDeathEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof PlayerEntity && event.getSource().getTrueSource() instanceof LivingEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get().getItem()) &&
                        DeathSwordItem.getDeathCoolDown(player.getHeldItemMainhand()) <= 0)
                {
                    event.setCanceled(true);
                    attacker.attackEntityFrom(new DamageSource("death"), attacker.getMaxHealth() * 0.5F);
                    player.setHealth(player.getMaxHealth() * 0.5F);
                    DeathSwordItem.setDeathCoolDown(player.getHeldItemMainhand(), 600);
                    player.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 10, 1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get().getItem()) &&
                        DeathSwordItem.getVoodooID(player.getHeldItemMainhand()) != 0 &&
                        player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())) != null &&
                        player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).isAlive())
                {
                    event.setCanceled(true);
                    player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).attackEntityFrom(new DamageSource("reaper"), event.getAmount());
                    player.playSound(SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }

                if (player.getHealth() < player.getMaxHealth() * 0.3F && player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.WAR_SWORD.get().getItem()) &&
                        event.getSource().getTrueSource() instanceof LivingEntity)
                {
                    event.getSource().getTrueSource().setFire(10);
                    player.playSound(SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            }
        }
    }
}
