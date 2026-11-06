package com.evandev.redomesticate.client.particle;

//? if >=26.1 {
/*import com.evandev.redomesticate.Constants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

/^*
 * A deflection shield icon that pops up where a projectile was deflected.
 ^/
public class ParticleDeflectionShield extends SingleQuadParticle {
    private static final ResourceLocation SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item/deflection_shield");
    private final float xRot;
    private final float yRot;
    private float renderScale;

    ParticleDeflectionShield(ClientLevel lvl, double x, double y, double z, float xRot, float yRot) {
        super(lvl, x, y, z, Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).getSprite(SPRITE));
        this.setSize(1, 1);
        this.gravity = 0.0F;
        this.lifetime = 15 + this.random.nextInt(7);
        this.xRot = xRot;
        this.yRot = yRot;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT_ITEMS;
    }

    @Override
    protected int getLightCoords(float partialTicks) {
        return LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.renderScale;
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTick) {
        float lerpAge = this.age + partialTick;
        float ageProgress = lerpAge / (float) this.lifetime;
        float fadeTime = 0.15F;
        float fadeIn = Math.min(ageProgress, fadeTime) / fadeTime;
        float fadeOut = Mth.clamp(ageProgress - (1F - fadeTime), 0F, fadeTime) / fadeTime;
        float up = -1F + fadeIn + (float) Math.sin(lerpAge * 0.3F) * 0.03F;
        float down = -1F * fadeOut;
        this.renderScale = (fadeIn - fadeOut) * 1.85F * 0.25F;
        Vec3 cam = camera.position();
        float x = (float) (Mth.lerp(partialTick, this.xo, this.x) - cam.x());
        float y = (float) (Mth.lerp(partialTick, this.yo, this.y) - cam.y()) + up + down + 0.5F;
        float z = (float) (Mth.lerp(partialTick, this.zo, this.z) - cam.z());
        Quaternionf rotation = new Quaternionf().rotateY(this.yRot * Mth.DEG_TO_RAD).rotateX(-this.xRot * Mth.DEG_TO_RAD);
        this.extractRotatedQuad(state, rotation, x, y, z, partialTick);
        this.extractRotatedQuad(state, new Quaternionf(rotation).rotateY((float) Math.PI), x, y, z, partialTick);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleDeflectionShield(worldIn, x, y, z, (float) xSpeed, (float) ySpeed);
        }
    }
}
*///?} else {
import com.evandev.redomesticate.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ParticleDeflectionShield extends Particle {
    private float xRot;
    private float yRot;

    ParticleDeflectionShield(ClientLevel lvl, double x, double y, double z, float xRot, float yRot) {
        super(lvl, x, y, z);
        this.setSize(1, 1);
        this.gravity = 0.0F;
        this.lifetime = 15 + new Random().nextInt(7);
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        PoseStack posestack = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        float f = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());
        float lerpAge = this.age + partialTick;
        float ageProgress = lerpAge / (float) this.lifetime;
        float fadeTime = 0.15F;
        float fadeIn = Math.min(ageProgress, fadeTime) / fadeTime;
        float fadeOut = Mth.clamp(ageProgress - (1F - fadeTime), 0F, fadeTime) / fadeTime;
        float up = -1F + fadeIn + (float) Math.sin(lerpAge * 0.3F) * 0.03F;
        float down = -1F * fadeOut;
        float scale = (fadeIn - fadeOut) * 1.85F;
        posestack.pushPose();
        posestack.translate(f, f1 + up + down, f2);
        posestack.mulPose(Axis.YP.rotationDegrees(yRot));
        posestack.mulPose(Axis.XN.rotationDegrees(xRot));
        posestack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(ModItems.DEFLECTION_SHIELD.get()), ItemDisplayContext.GROUND, 240, OverlayTexture.NO_OVERLAY, posestack, multibuffersource$buffersource, level, 0);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleDeflectionShield(worldIn, x, y, z, (float) xSpeed, (float) ySpeed);
        }
    }
}
//?}
