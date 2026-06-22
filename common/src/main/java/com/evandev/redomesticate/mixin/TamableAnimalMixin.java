package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.PetCommand;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModTags;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin {

    @Inject(method = "setOrderedToSit", at = @At("HEAD"), cancellable = true)
    private void redomesticate$lockSitStateToCommand(boolean pOrderedToSit, CallbackInfo ci) {
        TamableAnimal tamable = (TamableAnimal) (Object) this;

        if (ModConfig.get().trinaryCommandSystem && tamable.isTame() && this instanceof ICommandableMob commandable && tamable.getType().is(ModTags.COMMAND_WHITELIST)) {
            boolean shouldSit = commandable.redomesticate$getPetCommand() == PetCommand.SIT;

            if (pOrderedToSit != shouldSit) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "shouldTryTeleportToOwner", at = @At("HEAD"), cancellable = true)
    private void redomesticate$disableTeleportation(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().disablePetTeleportation) {
            cir.setReturnValue(false);
        }
    }
}