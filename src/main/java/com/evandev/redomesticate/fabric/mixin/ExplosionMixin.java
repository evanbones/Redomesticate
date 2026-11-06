package com.evandev.redomesticate.fabric.mixin;

//? if fabric {
import com.evandev.redomesticate.event.EventProxy;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//? if >=26.1 {
/*import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?} else {
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

//? if >=26.1 {
/*@Mixin(ServerExplosion.class)
public abstract class ExplosionMixin {

    @Shadow
    @Final
    private ServerLevel level;

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void redomesticate$onExplosion(CallbackInfoReturnable<Integer> cir) {
        if (EventProxy.onExplosion(this.level, (Explosion) this)) {
            cir.setReturnValue(0);
        }
    }
}
*///?} else {
@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Shadow
    @Final
    private Level level;

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void redomesticate$onExplosion(CallbackInfo ci) {
        if (EventProxy.onExplosion(this.level, (Explosion) (Object) this)) {
            ci.cancel();
        }
    }
}
//?}
//?}
