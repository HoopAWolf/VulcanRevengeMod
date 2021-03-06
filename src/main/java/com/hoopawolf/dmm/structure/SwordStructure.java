package com.hoopawolf.dmm.structure;

import com.hoopawolf.dmm.config.ConfigHandler;
import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.structure.piece.SwordStructurePiece;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

import java.util.Random;
import java.util.function.Function;

public class SwordStructure extends Structure<NoFeatureConfig>
{
    public SwordStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
    {
        super(config);
    }

    @Override
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ)
    {
        int maxDistance = ConfigHandler.COMMON.minStructureAway.get();
        int minDistance = ConfigHandler.COMMON.maxStructureAway.get();

        int xTemp = x + maxDistance * spacingOffsetsX;
        int ztemp = z + maxDistance * spacingOffsetsZ;
        int xTemp2 = xTemp < 0 ? xTemp - maxDistance + 1 : xTemp;
        int zTemp2 = ztemp < 0 ? ztemp - maxDistance + 1 : ztemp;
        int validChunkX = xTemp2 / maxDistance;
        int validChunkZ = zTemp2 / maxDistance;

        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), validChunkX, validChunkZ, this.getSeedModifier());
        validChunkX = validChunkX * maxDistance;
        validChunkZ = validChunkZ * maxDistance;
        validChunkX = validChunkX + random.nextInt(maxDistance + minDistance);
        validChunkZ = validChunkZ + random.nextInt(maxDistance + minDistance);

        return new ChunkPos(validChunkX, validChunkZ);
    }

    @Override
    public String getStructureName()
    {
        return Reference.MOD_ID + ":swordstructure";
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    @Override
    public Structure.IStartFactory getStartFactory()
    {
        return SwordStructure.Start::new;
    }

    protected int getSeedModifier()
    {
        return 62353535;
    }

    @Override
    public boolean canBeGenerated(BiomeManager p_225558_1_, ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ, Biome biome)
    {
        ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);

        if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z && rand.nextInt(100) < ConfigHandler.COMMON.structureSpawnChance.get())
        {
            return chunkGen.hasStructure(biome, this);
        }

        return false;
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn)
        {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            int surfaceY = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, surfaceY, z);

            SwordStructurePiece.start(templateManagerIn, blockpos, rotation, this.components, this.rand);

            this.recalculateStructureSize();

            Reference.LOGGER.log(Level.DEBUG, "Sword Structure at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
        }

    }
}
