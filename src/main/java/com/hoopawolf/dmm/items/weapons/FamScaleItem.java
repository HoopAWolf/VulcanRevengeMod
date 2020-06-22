package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.helper.EntityHelper;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.block.BushBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FamScaleItem extends Item
{
    private final int range = 10;

    public FamScaleItem(Properties properties)
    {
        super(properties.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isRemote)
        {
            if (!playerIn.isCrouching() && handIn.equals(Hand.MAIN_HAND))
            {
                for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(playerIn, 5, 2, 5, 10))
                {
                    if (entity instanceof AnimalEntity)
                    {
                        if (!entity.isChild() && !((AnimalEntity) entity).isInLove())
                        {
                            ((AnimalEntity) entity).setInLove(600);
                            ((AnimalEntity) entity).setGrowingAge(0);
                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(entity.getPosX(), entity.getPosY() + 0.5F, entity.getPosZ()), new Vec3d(0.0F, 0.0D, 0.0F), 3, 7, entity.getWidth());
                            VRMPacketHandler.packetHandler.sendToDimension(playerIn.dimension, spawnParticleMessage);
                        }
                    }
                }
                playerIn.playSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.BLOCKS, 5.0F, 0.1F);
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn.ticksExisted % 10 == 0)
            {
                if (entityIn instanceof PlayerEntity)
                {
                    if (((PlayerEntity) entityIn).getFoodStats().needFood())
                    {
                        if (worldIn.rand.nextInt(100) < 50)
                        {
                            boolean _flag = false;
                            for (int x = -range; x < range; ++x)
                            {
                                for (int z = -range; z < range; ++z)
                                {
                                    BlockPos pos = new BlockPos(entityIn.getPosX() + x, entityIn.getPosY(), entityIn.getPosZ() + z);
                                    if (worldIn.getBlockState(pos).getBlock() instanceof BushBlock)
                                    {
                                        worldIn.destroyBlock(pos, false);
                                        increaseFood((PlayerEntity) entityIn);
                                        _flag = true;
                                        break;
                                    }
                                }

                                if (_flag)
                                {
                                    break;
                                }
                            }
                        } else
                        {
                            if (isSelected)
                            {
                                for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityIn, range, 1, range, 15))
                                {
                                    entity.attackEntityFrom(DamageSource.STARVE, 2);
                                    increaseFood((PlayerEntity) entityIn);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void increaseFood(PlayerEntity entityIn)
    {
        entityIn.getFoodStats().setFoodLevel(entityIn.getFoodStats().getFoodLevel() + 1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:fam1")).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:fam2")).setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY)));
//        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:death3") + ((getVoodooCoolDown(stack) > 0) ? " [" + (getVoodooCoolDown(stack) / 20) + "s]" : "")).setStyle(new Style().setItalic(true).setColor(((getVoodooCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
//        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:death4") + ((getDeathCoolDown(stack) > 0) ? " [" + (getDeathCoolDown(stack) / 20) + "s]" : "")).setStyle(new Style().setItalic(true).setColor(((getDeathCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
    }
}
