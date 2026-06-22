package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.registry.ModTags;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetGoal.class)
public abstract class TargetGoalMixin {

    @Shadow
    @Final
    protected Mob mob;

    @Shadow
    @Nullable
    protected LivingEntity targetMob;

    @Inject(method = "canAttack", at = @At("HEAD"), cancellable = true)
    private void redomesticate_canAttack(@Nullable LivingEntity potentialTarget, TargetingConditions targetPredicate, CallbackInfoReturnable<Boolean> cir) {
        if (potentialTarget != null && this.mob.getType().is(ModTags.COMMAND_WHITELIST) && TameableUtils.couldBeTamed(this.mob) && !TameableUtils.wantsToAttack(this.mob, potentialTarget)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void redomesticate_canContinueToUse(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity currentTarget = this.mob.getTarget() != null ? this.mob.getTarget() : this.targetMob;

        if (currentTarget != null && this.mob.getType().is(ModTags.COMMAND_WHITELIST) && TameableUtils.couldBeTamed(this.mob) && !TameableUtils.wantsToAttack(this.mob, currentTarget)) {
            cir.setReturnValue(false);
        }
    }
}