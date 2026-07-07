package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.AxolotlAttackablesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxolotlAttackablesSensor.class)
public class AxolotlAttackablesSensorMixin {

    @Inject(
            method = "isMatchingEntity(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void isHuntTarget(LivingEntity axolotl, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (axolotl instanceof ITameableEntity tamed && axolotl instanceof ICommandableMob commandable && tamed.redomesticate$getTameOwner() != null) {
            if (!commandable.redomesticate$isStayingStill()) {
                if (livingEntity.getUUID().equals(tamed.redomesticate$getTameOwnerUUID())
                        || TameableUtils.hasSameOwnerAs(axolotl, livingEntity)
                        || axolotl.getType() == livingEntity.getType()) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}