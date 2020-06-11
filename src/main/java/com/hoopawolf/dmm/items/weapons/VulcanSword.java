package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VulcanSword extends SwordItem
{
    private int type;

    public VulcanSword(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, new Item.Properties().group(VRMItemGroup.instance));

        type = 1;

        this.addPropertyOverride(new ResourceLocation("type"), (p_210310_0_, p_210310_1_, p_210310_2_) -> getType());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isRemote)
        {
            ++type;

            if (getType() > 4)
                type = 1;

            return ActionResult.resultPass(playerIn.getHeldItem(handIn));
        }

        return ActionResult.resultFail(playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.hitEntity(stack, target, attacker);

        switch (getType())
        {
            case 1:
                target.setFire(10);
                break;
            case 2:
                target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 140, 10));
                stack.damageItem(2, target, (p_220009_1_) -> p_220009_1_.sendBreakAnimation(target.getActiveHand()));
                break;

            case 3:
                target.addPotionEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 1, 40));
                stack.damageItem(5, target, (p_220009_1_) -> p_220009_1_.sendBreakAnimation(target.getActiveHand()));
                break;

            case 4:
                target.addPotionEffect(new EffectInstance(Effects.WITHER, 140, 10));
                stack.damageItem(2, target, (p_220009_1_) -> p_220009_1_.sendBreakAnimation(target.getActiveHand()));
                break;
        }

        return true;
    }

    private int getType()
    {
        return type;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (isSelected && getType() == 3)
            {
                if (entityIn instanceof LivingEntity)
                {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 1, 5));
                }
            }
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        return new TranslationTextComponent(this.getTranslationKey(stack) + getType());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.mwaw:vulcansrevenge") + getType()).setStyle(new Style().setItalic(true).setColor(TextFormatting.LIGHT_PURPLE)));
    }
}
