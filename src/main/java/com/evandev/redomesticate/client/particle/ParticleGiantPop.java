package com.evandev.redomesticate.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class ParticleGiantPop extends SimpleAnimatedParticle {

    private ParticleGiantPop(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize = 1F;
        this.lifetime = 4;
        this.gravity = 0F;
        this.pickSprite(sprites);
    }

    public void tick() {
        super.tick();
        this.quadSize = 1.5F + 0.5F * (age / (float) lifetime);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleGiantPop p = new ParticleGiantPop(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
            return p;
        }
    }
}
