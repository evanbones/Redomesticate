package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.client.model.RecallBallModel;
import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.content.entity.RecallBallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

//? if >=26.1 {
/*import net.minecraft.client.renderer.rendertype.RenderTypes;
*///?}

public class RecallBallRender extends SimpleEntityRenderer<RecallBallEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/recall_ball.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
    private final RecallBallModel recallBallModel = new RecallBallModel();

    public RecallBallRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected void render(RecallBallEntity entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation) {
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotateX(180F * ((float) Math.PI / 180F)));
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        poseStack.translate(0, -1.65F, 0);
        sink.draw(poseStack, RENDER_TYPE, (stack, consumer) -> {
            this.recallBallModel.animateBall(entity, partialTicks);
            this.recallBallModel.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY);
        });
        poseStack.popPose();
    }
}
