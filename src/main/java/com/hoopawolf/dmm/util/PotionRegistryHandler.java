package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionRegistryHandler
{
    public static final DeferredRegister<Potion> POTION = new DeferredRegister<>(ForgeRegistries.POTION_TYPES, Reference.MOD_ID);
    public static final DeferredRegister<Effect> POTION_EFFECT = new DeferredRegister<>(ForgeRegistries.POTIONS, Reference.MOD_ID);

    //EFFECTS


    //POTION

    public static void init(IEventBus _iEventBus)
    {
        POTION_EFFECT.register(_iEventBus);
        POTION.register(_iEventBus);
    }

}
