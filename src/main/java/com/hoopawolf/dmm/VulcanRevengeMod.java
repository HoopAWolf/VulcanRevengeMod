package com.hoopawolf.dmm;

import com.hoopawolf.dmm.proxy.ClientProxy;
import com.hoopawolf.dmm.proxy.CommonProxy;
import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.util.EntityRegistryHandler;
import com.hoopawolf.dmm.util.ItemBlockRegistryHandler;
import com.hoopawolf.dmm.util.ParticleRegistryHandler;
import com.hoopawolf.dmm.util.PotionRegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class VulcanRevengeMod
{
    public VulcanRevengeMod()
    {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(ClientProxy::onClientSetUp);
        modEventBus.addListener(CommonProxy::onCommonSetupEvent);

        ItemBlockRegistryHandler.init(modEventBus);
        EntityRegistryHandler.ENTITIES.register(modEventBus);
        ParticleRegistryHandler.PARTICLES.register(modEventBus);
        PotionRegistryHandler.init(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
