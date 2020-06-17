package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.helper.EntityHelper;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class WarSwordItem extends SwordItem
{
    public WarSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));
    }

    public static int getWarCryCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("warcry", 0);

        return stack.getTag().getInt("warcry");
    }

    public static int getRageCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("rage", 0);

        return stack.getTag().getInt("rage");
    }

    public static void setWarCryCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("warcry", amount);
    }

    public static void setRageCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("rage", amount);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isRemote)
        {
            if (!playerIn.isCrouching())
            {
                if (getWarCryCoolDown(playerIn.getHeldItem(handIn)) <= 0)
                {
                    setWarCryCoolDown(playerIn.getHeldItem(handIn), 200);
                    playerIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 150, 3));
                    playerIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 150, 3));
                    if (playerIn.getHealth() < playerIn.getMaxHealth() * 0.3F)
                    {
                        playerIn.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 150, 3));
                    }

                    playerIn.playSound(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            } else
            {
                if (getRageCoolDown(playerIn.getHeldItem(handIn)) <= 0)
                {
                    setRageCoolDown(playerIn.getHeldItem(handIn), 200);
                    MobEntity temp = null;
                    for (LivingEntity entity : EntityHelper.INSTANCE.getEntityLivingBaseNearby(playerIn, 10, 2, 10, 15))
                    {
                        if (entity instanceof MobEntity)
                        {
                            if (temp == null)
                            {
                                temp = (MobEntity) entity;
                            } else
                            {
                                ((MobEntity) entity).setAttackTarget(temp);
                                temp = null;
                            }
                        }
                    }

                    playerIn.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.hitEntity(stack, target, attacker);

        if (attacker.world.rand.nextInt(100) < 30 || attacker.getHealth() < attacker.getMaxHealth() * 0.3F)
        {
            target.setFire(10);
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
                if (getRageCoolDown(stack) > 0)
                {
                    setRageCoolDown(stack, getRageCoolDown(stack) - 1);
                }

                if (getWarCryCoolDown(stack) > 0)
                {
                    setWarCryCoolDown(stack, getWarCryCoolDown(stack) - 1);
                }
            }

            if (entityIn instanceof LivingEntity && isSelected)
            {
                if (((LivingEntity) entityIn).getHealth() < ((LivingEntity) entityIn).getMaxHealth() * 0.3F)
                {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.STRENGTH, 1, 3));
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1, 3));
                }

                if (entityIn.getFireTimer() > 0)
                {
                    entityIn.extinguish();
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
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.mwaw:war1")).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.mwaw:war2") + ((getWarCryCoolDown(stack) > 0) ? " [" + (getWarCryCoolDown(stack) / 20) + "s]" : "")).setStyle(new Style().setItalic(true).setColor(((getWarCryCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.mwaw:war3") + ((getRageCoolDown(stack) > 0) ? " [" + (getRageCoolDown(stack) / 20) + "s]" : "")).setStyle(new Style().setItalic(true).setColor(((getRageCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.mwaw:war4")).setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY)));
    }
}
