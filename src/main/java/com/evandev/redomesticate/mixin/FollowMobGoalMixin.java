package com.evandev.redomesticate.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FollowMobGoal.class)
public class FollowMobGoalMixin {

    @Shadow
    @Final
    private Mob mob;

    @Inject(method = {"canUse()Z", "canContinueToUse()Z"}, at = @At("HEAD"), cancellable = true)
    private void redomesticate$stayWhenOrderedToSit(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof TamableAnimal tamable && tamable.isOrderedToSit()) {
            cir.setReturnValue(false);
        }
    }
}
