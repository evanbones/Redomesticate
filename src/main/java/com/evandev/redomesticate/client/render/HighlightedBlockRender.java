package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.client.model.HighlightedBlockModel;
import com.evandev.redomesticate.client.registry.OreColorRegistry;
import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.content.entity.HighlightedBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

//? if >=26.1 {
/*import net.minecraft.client.renderer.rendertype.RenderTypes;
*///?} else {
import net.minecraft.client.renderer.RenderType;
//?}

public class HighlightedBlockRender extends SimpleEntityRenderer<HighlightedBlockEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/highlighted_block.png");
    private final HighlightedBlockModel highlightedBlockModel = new HighlightedBlockModel();

    public HighlightedBlockRender(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    protected void render(HighlightedBlockEntity entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5F, 0);
        int color = OreColorRegistry.getBlockColor(entity.getBlockState());
        sink.draw(poseStack, RenderType.outline(TEXTURE), (stack, consumer) -> this.highlightedBlockModel.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, color));
        poseStack.popPose();
    }
}
