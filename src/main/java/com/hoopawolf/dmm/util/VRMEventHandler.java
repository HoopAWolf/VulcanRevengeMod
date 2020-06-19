package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.dmm.items.weapons.DeathSwordItem;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
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

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get()) &&
                        DeathSwordItem.getDeathCoolDown(player.getHeldItemMainhand()) <= 0)
                {
                    event.setCanceled(true);
                    attacker.attackEntityFrom(new DamageSource("death"), attacker.getMaxHealth() * 0.5F);
                    player.setHealth(player.getMaxHealth() * 0.5F);
                    DeathSwordItem.setDeathCoolDown(player.getHeldItemMainhand(), 600);
                    player.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = i * 360 / 180;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 4, 0.0F);
                        VRMPacketHandler.packetHandler.sendToDimension(player.dimension, spawnParticleMessage);
                    }
                }
            }

            if (event.getEntity() instanceof LivingEntity)
            {
                LivingEntity target = (LivingEntity) event.getEntity();

                if (target.isPotionActive(PotionRegistryHandler.PLAGUE_EFFECT.get()) || event.getSource().getImmediateSource() instanceof PesArrowEntity)
                {
                    target.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, 0.5F, 0.1F);
                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = i * 360 / 180;
                        double speed = 0.7;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(target.getPosX(), target.getPosY() + 0.5F, target.getPosZ()), new Vec3d(xSpeed, 0.9D, zSpeed), 3, 5, 0.0F);
                        VRMPacketHandler.packetHandler.sendToDimension(target.dimension, spawnParticleMessage);
                    }
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

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get()) &&
                        DeathSwordItem.getVoodooID(player.getHeldItemMainhand()) != 0 &&
                        player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())) != null &&
                        player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).isAlive())
                {
                    event.setCanceled(true);
                    player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).attackEntityFrom(new DamageSource("reaper"), event.getAmount());
                    player.playSound(SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }

                if (player.getHealth() < player.getMaxHealth() * 0.3F && player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.WAR_SWORD.get()) &&
                        event.getSource().getTrueSource() instanceof LivingEntity)
                {
                    event.getSource().getTrueSource().setFire(10);
                    player.playSound(SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void ApplyPotionEvent(PotionEvent.PotionApplicableEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.PES_BOW.get()))
                {
                    if (!event.getPotionEffect().getPotion().isBeneficial())
                    {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }

    }
}
