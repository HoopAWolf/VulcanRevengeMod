package com.hoopawolf.dmm.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
                    }
                }
            }
        }

        return false;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        return new TranslationTextComponent(this.getTranslationKey(stack) + getType(stack));
    }
}
