package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;

public class ScuteRuneTileEntity extends RuneTileEntity
{
    public ScuteRuneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public ScuteRuneTileEntity()
    {
        this(TileEntityRegistryHandler.SCUTE_RUNE_TILE_ENTITY.get());
    }

    @Override
    public ItemStack getActivationItem()
    {
        return Items.SCUTE.getDefaultInstance();
    }
}