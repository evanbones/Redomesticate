package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.client.data.LightningBoltData;
import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.content.entity.ChainLightningEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.joml.Vector4f;

public class ChainLightningRender extends SimpleEntityRenderer<ChainLightningEntity> {

    private final LightningRender lightningRender = new LightningRender();
    private final LightningBoltData.BoltRenderInfo lightningBoltData = new LightningBoltData.BoltRenderInfo(1.3F, 0.15F, 0.5F, 0.25F, new Vector4f(0.1F, 0.3F, 0.5F, 0.5F), 0.45F);

    public ChainLightningRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    //? if >=26.3 {
    /*@Override
    public boolean shouldRender(ChainLightningEntity entity, Frustum frustum, double x, double y, double z, float partialTicks) {
        Entity next = entity.getFromEntity();
        return next != null && frustum.isVisible(entity.getBoundingBox().minmax(next.getBoundingBox())) || super.shouldRender(entity, frustum, x, y, z, partialTicks);
    }
    *///?} else {
    @Override
    public boolean shouldRender(ChainLightningEntity entity, Frustum frustum, double x, double y, double z) {
        Entity next = entity.getFromEntity();
        return next != null && frustum.isVisible(entity.getBoundingBox().minmax(next.getBoundingBox())) || super.shouldRender(entity, frustum, x, y, z);
    }
    //?}

    @Override
    protected void render(ChainLightningEntity entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation) {
        Entity from = entity.getFromEntity();
        if (from == null) return;
        poseStack.pushPose();
        float x = (float) Mth.lerp(partialTicks, entity.xo, entity.getX());
        float y = (float) Mth.lerp(partialTicks, entity.yo, entity.getY());
        float z = (float) Mth.lerp(partialTicks, entity.zo, entity.getZ());
        LightningBoltData bolt = new LightningBoltData(lightningBoltData, from.getEyePosition(), entity.position(), 5)
                .size(0.1F)
                .lifespan(2)
                .spawn(LightningBoltData.SpawnFunction.NO_DELAY);
        lightningRender.update(null, bolt, partialTicks);
        poseStack.translate(-x, -y, -z);
        lightningRender.render(partialTicks, poseStack, sink);
        poseStack.popPose();
    }
}
