package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.content.entity.FeatherEntity;
import com.evandev.redomesticate.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

//? if >=26.1 {
/*import net.minecraft.client.renderer.rendertype.RenderTypes;
*///?} else {
import net.minecraft.client.renderer.RenderType;
//?}

public class FeatherRender extends SimpleEntityRenderer<FeatherEntity> {

    private static final int LINE_SEGMENTS = 16;

    public FeatherRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected void render(FeatherEntity entity, float partialTicks, PoseStack poseStack, RenderSink sink, int light, Quaternionf cameraOrientation) {
        poseStack.pushPose();
        poseStack.translate(0, 0.1F, 0);
        poseStack.mulPose(cameraOrientation);
        poseStack.mulPose(new Quaternionf().rotateZ(35F * ((float) Math.PI / 180F)));
        sink.item(new ItemStack(Items.FEATHER), entity, poseStack, light);
        poseStack.popPose();

        Player player = entity.getPlayerOwner();
        if (player != null) {
            Vec3 hand = this.getPlayerHandPos(player, partialTicks);
            double x = Mth.lerp(partialTicks, entity.xo, entity.getX());
            double y = Mth.lerp(partialTicks, entity.yo, entity.getY()) + 0.25D;
            double z = Mth.lerp(partialTicks, entity.zo, entity.getZ());
            float dx = (float) (hand.x - x);
            float dy = (float) (hand.y - y);
            float dz = (float) (hand.z - z);
            //? if >=26.1 {
            /*//? if >=26.2 {
            /^float width = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth;
            ^///?} else {
            float width = Minecraft.getInstance().gameRenderer.getGameRenderState().windowRenderState.appropriateLineWidth;
            //?}
            sink.draw(poseStack, RenderType.lines(), (stack, consumer) -> {
                for (int k = 0; k < LINE_SEGMENTS; ++k) {
                    stringVertex(dx, dy, dz, consumer, stack.last(), fraction(k), fraction(k + 1), width);
                    stringVertex(dx, dy, dz, consumer, stack.last(), fraction(k + 1), fraction(k), width);
                }
            });
            *///?} else {
            sink.draw(poseStack, RenderType.lineStrip(), (stack, consumer) -> {
                for (int k = 0; k <= LINE_SEGMENTS; ++k) {
                    stringVertex(dx, dy, dz, consumer, stack.last(), fraction(k), fraction(k + 1), 1F);
                }
            });
            //?}
        }
    }

    private Vec3 getPlayerHandPos(Player player, float partialTicks) {
        int arm = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (!player.getMainHandItem().is(ModItems.FEATHER_ON_A_STICK.get())) {
            arm = -arm;
        }
        float swing = Mth.sin(Mth.sqrt(player.getAttackAnim(partialTicks)) * (float) Math.PI);
        var options = Minecraft.getInstance().options;
        if (options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            float fov = options.fov().get().intValue();
            //? if >=26.1 {
            /*Vec3 view = this.entityRenderDispatcher.camera.getNearPlane(fov).getPointOnPlane(arm * 0.525F, -0.1F);
            *///?} else {
            Vec3 view = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane(arm * 0.525F, -0.1F);
            //?}
            view = view.scale(960.0D / fov).yRot(swing * 0.5F).xRot(-swing * 0.7F);
            return player.getEyePosition(partialTicks).add(view);
        } else {
            float bodyRot = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * ((float) Math.PI / 180F);
            double sin = Mth.sin(bodyRot);
            double cos = Mth.cos(bodyRot);
            double right = arm * 0.35D;
            float crouch = player.isCrouching() ? -0.1875F : 0.0F;
            return player.getEyePosition(partialTicks).add(-cos * right - sin * 0.8D, crouch - 0.45D, -sin * right + cos * 0.8D);
        }
    }

    private static float fraction(int segment) {
        return (float) segment / (float) LINE_SEGMENTS;
    }

    private static void stringVertex(float dx, float dy, float dz, VertexConsumer consumer, PoseStack.Pose pose, float a, float next, float width) {
        float x = dx * a;
        float y = dy * (a * a + a) * 0.5F + 0.25F;
        float z = dz * a;
        float nx = dx * next - x;
        float ny = dy * (next * next + next) * 0.5F + 0.25F - y;
        float nz = dz * next - z;
        float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= length;
        ny /= length;
        nz /= length;
        //? if >=26.1 {
        /*consumer.addVertex(pose, x, y, z).setColor(0, 0, 0, 255).setNormal(pose, nx, ny, nz).setLineWidth(width);
        *///?} else {
        consumer.addVertex(pose.pose(), x, y, z).setColor(0, 0, 0, 255).setNormal(pose, nx, ny, nz);
        //?}
    }
}
