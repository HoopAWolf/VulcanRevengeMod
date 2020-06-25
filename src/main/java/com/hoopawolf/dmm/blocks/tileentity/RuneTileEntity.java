package com.hoopawolf.dmm.blocks.tileentity;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.Vec3i;

public abstract class RuneTileEntity extends TileEntity
{
    private boolean isActivated;
    private float degree;

    public RuneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;
    }

    public boolean isActivated()
    {
        return isActivated;
    }

    public void setActivated(boolean flag)
    {
        isActivated = flag;
    }

    public float getDegree()
    {
        return degree += 0.5F;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        isActivated = compound.getBoolean("activated");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putBoolean("activated", isActivated);
        return compound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTag = new CompoundNBT();
        write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        CompoundNBT tag = pkt.getNbtCompound();
        read(tag);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag)
    {
        this.read(tag);
    }

    public abstract Item getActivationItem();

    public abstract Vec3i getRayColor();
}
