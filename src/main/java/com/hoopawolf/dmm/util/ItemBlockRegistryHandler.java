package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ItemBlockRegistryHandler
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static void init(IEventBus _iEventBus)
    {
        ITEMS.register(_iEventBus);
    }
}