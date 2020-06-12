package com.hoopawolf.dmm.blocks;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SwordStoneBlock extends Block
{

    public SwordStoneBlock(Block.Properties properties)
    {
        super(properties.notSolid());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return TileEntityRegistryHandler.SWORD_STONE_TILE_ENTITY.get().create();
    }
}
