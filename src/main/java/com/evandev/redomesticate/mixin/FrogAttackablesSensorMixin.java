package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.FrogAttackablesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >=26.1 {
/*import net.minecraft.server.level.ServerLevel;
*///?}

@Mixin(FrogAttackablesSensor.class)
public class FrogAttackablesSensorMixin {

    //? if >=26.1 {
    /*@Inject(
            method = "isMatchingEntity(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void isHuntTarget(ServerLevel level, LivingEntity frog, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
    @Inject(
            method = "isMatchingEntity(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void isHuntTarget(LivingEntity frog, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
    //?}
        if (frog instanceof ITameableEntity tamed && frog instanceof ICommandableMob commandable && tamed.redomesticate$getTameOwner() != null) {
            if (!commandable.redomesticate$isStayingStill()) {
                if (livingEntity.getUUID().equals(tamed.redomesticate$getTameOwnerUUID())
                        || TameableUtils.hasSameOwnerAs(frog, livingEntity)
                        || frog.getType() == livingEntity.getType()) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}