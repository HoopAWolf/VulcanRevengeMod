package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.helper.EntityHelper;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
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

public class DeathSwordItem extends SwordItem
{
    public DeathSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));
    }

    public static int getVoodooCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("voodoo", 0);

        return stack.getTag().getInt("voodoo");
    }

    public static int getDeathCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("death", 0);

        return stack.getTag().getInt("death");
    }

    public static int getMarkCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("mark", 0);

        return stack.getTag().getInt("mark");
    }

    public static int getVoodooID(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("voodooid", 0);

        return stack.getTag().getInt("voodooid");
    }

    public static void setVoodooCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("voodoo", amount);
    }

    public static void setDeathCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("death", amount);
    }

    public static void setMarkCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("mark", amount);
    }

    public static void setVoodooID(ItemStack stack, int id)
    {
        stack.getOrCreateTag().putInt("voodooid", id);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isRemote)
        {
            if (!playerIn.isCrouching() && handIn.equals(Hand.MAIN_HAND))
            {
                if (getMarkCoolDown(playerIn.getHeldItem(handIn)) <= 0)
                {
                    setMarkCoolDown(playerIn.getHeldItem(handIn), 200);
                    for (LivingEntity entity : EntityHelper.INSTANCE.getEntityLivingBaseNearby(playerIn, 5, 2, 5, 10))
                    {
                        entity.addPotionEffect(new EffectInstance(Effects.GLOWING, 180, 1));
                    }
                    playerIn.playSound(SoundEvents.ENTITY_SQUID_SQUIRT, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        if (!playerIn.world.isRemote)
        {
            if (playerIn.isCrouching() && hand.equals(Hand.MAIN_HAND))
            {
                if (getVoodooCoolDown(playerIn.getHeldItem(hand)) <= 0)
                {
                    setVoodooCoolDown(playerIn.getHeldItem(hand), 200);
                    setVoodooID(playerIn.getHeldItem(hand), target.getEntityId());
                    playerIn.playSound(SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    return true;
                } else
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            }
        }

        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.hitEntity(stack, target, attacker);

        if (attacker.world.rand.nextInt(100) < 30 || target.isPotionActive(Effects.GLOWING))
        {
            attacker.heal(this.getAttackDamage() * 0.5F);
        }

        if (target.isPotionActive(Effects.GLOWING))
        {
            target.attackEntityFrom(new DamageSource("reaper"), this.getAttackDamage() * 2.0F);
        }

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn.ticksExisted % 2 == 0)
            {
                if (getDeathCoolDown(stack) > 0)
                {
                    setDeathCoolDown(stack, getDeathCoolDown(stack) - 1);
                }

                if (getMarkCoolDown(stack) > 0)
                {
                    setMarkCoolDown(stack, getMarkCoolDown(stack) - 1);
                }

                if (getVoodooCoolDown(stack) > 0)
                {
                    setVoodooCoolDown(stack, getVoodooCoolDown(stack) - 1);
                } else if (getVoodooCoolDown(stack) <= 0 && getVoodooID(stack) != 0)
                {
                    setVoodooID(stack, 0);
                }

                if (worldIn.getEntityByID(getVoodooID(stack)) == null || !worldIn.getEntityByID(getVoodooID(stack)).isAlive())
                {
                    setVoodooID(stack, 0);
                }
            }
        } else
        {
            if (getVoodooID(stack) != 0)
            {
                Entity entity = worldIn.getEntityByID(getVoodooID(stack));

                if (entity != null)
                {
                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(entity.getPosX(), entity.getPosY() + entity.getEyeHeight() + 0.5D, entity.getPosZ()), new Vec3d(0.0D, 0.0D, 0.0D), 1, 3, 0.0F);
                    VRMPacketHandler.packetHandler.sendToDimension(entity.dimension, spawnParticleMessage);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:death1")).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:death2") + ((getMarkCoolDown(stack) > 0) ? " [" + (getMarkCoolDown(stack) / 20) + "s]" : "")).setStyle(new Style().setItalic(true).setColor(((getMarkCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:death3") + ((getVoodooCoolDown(stack) > 0) ? " [" + (getVoodooCoolDown(stack) / 20) + "s]" : "")).setStyle(new Style().setItalic(true).setColor(((getVoodooCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:death4") + ((getDeathCoolDown(stack) > 0) ? " [" + (getDeathCoolDown(stack) / 20) + "s]" : "")).setStyle(new Style().setItalic(true).setColor(((getDeathCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
    }
}