package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.PetCommand;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModTags;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FollowParentGoal.class)
public class FollowParentGoalMixin {

    @Shadow
    @Final
    private Animal animal;

    @Inject(
            at = @At("HEAD"),
            method = "canUse()Z",
            cancellable = true
    )
    private void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (animal instanceof ICommandableMob commandableMob && commandableMob.redomesticate$getPetCommand() != PetCommand.WANDER && ModConfig.get().trinaryCommandSystem) {
            if (this.animal.getType().is(ModTags.COMMAND_WHITELIST)) {
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
        if (animal instanceof ICommandableMob commandableMob && commandableMob.redomesticate$getPetCommand() != PetCommand.WANDER && ModConfig.get().trinaryCommandSystem) {
            if (this.animal.getType().is(ModTags.COMMAND_WHITELIST)) {
                cir.setReturnValue(false);
            }
        }
    }
}