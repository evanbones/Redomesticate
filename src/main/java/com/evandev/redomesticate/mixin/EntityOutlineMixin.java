package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.client.event.OutlineColorCallback;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityOutlineMixin {

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void redomesticate$overrideOutlineColor(CallbackInfoReturnable<Integer> cir) {
        Integer customColor = OutlineColorCallback.invoke((Entity) (Object) this);
        if (customColor != null) {
            cir.setReturnValue(customColor);
        }
    }
}