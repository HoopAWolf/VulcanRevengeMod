package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PesBowItem extends ShootableItem
{
    public PesBowItem(Properties builder)
    {
        super(builder.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));

        this.addPropertyOverride(new ResourceLocation("pull"), (p_210310_0_, p_210310_1_, p_210310_2_) ->
        {
            if (p_210310_2_ == null)
            {
                return 0.0F;
            } else
            {
                return !(p_210310_2_.getActiveItemStack().getItem() instanceof PesBowItem) ? 0.0F : (float) (p_210310_0_.getUseDuration() - p_210310_2_.getItemInUseCount()) / 20.0F;
            }
        });

        this.addPropertyOverride(new ResourceLocation("pulling"), (p_210309_0_, p_210309_1_, p_210309_2_) ->
        {
            return p_210309_2_ != null && p_210309_2_.isHandActive() && p_210309_2_.getActiveItemStack() == p_210309_0_ ? 1.0F : 0.0F;
        });
    }

    public static float getArrowVelocity(int charge)
    {
        float f = (float) charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate()
    {
        return null;
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count)
    {
        if (livingEntityIn.ticksExisted % 50 == 0)
        {
            livingEntityIn.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, 0.5F, 0.1F);
            if (!worldIn.isRemote)
            {
                for (int i = 1; i <= 180; ++i)
                {
                    double yaw = i * 360 / 180;
                    double speed = 0.3;
                    double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                    double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(livingEntityIn.getPosX(), livingEntityIn.getPosY() + 0.5F, livingEntityIn.getPosZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 1, 5, 0.0F);
                    VRMPacketHandler.packetHandler.sendToDimension(livingEntityIn.dimension, spawnParticleMessage);
                }
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, true);
            if (i < 0) return;

            float f = getArrowVelocity(i);
            if (!((double) f < 0.1D))
            {
                if (!worldIn.isRemote)
                {
                    AbstractArrowEntity abstractarrowentity = new PesArrowEntity(worldIn, playerentity);
                    abstractarrowentity = customeArrow(abstractarrowentity);
                    abstractarrowentity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                    if (f == 1.0F)
                    {
                        abstractarrowentity.setIsCritical(true);
                    }

                    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                    if (j > 0)
                    {
                        abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
                    }

                    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                    if (k > 0)
                    {
                        abstractarrowentity.setKnockbackStrength(k);
                    }

                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                    {
                        abstractarrowentity.setFire(100);
                    }

                    stack.damageItem(1, playerentity, (p_220009_1_) ->
                    {
                        p_220009_1_.sendBreakAnimation(playerentity.getActiveHand());
                    });

                    worldIn.addEntity(abstractarrowentity);
                }

                worldIn.playSound(null, playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                playerentity.addStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn instanceof LivingEntity && isSelected)
            {
                ArrayList<Effect> array = new ArrayList<>();
                for (EffectInstance eff : ((LivingEntity) entityIn).getActivePotionEffects())
                {
                    if (!eff.getPotion().isBeneficial())
                    {
                        array.add(eff.getPotion());
                    }
                }

                if (array.size() > 0)
                {
                    for (Effect eff : array)
                    {
                        ((LivingEntity) entityIn).removePotionEffect(eff);
                    }
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
        if (ret != null) return ret;

        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow)
    {
        return arrow;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes1")).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes2")).setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes3")).setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes4")).setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY)));
    }
}
