package com.evandev.redomesticate.client.render;

import com.evandev.redomesticate.client.data.LightningBoltData;
import com.evandev.redomesticate.client.data.RenderData;
import com.evandev.redomesticate.client.model.BlazingBarModel;
import com.evandev.redomesticate.client.model.ShadowHandModel;
import com.evandev.redomesticate.client.util.RenderSink;
import com.evandev.redomesticate.platform.Services;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.registry.ModItems;
import com.evandev.redomesticate.registry.ModRenderTypes;
import com.evandev.redomesticate.util.TameableUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Random;

//? if >=26.1 {
/*import com.evandev.redomesticate.client.particle.ParticleIntimidation;
import com.evandev.redomesticate.client.util.EntityRenderStateAccess;
import net.minecraft.util.ARGB;
import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.LightCoordsUtil;
*///?} else {
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//?}

//? if >=26.1 {
/*public class LayerPetOverlays<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
*///?} else {
@SuppressWarnings({"rawtypes", "unchecked"})
public class LayerPetOverlays extends RenderLayer {
//?}

    private static final int CLOUD_COUNT = 14;
    private static final Vec3[] CLOUD_OFFSETS = new Vec3[CLOUD_COUNT];
    private static final Vec3[] CLOUD_SCALES = new Vec3[CLOUD_COUNT];
    private static final ShadowHandModel SHADOW_HAND_MODEL = new ShadowHandModel();
    private static final BlazingBarModel BLAZING_BAR_MODEL = new BlazingBarModel();
    private static final ResourceLocation BLAZE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/blaze.png");

    static {
        Random random = new Random(500);
        for (int i = 0; i < CLOUD_COUNT; i++) {
            CLOUD_OFFSETS[i] = new Vec3(random.nextFloat() - 0.5F, 0.2F * (random.nextFloat() - 0.5F), random.nextFloat() - 0.5F).scale(1.2F);
            CLOUD_SCALES[i] = new Vec3(0.6F + random.nextFloat() * 0.2F, 0.4F + random.nextFloat() * 0.2F, 0.4F + random.nextFloat() * 0.2F);
        }
    }

    private final LightningRender lightningRender = new LightningRender();
    private final LightningBoltData.BoltRenderInfo healthBoltData = new LightningBoltData.BoltRenderInfo(0.3F, 0.0F, 0.0F, 0.0F, new Vector4f(0.4F, 0, 0, 0.4F), 0.2F);

    //? if >=26.1 {
    /*private final RenderLayerParent<S, M> renderer;

    public LayerPetOverlays(RenderLayerParent<S, M> parent) {
        super(parent);
        this.renderer = parent;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, S state, float yRot, float xRot) {
        if (((EntityRenderStateAccess) state).redomesticate$getEntity() instanceof LivingEntity living) {
            float partialTicks = ((EntityRenderStateAccess) state).redomesticate$getPartialTicks();
            int overlay = LivingEntityRenderer.getOverlayCoords(state, 0);
            this.renderOverlays(living, partialTicks, poseStack, RenderSink.of(collector), light, overlay,
                    //? if >=26.3 {
                    /^() -> collector.submitModel(this.getParentModel(), state, poseStack, ModRenderTypes.IFRAME_GLINT, light, overlay, 0));
                    ^///?} else {
                    () -> collector.submitModel(this.getParentModel(), state, poseStack, ModRenderTypes.IFRAME_GLINT, light, overlay, 0, null));
                    //?}
            this.submitIntimidationGhosts(living, partialTicks, poseStack, collector, light, state);
        }
    }

    @SuppressWarnings("unchecked")
    private void submitIntimidationGhosts(LivingEntity living, float partialTicks, PoseStack poseStack, SubmitNodeCollector collector, int light, S state) {
        List<ParticleIntimidation> ghosts = ParticleIntimidation.getFor(living.getId());
        if (ghosts.isEmpty() || !(this.renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) {
            return;
        }
        ResourceLocation texture = ((LivingEntityRenderer<?, S, ?>) livingRenderer).getTextureLocation(state);
        for (ParticleIntimidation ghost : ghosts) {
            float[] transform = ghost.getGhostTransform(living.getBbHeight(), partialTicks);
            float scale = transform[1];
            if (scale <= 0) {
                continue;
            }
            poseStack.pushPose();
            poseStack.translate(0, -transform[0], 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(ghost.getYRot() - state.bodyRot));
            poseStack.scale(scale, scale, scale);
            int color = ARGB.colorFromFloat(Math.min(scale, 1.0F) * 0.5F, 1.0F, 1.0F, 1.0F);
            //? if >=26.3 {
            /^collector.submitModel(this.getParentModel(), state, poseStack, RenderType.entityTranslucent(texture), light, OverlayTexture.NO_OVERLAY, color, null, 0);
            ^///?} else {
            collector.submitModel(this.getParentModel(), state, poseStack, RenderType.entityTranslucent(texture), light, OverlayTexture.NO_OVERLAY, color, null, 0, null);
            //?}
            poseStack.popPose();
        }
    }
    *///?} else {
    public LayerPetOverlays(RenderLayerParent parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof LivingEntity living) {
            int overlay = LivingEntityRenderer.getOverlayCoords(living, 0);
            this.renderOverlays(living, partialTicks, poseStack, RenderSink.of(buffer), light, overlay,
                    () -> this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(ModRenderTypes.IFRAME_GLINT), light, overlay));
        }
    }
    //?}

    private void renderOverlays(LivingEntity living, float partialTicks, PoseStack poseStack, RenderSink sink, int light, int overlay, Runnable renderParentWithIframeGlint) {
        if (!TameableUtils.isTamed(living)) {
            return;
        }
        float bodyRot = Mth.rotLerp(partialTicks, living.yBodyRotO, living.yBodyRot);
        float realAge = living.tickCount + partialTicks;

        if (TameableUtils.hasEnchant(living, ModEnchantments.IMMUNITY_FRAME) && TameableUtils.getImmuneTime(living) > 0) {
            renderParentWithIframeGlint.run();
        }

        if (TameableUtils.hasEnchant(living, ModEnchantments.MAGNETIC)) {
            Entity target = TameableUtils.getPetAttackTarget(living);
            if (target != null) {
                Vec3 delta = lerpedOffset(target, living, partialTicks);
                double horizontal = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
                float yaw = (float) (Mth.atan2(delta.z, delta.x) * (180F / Math.PI)) - 90.0F;
                float pitch = (float) (-(Mth.atan2(delta.y, horizontal) * (180F / Math.PI)));
                poseStack.pushPose();
                poseStack.mulPose(Axis.YN.rotationDegrees(bodyRot));
                poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
                poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
                float bob1 = (float) Math.sin(realAge * 0.5F) * 0.05F;
                float bob2 = (float) Math.sin(realAge * 0.3F) * 0.09F - 0.03F;
                float bob3 = (float) Math.cos(realAge * 0.1F) * 0.05F;
                poseStack.translate(bob1, 1.25F - living.getBbHeight() * 0.5F - bob2, -living.getBbWidth() - 0.125F - bob3);
                poseStack.mulPose(Axis.XN.rotationDegrees(90));
                poseStack.scale(1.6F, 1.6F, 3F);
                sink.item(new ItemStack(ModItems.MAGNET.get()), living, poseStack, light);
                poseStack.popPose();
            }
        }

        if (TameableUtils.hasEnchant(living, ModEnchantments.HEALTH_SIPHON)) {
            Entity owner = TameableUtils.getOwnerOf(living);
            if (owner != null && owner.isAlive() && owner.distanceTo(living) < 100) {
                float x = (float) Mth.lerp(partialTicks, living.xo, living.getX());
                float y = (float) Mth.lerp(partialTicks, living.yo, living.getY());
                float z = (float) Mth.lerp(partialTicks, living.zo, living.getZ());
                if (living.hurtTime > 0 && living.hurtTime == living.hurtDuration - 1) {
                    float height = -2 + living.getBbHeight() * 0.8F;
                    float ownerHeight = -2 + owner.getBbHeight() * 0.6F;
                    LightningBoltData bolt = new LightningBoltData(healthBoltData, new Vec3(x, y + height, z), owner.position().add(0, ownerHeight, 0), 3)
                            .size(0.5F)
                            .lifespan(5)
                            .spawn(LightningBoltData.SpawnFunction.NO_DELAY);
                    lightningRender.update(living, bolt, partialTicks);
                }
                poseStack.pushPose();
                poseStack.mulPose(Axis.YN.rotationDegrees(bodyRot));
                poseStack.mulPose(Axis.XN.rotationDegrees(180));
                poseStack.translate(-x, -y, -z);
                lightningRender.render(partialTicks, poseStack, sink);
                poseStack.popPose();
            }
        }

        if (TameableUtils.hasEnchant(living, ModEnchantments.VOID_CLOUD) && !living.isInWaterOrBubble() && !living.onGround() && TameableUtils.getFallDistance(living) >= 3.0F) {
            poseStack.pushPose();
            poseStack.translate(0.4F, 1.25F + living.getBbHeight(), 0.2F);
            poseStack.mulPose(Axis.YN.rotationDegrees(bodyRot));
            poseStack.mulPose(Axis.XN.rotationDegrees(180));
            for (int i = 0; i < CLOUD_COUNT; i++) {
                float xSin = (float) Math.sin(realAge * 0.05F + i * 2F) * 0.1F;
                float ySin = (float) Math.cos(realAge * 0.05F + i * 2F) * 0.1F;
                float zSin = (float) Math.sin(realAge * 0.05F + i * 2F - 2F) * 0.1F;
                poseStack.pushPose();
                poseStack.translate(CLOUD_OFFSETS[i].x + xSin, CLOUD_OFFSETS[i].y + ySin, CLOUD_OFFSETS[i].z + zSin);
                poseStack.scale((float) CLOUD_SCALES[i].x + xSin, (float) CLOUD_SCALES[i].y + ySin, (float) CLOUD_SCALES[i].z + xSin);
                sink.draw(poseStack, ModRenderTypes.VOID_CLOUD, LayerPetOverlays::renderVoidCloudCube);
                poseStack.popPose();
            }
            poseStack.popPose();
        }

        int shadowHandCount = TameableUtils.getEnchantLevel(living, ModEnchantments.SHADOW_HANDS);
        if (shadowHandCount > 0) {
            Entity punching = TameableUtils.getPetAttackTarget(living);
            Vec3 delta = punching == null ? Vec3.ZERO : lerpedOffset(punching, living, partialTicks);
            double horizontal = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
            float yaw = (float) (Mth.atan2(delta.z, delta.x) * (180F / Math.PI)) - 90.0F;
            float pitch = (float) (-(Mth.atan2(delta.y, horizontal) * (180F / Math.PI)));
            for (int i = 0; i < shadowHandCount; i++) {
                final int handIndex = i;
                float punch = getPunchFor(living, i, partialTicks) / 10F;
                float xSpread = shadowHandCount == 1 ? 0F : (i / (float) (shadowHandCount - 1)) - 0.5F;
                float xOffset = shadowHandCount == 1 ? 0 : Mth.sin((float) (xSpread * Math.PI)) * 1.8F;
                float zOffset = shadowHandCount == 1 ? 0.25F : Mth.cos((float) (xSpread * Math.PI)) * 1.8F;
                float yOffset = -zOffset * 0.3F;
                Vec3 fromPos = new Vec3(0, 1.25F - living.getBbHeight() * 0.5F, -living.getBbWidth() * 0.3F);
                Vec3 handTranslate = new Vec3(xOffset * living.getBbWidth(), 1 + yOffset + Math.sin(realAge * 0.2F + i * 1.5F) * 0.15F, zOffset * living.getBbWidth());
                if (punching != null) {
                    handTranslate = handTranslate.add(delta.scale(punch)).yRot((float) Math.PI + yaw * (float) (Math.PI / 180));
                }
                poseStack.pushPose();
                if (punching != null) {
                    poseStack.mulPose(Axis.YN.rotationDegrees(bodyRot));
                    poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
                    poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
                }
                poseStack.pushPose();
                poseStack.translate(handTranslate.x, handTranslate.y - 1.15F, handTranslate.z - 0.15F);
                sink.draw(poseStack, ModRenderTypes.SHADOW_HAND_ENTITY, (stack, consumer) -> {
                    SHADOW_HAND_MODEL.animateShadowHand(punch, handIndex, shadowHandCount, realAge);
                    SHADOW_HAND_MODEL.renderToBuffer(stack, consumer, light, overlay);
                });
                poseStack.popPose();
                renderShadowString(living, fromPos, poseStack, sink, handTranslate);
                poseStack.popPose();
            }
        }

        if (TameableUtils.hasEnchant(living, ModEnchantments.BLAZING_PROTECTION)) {
            int bars = TameableUtils.getBlazingProtectionBars(living);
            float rotation = realAge * 7;
            float separation = 360F / (TameableUtils.getEnchantLevel(living, ModEnchantments.BLAZING_PROTECTION) * 2F);
            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(bodyRot));
            for (int i = 0; i < bars; i++) {
                rotation += separation;
                final float barRotation = rotation;
                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
                float bob = (float) Math.sin(realAge * 0.6F + Math.toRadians(separation * i)) * 0.15F - 0.07F;
                poseStack.translate(0, 0.4F - living.getBbHeight() * 0.5F - bob, -living.getBbWidth() - 0.2F);
                sink.draw(poseStack, Services.PLATFORM.getUnlitTranslucent(BLAZE_TEXTURE), (stack, consumer) -> {
                    BLAZING_BAR_MODEL.animateBar(barRotation);
                    BLAZING_BAR_MODEL.renderToBuffer(stack, consumer, 240, overlay);
                });
                poseStack.popPose();
            }
            poseStack.popPose();
        }

        if (TameableUtils.hasEnchant(living, ModEnchantments.HEALING_AURA)) {
            int t = TameableUtils.getHealingAuraTime(living);
            if (t > 0) {
                float time = t > 20 ? 200 - Math.max(180, t + partialTicks) : t - partialTicks;
                float pulse = 0.9F + (float) (Math.sin(realAge * 0.08F) * 0.1F + 0.1F);
                float healScale = (Math.min(time, 20) / 20F) * 2.2F * pulse;
                poseStack.pushPose();
                poseStack.translate(0, 1.8F - living.getBbHeight() * 0.5F, 0);
                poseStack.mulPose(Axis.YN.rotationDegrees(bodyRot));
                poseStack.mulPose(Axis.YP.rotationDegrees(realAge * 3F));
                poseStack.mulPose(Axis.XP.rotationDegrees(90F));
                poseStack.scale(3 * healScale, 3 * healScale, 3 * healScale);
                sink.draw(poseStack, ModRenderTypes.HEALING_AURA, (stack, consumer) -> {
                    PoseStack.Pose pose = stack.last();
                    auraVertex(consumer, pose, 0.0F, 0, 0, 1);
                    auraVertex(consumer, pose, 1.0F, 0, 1, 1);
                    auraVertex(consumer, pose, 1.0F, 1, 1, 0);
                    auraVertex(consumer, pose, 0.0F, 1, 0, 0);
                });
                poseStack.popPose();
            }
        }
    }

    private static Vec3 lerpedOffset(Entity to, Entity from, float partialTicks) {
        return new Vec3(
                Mth.lerp(partialTicks, to.xo, to.getX()) - Mth.lerp(partialTicks, from.xo, from.getX()),
                Mth.lerp(partialTicks, to.yo, to.getY()) - Mth.lerp(partialTicks, from.yo, from.getY()),
                Mth.lerp(partialTicks, to.zo, to.getZ()) - Mth.lerp(partialTicks, from.zo, from.getZ()));
    }

    private static void renderShadowString(Entity from, Vec3 fromVec, PoseStack poseStack, RenderSink sink, Vec3 to) {
        poseStack.pushPose();
        poseStack.translate(fromVec.x, fromVec.y, fromVec.z);
        float dx = (float) (to.x - fromVec.x);
        float dy = (float) (to.y - fromVec.y);
        float dz = (float) (to.z - fromVec.z);
        int blockLightTo = from.level().getBrightness(LightLayer.BLOCK, BlockPos.containing(to));
        int skyLightFrom = from.level().getBrightness(LightLayer.SKY, BlockPos.containing(fromVec));
        int skyLightTo = from.level().getBrightness(LightLayer.SKY, BlockPos.containing(to));
        //? if >=26.1 {
        /*sink.draw(poseStack, RenderType.leash(), (stack, consumer) -> {
        *///?} else {
        sink.draw(poseStack, RenderType.leash(), (stack, consumer) -> {
        //?}
            Matrix4f matrix = stack.last().pose();
            for (int i = 0; i <= 8; ++i) {
                float width = 0.05F - (i / 8F) * 0.025F;
                addStringVertexPair(consumer, matrix, dx, dy, dz, 0, blockLightTo, skyLightFrom, skyLightTo, 0.2F, width, width, i);
            }
        });
        poseStack.popPose();
    }

    private static void addStringVertexPair(VertexConsumer consumer, Matrix4f matrix, float dx, float dy, float dz, int blockFrom, int blockTo, int skyFrom, int skyTo, float height, float halfWidth, float depth, int segment) {
        float f = (float) segment / 8.0F;
        int block = (int) Mth.lerp(f, (float) blockFrom, (float) blockTo);
        int sky = (int) Mth.lerp(f, (float) skyFrom, (float) skyTo);
        //? if >=26.1 {
        /*int light = LightCoordsUtil.pack(block, sky);
        *///?} else {
        int light = LightTexture.pack(block, sky);
        //?}
        float x = dx * f;
        float y = dy < 0.0F ? dy * f * f : dy - dy * (1.0F - f) * (1.0F - f);
        float z = dz * f;
        consumer.addVertex(matrix, x - halfWidth, y + depth, z + halfWidth).setColor(0F, 0F, 0F, 1.0F).setLight(light);
        consumer.addVertex(matrix, x + halfWidth, y + height - depth, z - halfWidth).setColor(0F, 0F, 0F, 1.0F).setLight(light);
    }

    private static void auraVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, int y, int u, int v) {
        consumer.addVertex(pose.pose(), x - 0.5F, (float) y - 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    private float getPunchFor(LivingEntity living, int i, float partialTicks) {
        int[] arr = TameableUtils.getShadowPunchTimes(living);
        if (arr.length > i) {
            if (RenderData.shadowPunchRenderData.containsKey(living) && RenderData.shadowPunchRenderData.get(living).length > i) {
                int[] prevArr = RenderData.shadowPunchRenderData.get(living);
                return prevArr[i] + (arr[i] - prevArr[i]) * partialTicks;
            }
            return arr[i];
        }
        return 0F;
    }

    private static void renderVoidCloudCube(PoseStack poseStack, VertexConsumer consumer) {
        Matrix4f cube = poseStack.last().pose();
        voidCloudFace(cube, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        voidCloudFace(cube, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        voidCloudFace(cube, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        voidCloudFace(cube, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        voidCloudFace(cube, consumer, 0.0F, 1.0F, 0, 0, 0.0F, 0.0F, 1.0F, 1.0F);
        voidCloudFace(cube, consumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F);
    }

    private static void voidCloudFace(Matrix4f matrix, VertexConsumer consumer, float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3) {
        consumer.addVertex(matrix, x0, y0, z0);
        consumer.addVertex(matrix, x1, y0, z1);
        consumer.addVertex(matrix, x1, y1, z2);
        consumer.addVertex(matrix, x0, y1, z3);
    }
}
