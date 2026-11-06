package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.client.util.RenderSink;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;

//? if >=26.1 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
*///?} else {
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
//?}

//? if >=26.1 {
/*public abstract class SimpleEntityRenderer<T extends Entity> extends EntityRenderer<T, SimpleEntityRenderer.State<T>> {
*///?} else {
public abstract class SimpleEntityRenderer<T extends Entity> extends EntityRenderer<T> {
//?}

    protected SimpleEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected abstract void render(T entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation);

    //? if >=26.1 {
    /*@Override
    public State<T> createRenderState() {
        return new State<>();
    }

    @Override
    public void extractRenderState(T entity, State<T> state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(State<T> state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (state.entity != null) {
            this.render(state.entity, state.partialTicks, poseStack, RenderSink.of(collector), state.lightCoords, camera.orientation);
        }
    }

    public static class State<T extends Entity> extends EntityRenderState {
        public T entity;
        public float partialTicks;
    }
    *///?} else {
    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
        this.render(entity, partialTicks, poseStack, RenderSink.of(buffer), light, this.entityRenderDispatcher.cameraOrientation());
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
    //?}
}
