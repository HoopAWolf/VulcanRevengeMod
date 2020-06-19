package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.potion.PotionEffectBase;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionRegistryHandler
{
    public static final DeferredRegister<Potion> POTION = new DeferredRegister<>(ForgeRegistries.POTION_TYPES, Reference.MOD_ID);
    public static final DeferredRegister<Effect> POTION_EFFECT = new DeferredRegister<>(ForgeRegistries.POTIONS, Reference.MOD_ID);

    //EFFECTS
    public static final RegistryObject<Effect> PLAGUE_EFFECT = POTION_EFFECT.register("plagueeffect", () -> new PotionEffectBase(EffectType.HARMFUL, 3035801));

    //POTION
    public static final RegistryObject<Potion> PLAGUE_POTION = POTION.register("plaguepotion", () -> new Potion(new EffectInstance(PLAGUE_EFFECT.get(), 1000)));

    public static void init(IEventBus _iEventBus)
    {
        POTION_EFFECT.register(_iEventBus);
        POTION.register(_iEventBus);
    }
}
