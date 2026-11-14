package com.evandev.redomesticate.mixin;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomStrollGoal.class)
public class RandomStrollGoalMixin {

    @Shadow
    @Final
    protected PathfinderMob mob;

    @Inject(method = {"canUse()Z", "canContinueToUse()Z"}, at = @At("HEAD"), cancellable = true)
    private void redomesticate$stayWhenOrderedToSit(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof TamableAnimal tamable && tamable.isOrderedToSit()) {
            cir.setReturnValue(false);
        }
    }
}
