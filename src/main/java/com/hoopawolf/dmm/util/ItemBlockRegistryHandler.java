package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.blocks.SwordStoneBlock;
import com.hoopawolf.dmm.items.weapons.BrokenSwordItem;
import com.hoopawolf.dmm.items.weapons.VulcanSwordItem;
import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ItemBlockRegistryHandler
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    //BLOCKS
    public static final RegistryObject<Block> SWORD_STONE_BLOCK = BLOCKS.register("swordstone", () -> new SwordStoneBlock(Block.Properties.create(Material.ANVIL)));

    //ITEMS
    public static final RegistryObject<Item> VULCAN_SWORD = ITEMS.register("vulcansrevenge", () -> new VulcanSwordItem(ItemTier.DIAMOND, 3, -2.5f, new Item.Properties().maxDamage(500)));
    public static final RegistryObject<Item> BROKEN_SWORD = ITEMS.register("brokensword", () -> new BrokenSwordItem(new Item.Properties().maxStackSize(1)));
    public static final RegistryObject<BlockItem> SWORD_STONE = ITEMS.register("swordstone",
            () -> new BlockItem(SWORD_STONE_BLOCK.get(), new Item.Properties().group(VRMItemGroup.instance)));


    public static void init(IEventBus _iEventBus)
    {
        BLOCKS.register(_iEventBus);
        ITEMS.register(_iEventBus);
    }
}