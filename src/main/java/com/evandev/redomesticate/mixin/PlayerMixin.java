package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.util.TameableUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {

    @WrapOperation(
            //? if >=26.1 {
            /*method = "doSweepAttack*", // NeoForge adds an overload that holds the actual sweep logic
            *///?} else {
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            //?}
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    private boolean wrapIsAlliedTo(Player instance, Entity entity, Operation<Boolean> original) {
        return TameableUtils.isPetOf(instance, entity) || original.call(instance, entity);
    }
}
