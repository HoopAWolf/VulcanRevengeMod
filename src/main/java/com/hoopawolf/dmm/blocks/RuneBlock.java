package com.hoopawolf.dmm.blocks;

import com.hoopawolf.dmm.blocks.tileentity.RuneTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class RuneBlock extends Block
{

    public RuneBlock(Block.Properties properties)
    {
        super(properties.notSolid());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (hasTileEntity(state))
        {
            RuneTileEntity rune = (RuneTileEntity) worldIn.getTileEntity(pos);

            if (!rune.isActivated() && player.getHeldItemMainhand().getItem().equals(rune.getActivationItem().getItem()))
            {
                rune.setActivated(true);
                rune.markDirty();

                if (!worldIn.isRemote)
                {
                    if (!player.isCreative())
                    {
                        player.getHeldItemMainhand().shrink(1);
                    }
                }
            }
        }

        return ActionResultType.SUCCESS;
    }
}

