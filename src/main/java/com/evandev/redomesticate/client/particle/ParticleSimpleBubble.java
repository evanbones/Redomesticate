package com.evandev.redomesticate.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class ParticleSimpleBubble extends SimpleAnimatedParticle {

    private ParticleSimpleBubble(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.xd = (float) motionX;
        this.yd = (float) motionY;
        this.zd = (float) motionZ;
        this.quadSize = 0.1F + this.random.nextFloat() * 0.1F;
        this.lifetime = 12 + this.random.nextInt(12);
        this.gravity = -0.08F;
        this.pickSprite(sprites);
    }

    public void tick() {
        super.tick();
        this.xd *= 0.8D;
        this.yd *= 0.8D;
        this.zd *= 0.8D;
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
            return new ParticleSimpleBubble(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}
