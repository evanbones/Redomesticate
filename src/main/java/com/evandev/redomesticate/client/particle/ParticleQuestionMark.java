package com.evandev.redomesticate.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class ParticleQuestionMark extends SimpleAnimatedParticle {
    protected ParticleQuestionMark(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.lifetime = 100;

        this.quadSize = 0.1F + this.random.nextFloat() * 0.1F;
        this.lifetime = 1 + this.random.nextInt(2);
        this.gravity = 0;
        this.pickSprite(sprites);

        this.hasPhysics = true;
    }

    public int getLightColor(float partialTicks) {
        return 240;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleQuestionMark(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}