package com.hoopawolf.dmm.blocks.tileentity;

import com.hoopawolf.dmm.util.ItemBlockRegistryHandler;
import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SwordStoneTileEntity extends TileEntity implements ITickableTileEntity
{
    private static final BlockPos[] runePos =
            {
                    new BlockPos(2, 1, 2),
                    new BlockPos(-2, 1, -2),
                    new BlockPos(-2, 1, 2),
                    new BlockPos(2, 1, -2)
            };

    private static final EntityType[] firePhase =
            {
                    EntityType.BLAZE,
                    EntityType.MAGMA_CUBE,
                    EntityType.ZOMBIE_PIGMAN
            };

    private static final EntityType[] heavyPhase =
            {
                    EntityType.WITCH,
                    EntityType.VEX,
                    EntityType.PHANTOM
            };

    private static final EntityType[] slowPhase =
            {
                    EntityType.ZOMBIE,
                    EntityType.SKELETON,
                    EntityType.SPIDER
            };

    private static final EntityType[] witherPhase =
            {
                    EntityType.WITHER_SKELETON,
                    EntityType.CAVE_SPIDER,
                    EntityType.ILLUSIONER,
                    EntityType.EVOKER
            };

    private static final ArrayList<EntityType[]> phraseList = new ArrayList<>();

    private float timer, degree;
    private boolean activated, activatedDone;
    private UUID player;

    public SwordStoneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;
        timer = 0.0F;
        player = null;

        phraseList.add(firePhase);
        phraseList.add(slowPhase);
        phraseList.add(heavyPhase);
        phraseList.add(witherPhase);
    }

    public SwordStoneTileEntity()
    {
        this(TileEntityRegistryHandler.SWORD_STONE_TILE_ENTITY.get());
    }

    public boolean anyPlayerInRange()
    {
        return player != null && world.getPlayerByUuid(player) != null && Objects.requireNonNull(world.getPlayerByUuid(player)).getDistanceSq(getPos().getX(), getPos().getY(), getPos().getZ()) < 60;
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean flag)
    {
        activated = flag;
    }

    public boolean isDone()
    {
        return activatedDone;
    }

    public void setDone(boolean flag)
    {
        activatedDone = flag;
    }

    public float getDegree()
    {
        return degree;
    }

    public float getTimer()
    {
        return timer;
    }

    public void setUUID(UUID _uuidIn)
    {
        player = _uuidIn;
    }

    @Override
    public void tick()
    {
        if (!isDone())
        {
            for (BlockPos pos : runePos)
            {
                if (world.getBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())).hasTileEntity() &&
                        world.getTileEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())) instanceof RuneTileEntity)
                {
                    if (!((RuneTileEntity) Objects.requireNonNull(world.getTileEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())))).isActivated())
                    {
                        resetData();
                        break;
                    }
                } else
                {
                    resetData();
                    break;
                }
            }

            if (isActivated())
            {
                if (!anyPlayerInRange())
                {
                    resetData();
                } else
                {
                    if (timer <= 0)
                    {
                        if (!world.isRemote)
                        {
                            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 2.0F, 0.1F);
                            world.getWorld().getWorldInfo().setClearWeatherTime(0);
                            world.getWorld().getWorldInfo().setRainTime(6000);
                            world.getWorld().getWorldInfo().setThunderTime(6000);
                            world.getWorld().getWorldInfo().setRaining(true);
                            world.getWorld().getWorldInfo().setThundering(true);
                        }
                    } else if (timer % 150 == 0)
                    {
                        if (!world.isRemote)
                        {
                            int max = world.rand.nextInt(15) + 8;
                            for (int i = 0; i < max; ++i)
                            {
                                MobEntity entity = (MobEntity) phraseList.get(((int) timer / 150) - 1)[world.rand.nextInt(3)].create(world);
                                entity.setLocationAndAngles(this.getPos().getX() + (world.rand.nextInt(5) - world.rand.nextInt(5)),
                                        this.getPos().getY() + 1.0D,
                                        this.getPos().getZ() + (world.rand.nextInt(5) - world.rand.nextInt(5)), 0.0F, 0.0F);
                                entity.setAttackTarget(world.getPlayerByUuid(player));
                                world.addEntity(entity);
                            }
                        }
                    }

                    timer += 0.5F;

                    for (BlockPos pos : runePos)
                    {
                        Vec3d block = new Vec3d(getPos().getX() + pos.getX() + 0.5D, getPos().getY() + pos.getY() + 1.75D, getPos().getZ() + pos.getZ() + 0.5D);
                        Vec3d dir = new Vec3d(getPos().getX() + 0.5D, getPos().getY(), getPos().getZ() + 0.5).subtract(new Vec3d(block.getX(), block.getY(), block.getZ())).normalize();
                        this.world.addParticle(ParticleTypes.FIREWORK, block.getX(), block.getY(), block.getZ(), dir.getX(), dir.getY(), dir.getZ());
                        this.world.addParticle(ParticleTypes.END_ROD, block.getX(), block.getY(), block.getZ(), dir.getX(), dir.getY(), dir.getZ());
                    }

                    if (getTimer() > 600)
                    {
                        setDone(true);

                        if (!world.isRemote)
                        {
                            world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 2.0F, 0.1F);
                            world.getWorld().getWorldInfo().setClearWeatherTime(6000);
                            world.getWorld().getWorldInfo().setRainTime(0);
                            world.getWorld().getWorldInfo().setThunderTime(0);
                            world.getWorld().getWorldInfo().setRaining(false);
                            world.getWorld().getWorldInfo().setThundering(false);
                        }

                        for (BlockPos pos : runePos)
                        {
                            world.setBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ()), Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        if (getTimer() > 300)
        {
            degree += 0.5F;
        }
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        activated = compound.getBoolean("activated");
        activatedDone = compound.getBoolean("activatedDone");
        timer = compound.getFloat("timer");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putBoolean("activated", activated);
        compound.putBoolean("activatedDone", activatedDone);
        compound.putFloat("timer", timer);
        return compound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTag = new CompoundNBT();
        write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), 3, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag)
    {
        read(tag);
    }

    private void resetData()
    {
        timer = 0;
        degree = 0.0F;
        setActivated(false);
    }

    public ItemStack getActivationItem()
    {
        return isDone() ? ItemBlockRegistryHandler.VULCAN_SWORD.get().getDefaultInstance() : Items.STONE_SWORD.getDefaultInstance();
    }
}
