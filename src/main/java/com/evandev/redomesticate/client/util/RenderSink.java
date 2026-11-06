package com.evandev.redomesticate.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

//? if >=26.1 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
*///?} else {
import net.minecraft.client.renderer.MultiBufferSource;
//?}

public interface RenderSink {

    void draw(PoseStack poseStack, RenderType renderType, Drawer drawer);

    void item(ItemStack stack, Entity holder, PoseStack poseStack, int light);

    @FunctionalInterface
    interface Drawer {
        void draw(PoseStack poseStack, VertexConsumer consumer);
    }

    //? if >=26.1 {
    /*static RenderSink of(SubmitNodeCollector collector) {
        return new RenderSink() {
            @Override
            public void draw(PoseStack poseStack, RenderType renderType, Drawer drawer) {
                collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
                    PoseStack local = new PoseStack();
                    local.last().set(pose);
                    drawer.draw(local, consumer);
                });
            }

            @Override
            public void item(ItemStack stack, Entity holder, PoseStack poseStack, int light) {
                ItemStackRenderState state = new ItemStackRenderState();
                Minecraft.getInstance().getItemModelResolver().updateForNonLiving(state, stack, ItemDisplayContext.GROUND, holder);
                state.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
            }
        };
    }
    *///?} else {
    static RenderSink of(MultiBufferSource buffers) {
        return new RenderSink() {
            @Override
            public void draw(PoseStack poseStack, RenderType renderType, Drawer drawer) {
                drawer.draw(poseStack, buffers.getBuffer(renderType));
            }

            @Override
            public void item(ItemStack stack, Entity holder, PoseStack poseStack, int light) {
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, buffers, holder.level(), holder.getId());
            }
        };
    }
    //?}
}
