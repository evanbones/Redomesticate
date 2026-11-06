package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
//? if >=26.1 {
/*import net.minecraft.world.entity.EntityReference;
*///?}

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal implements ITameableEntity {

    @Shadow
    protected float playerJumpPendingScale;

    protected AbstractHorseMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Shadow
    public abstract boolean isTamed();

    @Shadow
    public abstract void setTamed(boolean tame);

    //? if >=26.1 {
    /*@Shadow
    private @Nullable EntityReference<LivingEntity> owner;
    *///?} else {
    @Shadow
    @Nullable
    public abstract UUID getOwnerUUID();

    @Shadow
    public abstract void setOwnerUUID(@Nullable UUID owner);
    //?}

    @Inject(
            method = "tickRidden(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tickRidden(Player rider, Vec3 vec3, CallbackInfo ci) {
        if (this.isAlive() && this.isVehicle() && this.isInWaterOrBubble() && TameableUtils.hasEnchant(this, ModEnchantments.AMPHIBIOUS)) {
            LivingEntity livingentity = this.getControllingPassenger();
            if (livingentity == null) return;

            this.setYRot(livingentity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(livingentity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
            float f = livingentity.xxa * 0.5F;
            float f1 = livingentity.zza;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }
            float down = 0.08F;
            if (playerJumpPendingScale != 0) {
                down -= playerJumpPendingScale;
                playerJumpPendingScale = 0;
            }
            //? if >=26.1 {
            /*if (this.isLocalInstanceAuthoritative()) {
            *///?} else {
            if (this.isControlledByLocalInstance()) {
            //?}
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                super.travel(new Vec3(f, vec3.y - down, f1));
            } else if (livingentity instanceof Player) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D).add(0, -down, 0));
            }
            this.calculateEntityAnimation(false);
            //? if >=26.1 {
            /*this.applyEffectsFromBlocks();
            *///?} else {
            this.tryCheckInsideBlocks();
            //?}
            ci.cancel();
        }
    }

    @Unique
    public boolean redomesticate$isTame() {
        return this.isTamed() && ModConfig.get().tameableHorse;
    }

    @Unique
    public void redomesticate$setTame(boolean tame) {
        this.setTamed(tame);
    }

    //? if >=26.1 {
    /*@Unique
    public UUID redomesticate$getTameOwnerUUID() {
        return this.owner == null ? null : this.owner.getUUID();
    }

    @Unique
    public void redomesticate$setTameOwnerUUID(@Nullable UUID uuid) {
        this.owner = uuid == null ? null : EntityReference.of(uuid);
    }
    *///?} else {
    @Unique
    public UUID redomesticate$getTameOwnerUUID() {
        return this.getOwnerUUID();
    }

    @Unique
    public void redomesticate$setTameOwnerUUID(@Nullable UUID uuid) {
        this.setOwnerUUID(uuid);
    }
    //?}

    @Unique
    @Nullable
    public LivingEntity redomesticate$getTameOwner() {
        try {
            UUID uuid = this.redomesticate$getTameOwnerUUID();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Unique
    public boolean redomesticate$isFollowingOwner() {
        return false;
    }

    @Unique
    public boolean redomesticate$isStayingStill() {
        return false;
    }

    @Unique
    public boolean redomesticate$isValidAttackTarget(LivingEntity target) {
        return false;
    }
}