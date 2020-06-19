package com.hoopawolf.dmm.client.renderer;

import com.hoopawolf.dmm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class PesArrowRenderer extends ArrowRenderer<PesArrowEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/pesarrow.png");

    public PesArrowRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    protected int getBlockLight(PesArrowEntity entityIn, float partialTicks)
    {
        return 15;
    }

    @Override
    public ResourceLocation getEntityTexture(PesArrowEntity _entity)
    {
        return TEXTURE;
    }

}
