package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;

public class BlazeRuneTileEntity extends RuneTileEntity
{
    public BlazeRuneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public BlazeRuneTileEntity()
    {
        this(TileEntityRegistryHandler.BLAZE_RUNE_TILE_ENTITY.get());
    }

    @Override
    public ItemStack getActivationItem()
    {
        return Items.BLAZE_POWDER.getDefaultInstance();
    }
}