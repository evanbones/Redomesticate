package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.content.entity.PsychicWallEntity;
import com.evandev.redomesticate.registry.ModRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;

public class PsychicWallRender extends SimpleEntityRenderer<PsychicWallEntity> {

    public PsychicWallRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected void render(PsychicWallEntity entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation) {
        poseStack.pushPose();
        float growth = Math.min(10, entity.tickCount + partialTicks) / 10F;
        if (entity.getLifespan() < 10) {
            growth = (entity.getLifespan() - partialTicks) / 10F;
        }
        float f = entity.getBlockWidth() * growth;
        poseStack.mulPose(entity.getWallDirection().getOpposite().getRotation());
        poseStack.scale(f * 0.5F, f * 0.5F, f * 0.5F);
        sink.draw(poseStack, ModRenderTypes.PSYCHIC_WALL, (stack, consumer) -> renderWall(stack, f, consumer));
        sink.draw(poseStack, ModRenderTypes.PSYCHIC_WALL_BORDER, (stack, consumer) -> renderWall(stack, 1, consumer));
        poseStack.popPose();
    }

    private static void renderWall(PoseStack poseStack, float size, VertexConsumer consumer) {
        PoseStack.Pose pose = poseStack.last();
        drawVertex(pose, consumer, -1, 0, -1, 0, 0);
        drawVertex(pose, consumer, -1, 0, 1, 0, size);
        drawVertex(pose, consumer, 1, 0, 1, size, size);
        drawVertex(pose, consumer, 1, 0, -1, size, 0);
    }

    private static void drawVertex(PoseStack.Pose pose, VertexConsumer consumer, int x, int y, int z, float u, float v) {
        consumer.addVertex(pose.pose(), (float) x, (float) y, (float) z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(240, 240).setNormal(pose, 1, 1, 0);
    }
}
