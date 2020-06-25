package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.Vec3i;

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
    public Item getActivationItem()
    {
        return Items.SCUTE;
    }

    @Override
    public Vec3i getRayColor()
    {
        return new Vec3i(127, 127, 255);
    }
}