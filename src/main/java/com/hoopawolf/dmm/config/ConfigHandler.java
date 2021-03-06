package com.hoopawolf.dmm.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHandler
{
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static
    {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static
    {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Client
    {

        public Client(ForgeConfigSpec.Builder builder)
        {

        }
    }

    public static class Common
    {
        public final ForgeConfigSpec.IntValue minStructureAway;
        public final ForgeConfigSpec.IntValue maxStructureAway;
        public final ForgeConfigSpec.IntValue structureSpawnChance;

        public Common(ForgeConfigSpec.Builder builder)
        {
            minStructureAway = builder
                    .comment("Min distance away from chosen spot")
                    .defineInRange("structure.spawndist.min", 7, 0, Integer.MAX_VALUE);

            maxStructureAway = builder
                    .comment("Max distance away from chosen spot")
                    .defineInRange("structure.spawndist.max", 12, 0, Integer.MAX_VALUE);

            structureSpawnChance = builder
                    .comment("Structure spawning percentage")
                    .defineInRange("structure.spawnpercentage", 40, 0, 100);
        }
    }
}