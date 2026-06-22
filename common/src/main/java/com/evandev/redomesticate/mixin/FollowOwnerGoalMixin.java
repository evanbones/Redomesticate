package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.registry.ModTags;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FollowOwnerGoal.class)
public abstract class FollowOwnerGoalMixin extends Goal {

    @Shadow
    @Final
    private TamableAnimal tamable;

    @Shadow
    private LivingEntity owner;

    @Shadow
    @Final
    private double speedModifier;

    @Inject(
            at = @At("HEAD"),
            method = "canUse()Z",
            cancellable = true
    )
    private void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (tamable instanceof ICommandableMob commandableMob && !commandableMob.redomesticate$isFollowingOwner() && ModConfig.get().trinaryCommandSystem) {
            if (this.tamable.getType().is(ModTags.COMMAND_WHITELIST)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "canContinueToUse()Z",
            cancellable = true
    )
    private void canContinueToUse(CallbackInfoReturnable<Boolean> cir) {
        if (tamable instanceof ICommandableMob commandableMob && !commandableMob.redomesticate$isFollowingOwner() && ModConfig.get().trinaryCommandSystem) {
            if (this.tamable.getType().is(ModTags.COMMAND_WHITELIST)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "tick()V",
            cancellable = true
    )
    private void tick(CallbackInfo ci) {
        double maxDist = ModConfig.get().disablePetTeleportation ? Double.MAX_VALUE : 144.0D;
        if (TameableUtils.hasEnchant(tamable, ModEnchantments.AMPHIBIOUS) && tamable.isInWaterOrBubble() && this.tamable.distanceToSqr(this.owner) < maxDist) {
            tamable.getNavigation().moveTo(owner, speedModifier);
            ci.cancel();
        }
    }
}