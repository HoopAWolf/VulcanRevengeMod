package com.hoopawolf.dmm.client.tileentity;

import com.hoopawolf.dmm.blocks.tileentity.RuneTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class RuneRenderer<T extends RuneTileEntity> extends TileEntityRenderer<T>
{
    public RuneRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        if (tileEntityIn.isActivated())
        {
            matrixStackIn.push();
            matrixStackIn.translate(0.5D, 1.35D, 0.5D);
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            float currentTime = tileEntityIn.getWorld().getGameTime() + partialTicks;
            matrixStackIn.translate(0D, Math.sin(Math.PI * (currentTime * 0.0125F)) * 0.2F, 0D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.getDegree()));
            renderItem(tileEntityIn.getActivationItem(), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
            matrixStackIn.pop();
        }
    }

    private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                            int combinedLightIn)
    {
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
    }
}
