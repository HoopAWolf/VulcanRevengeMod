package com.hoopawolf.dmm.items;

import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.util.ItemBlockRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RuneItem extends Item
{
    public RuneItem(Properties properties)
    {
        super(properties);

        this.addPropertyOverride(new ResourceLocation("type"), (p_210310_0_, p_210310_1_, p_210310_2_) -> getType(p_210310_0_));
    }

    public static int getType(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("type", 0);

        return stack.getTag().getInt("type");
    }

    private static void setType(ItemStack stack, int type)
    {
        stack.getOrCreateTag().putInt("type", type);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        if (!playerIn.world.isRemote)
        {
            if (getType(stack) == 0)
            {
                if (target instanceof AbstractHorseEntity)
                {
                    if (target.isPotionActive(Effects.WITHER))
                    {
                        setType(stack, 1);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else if (target.getFireTimer() > 0)
                    {
                        setType(stack, 2);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else if (((AbstractHorseEntity) target).getTemper() == ((AbstractHorseEntity) target).getMaxTemper())
                    {
                        setType(stack, 3);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else if (target.isPotionActive(Effects.POISON))
                    {
                        setType(stack, 4);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isRemote)
        {
            switch (getType(playerIn.getHeldItem(handIn)))
            {
                case 1:
                {
                    if (playerIn.getHeldItemOffhand().getItem().equals(Items.DIAMOND_HOE))
                    {
                        playerIn.getHeldItemOffhand().shrink(1);
                        playerIn.getHeldItem(handIn).shrink(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.DEATH_SWORD.get()), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = i * 360 / 180;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getPosX(), playerIn.getPosY() + 0.5F, playerIn.getPosZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 4, 0.0F);
                            VRMPacketHandler.packetHandler.sendToDimension(playerIn.dimension, spawnParticleMessage);
                        }
                    }
                }
                break;

                case 2:
                {
                    if (playerIn.getHeldItemOffhand().getItem().equals(Items.DIAMOND_SWORD))
                    {
                        playerIn.getHeldItemOffhand().shrink(1);
                        playerIn.getHeldItem(handIn).shrink(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.WAR_SWORD.get()), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = i * 360 / 180;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getPosX(), playerIn.getPosY() + 0.5F, playerIn.getPosZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 0, 0.0F);
                            VRMPacketHandler.packetHandler.sendToDimension(playerIn.dimension, spawnParticleMessage);
                        }
                    }
                }
                break;
                case 3:
                {
                    if (playerIn.getHeldItemOffhand().getItem().equals(Items.DIAMOND_PICKAXE))
                    {
                        playerIn.getHeldItemOffhand().shrink(1);
                        playerIn.getHeldItem(handIn).shrink(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.FAM_SCALE.get()), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = i * 360 / 180;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getPosX(), playerIn.getPosY() + 0.5F, playerIn.getPosZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 6, 0.0F);
                            VRMPacketHandler.packetHandler.sendToDimension(playerIn.dimension, spawnParticleMessage);
                        }
                    }
                }
                break;
                case 4:
                {
                    if (playerIn.getHeldItemOffhand().getItem().equals(Items.BOW))
                    {
                        playerIn.getHeldItemOffhand().shrink(1);
                        playerIn.getHeldItem(handIn).shrink(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.PES_BOW.get()), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = i * 360 / 180;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getPosX(), playerIn.getPosY() + 0.5F, playerIn.getPosZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 5, 0.0F);
                            VRMPacketHandler.packetHandler.sendToDimension(playerIn.dimension, spawnParticleMessage);
                        }
                    }
                }
                break;
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        return new TranslationTextComponent(this.getTranslationKey(stack) + getType(stack));
    }
}
