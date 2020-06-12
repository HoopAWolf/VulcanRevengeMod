package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class SwordStoneTileEntity extends TileEntity implements ITickableTileEntity
{
    public SwordStoneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public SwordStoneTileEntity()
    {
        this(TileEntityRegistryHandler.SWORD_STONE_TILE_ENTITY.get());
    }

    @Override
    public void tick()
    {

    }
}
