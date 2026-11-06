package com.evandev.redomesticate.fabric.mixin;

//? if fabric {
import com.evandev.redomesticate.event.EventProxy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobTargetMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void redomesticate$onSetTarget(LivingEntity target, CallbackInfo ci) {
        if (EventProxy.onSetAttackTarget((Mob) (Object) this, target)) {
            ci.cancel();
        }
    }
}
//?}
