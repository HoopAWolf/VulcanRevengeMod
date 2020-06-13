package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;

public class NetherRuneTileEntity extends RuneTileEntity
{
    public NetherRuneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public NetherRuneTileEntity()
    {
        this(TileEntityRegistryHandler.NETHER_RUNE_TILE_ENTITY.get());
    }

    @Override
    public ItemStack getActivationItem()
    {
        return Items.NETHER_BRICK.getDefaultInstance();
    }
}