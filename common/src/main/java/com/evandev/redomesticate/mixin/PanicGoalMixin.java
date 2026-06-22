package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.registry.ModTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PanicGoal.class)
public class PanicGoalMixin {

    @Shadow
    @Final
    protected PathfinderMob mob;

    @Inject(
            at = @At("HEAD"),
            method = "canUse()Z",
            cancellable = true
    )
    private void di_canUse(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob.getType().is(ModTags.COMMAND_WHITELIST) && mob instanceof ITameableEntity tameableEntity && tameableEntity.redomesticate$isTame()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "canContinueToUse()Z",
            cancellable = true
    )
    private void di_canContinueToUse(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob.getType().is(ModTags.COMMAND_WHITELIST) && mob instanceof ITameableEntity tameableEntity && tameableEntity.redomesticate$isTame()) {
            cir.setReturnValue(false);
        }
    }
}