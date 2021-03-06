package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.Vec3i;

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
    public Item getActivationItem()
    {
        return Items.NETHER_BRICK;
    }

    @Override
    public Vec3i getRayColor()
    {
        return new Vec3i(255, 255, 255);
    }
}