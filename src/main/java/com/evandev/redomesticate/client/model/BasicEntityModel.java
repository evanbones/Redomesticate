package com.evandev.redomesticate.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

public abstract class BasicEntityModel<T extends Entity> {
    public int textureWidth = 64;
    public int textureHeight = 32;

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
        this.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 0xFFFFFF);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgb) {
        float red = (rgb >> 16 & 0xFF) / 255F;
        float green = (rgb >> 8 & 0xFF) / 255F;
        float blue = (rgb & 0xFF) / 255F;
        this.parts().forEach(part -> part.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, 1.0F));
    }

    public abstract Iterable<BasicModelPart> parts();

    public abstract void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
}
