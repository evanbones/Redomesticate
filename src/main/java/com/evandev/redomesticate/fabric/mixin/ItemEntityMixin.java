package com.evandev.redomesticate.fabric.mixin;

//? if fabric {
import com.evandev.redomesticate.event.EventProxy;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"))
    private void redomesticate$onItemDespawn(CallbackInfo ci) {
        EventProxy.onItemDespawnEvent((ItemEntity) (Object) this);
    }
}
//?}
