package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.content.entity.GiantBubbleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

//? if >=26.1 {
/*import net.minecraft.client.renderer.rendertype.RenderTypes;
*///?}

public class GiantBubbleRender extends SimpleEntityRenderer<GiantBubbleEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/giant_bubble.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);

    public GiantBubbleRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected void render(GiantBubbleEntity entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation) {
        poseStack.pushPose();
        float age = entity.tickCount + partialTicks;
        float bubbleWobbleXZ = (float) Math.sin(age * 0.3F) * 0.2F;
        float bubbleWobbleY = (float) Math.cos(age * 0.3F) * 0.2F;
        poseStack.scale(2.6F + bubbleWobbleXZ, 2.6F + bubbleWobbleY, 2.6F + bubbleWobbleXZ);
        poseStack.mulPose(cameraOrientation);
        poseStack.mulPose(new Quaternionf().rotateY(180F * ((float) Math.PI / 180F)));
        sink.draw(poseStack, RENDER_TYPE, (stack, consumer) -> {
            PoseStack.Pose pose = stack.last();
            vertex(consumer, pose, light, 0.0F, 0, 0, 1);
            vertex(consumer, pose, light, 1.0F, 0, 1, 1);
            vertex(consumer, pose, light, 1.0F, 1, 1, 0);
            vertex(consumer, pose, light, 0.0F, 1, 0, 0);
        });
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float x, int y, int u, int v) {
        consumer.addVertex(pose.pose(), x - 0.5F, (float) y - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
