package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;

public class MagmaRuneTileEntity extends RuneTileEntity
{
    public MagmaRuneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public MagmaRuneTileEntity()
    {
        this(TileEntityRegistryHandler.MAGMA_RUNE_TILE_ENTITY.get());
    }

    @Override
    public ItemStack getActivationItem()
    {
        return Items.MAGMA_CREAM.getDefaultInstance();
    }
}