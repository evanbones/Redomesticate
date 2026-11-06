package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.content.entity.FollowingJukeboxEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;

public class JukeboxFollowerRender extends SimpleEntityRenderer<FollowingJukeboxEntity> {

    public JukeboxFollowerRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected void render(FollowingJukeboxEntity entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.translate(0, 0.1F, 0);
        poseStack.scale(1.8F, 1.8F, 1.8F);
        // item stacks can't be created before the world's components are bound, so build it when drawing
        ItemStack jukebox = new ItemStack(Items.JUKEBOX);
        jukebox.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        sink.item(jukebox, entity, poseStack, light);
        poseStack.popPose();
    }
}
