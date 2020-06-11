package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.items.weapons.VulcanSword;
import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ItemBlockRegistryHandler
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> VULCAN_SWORD = ITEMS.register("vulcansrevenge", () -> new VulcanSword(ItemTier.DIAMOND, 3, -2.5f, new Item.Properties().maxDamage(500)));
    public static final RegistryObject<Item> BROKEN_SWORD = ITEMS.register("brokensword", () -> new Item(new Item.Properties().maxStackSize(1).group(VRMItemGroup.instance)));

    public static void init(IEventBus _iEventBus)
    {
        ITEMS.register(_iEventBus);
    }
}