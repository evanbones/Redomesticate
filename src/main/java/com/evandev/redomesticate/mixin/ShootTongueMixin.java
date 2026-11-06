package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ITameableEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.ShootTongue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ShootTongue.class)
public class ShootTongueMixin {

    @Inject(
            method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/frog/Frog;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/frog/Frog;canEat(Lnet/minecraft/world/entity/LivingEntity;)Z"
            ),
            cancellable = true
    )
    private void checkExtraStartConditions(ServerLevel level, Frog frog, CallbackInfoReturnable<Boolean> cir) {
        if (frog instanceof ITameableEntity tameable && tameable.redomesticate$isTame()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "eatEntity(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/frog/Frog;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void eatEntity(ServerLevel level, Frog frog, CallbackInfo ci) {
        if (frog instanceof ITameableEntity tameable && tameable.redomesticate$isTame()) {
            ci.cancel();
            frog.playSound(SoundEvents.FROG_TONGUE);
            Optional<Entity> optional = frog.getTongueTarget();
            if (optional.isPresent()) {
                Entity entity = optional.get();
                if (entity.isAlive()) {
                    //? if >=26.1 {
                    /*frog.doHurtTarget(level, entity);
                    *///?} else {
                    frog.doHurtTarget(entity);
                    //?}
                    if (!entity.isAlive() && Frog.canEat(frog)) {
                        entity.remove(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }
}
