package com.evandev.redomesticate.fabric.mixin;

//? if fabric {
import com.evandev.redomesticate.event.EventProxy;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class FabricLivingEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void redomesticate$onLivingTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.level().isClientSide()) {
            EventProxy.onLivingUpdate(entity);
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void redomesticate$onLivingDrops(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        if (EventProxy.onLivingDrops((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }

    //? if >=26.3 {
    /*@Inject(method = "randomTeleport(DDDZLjava/util/function/Predicate;)Z", at = @At("HEAD"))
    *///?} else {
    @Inject(method = "randomTeleport(DDDZ)Z", at = @At("HEAD"))
    //?}
    //? if >=26.3 {
    /*private void redomesticate$onRandomTeleport(double x, double y, double z, boolean broadcastTeleport, java.util.function.Predicate<?> invalidPosition, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
    private void redomesticate$onRandomTeleport(double x, double y, double z, boolean broadcastTeleport, CallbackInfoReturnable<Boolean> cir) {
    //?}
        LivingEntity entity = (LivingEntity) (Object) this;
        EventProxy.onEntityTeleport(entity, entity.position(), new Vec3(x, y, z));
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void redomesticate$onDie(DamageSource damageSource, CallbackInfo ci) {
        EventProxy.onLivingDie((LivingEntity) (Object) this, damageSource);
    }

    //? if >=26.1 {
    /*@Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void redomesticate$onLivingDamagePre(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void redomesticate$onLivingDamagePre(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    //?}
        LivingEntity entity = (LivingEntity) (Object) this;

        if (EventProxy.onTameHurt(entity, source) || EventProxy.onLivingDamage(entity, source, amount)) {
            cir.setReturnValue(false);
        }
    }

    //? if >=26.1 {
    /*@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float redomesticate$modifyDamageAmount(float amount, ServerLevel level, DamageSource source) {
    *///?} else {
    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float redomesticate$modifyDamageAmount(float amount, DamageSource source) {
    //?}
        return EventProxy.onLivingDamageModifier((LivingEntity) (Object) this, source, amount);
    }

    //? if >=26.1 {
    /*@Inject(method = "actuallyHurt", at = @At("TAIL"))
    private void redomesticate$onActuallyHurt(ServerLevel level, DamageSource source, float damageAmount, CallbackInfo ci) {
    *///?} else {
    @Inject(method = "actuallyHurt", at = @At("TAIL"))
    private void redomesticate$onActuallyHurt(DamageSource source, float damageAmount, CallbackInfo ci) {
    //?}
        EventProxy.onEntityHurt((LivingEntity) (Object) this, source, damageAmount, damageAmount);
    }
}
//?}
