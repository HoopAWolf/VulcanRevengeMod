package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.blocks.tileentity.SwordStoneTileEntity;
import com.hoopawolf.dmm.client.tileentity.SwordStoneRenderer;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class TileEntityRegistryHandler
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    //TILE ENTITES
    public static final RegistryObject<TileEntityType<SwordStoneTileEntity>> SWORD_STONE_TILE_ENTITY = TILE_ENTITIES.register("swordstone", () ->
            TileEntityType.Builder.create(SwordStoneTileEntity::new, ItemBlockRegistryHandler.SWORD_STONE_BLOCK.get()).build(null));

    public static void init(IEventBus _iEventBus)
    {
        TILE_ENTITIES.register(_iEventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerTileEntityRenderer()
    {
        RenderTypeLookup.setRenderLayer(ItemBlockRegistryHandler.SWORD_STONE_BLOCK.get(), RenderType.getCutoutMipped());
        ClientRegistry.bindTileEntityRenderer(SWORD_STONE_TILE_ENTITY.get(),
                SwordStoneRenderer::new);
    }
}
