package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.registry.ModItems;
import com.evandev.redomesticate.content.entity.FeatherEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {

    protected FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"
            )
    )
    private FluidState wrapGetFluidState(Level instance, BlockPos pos, Operation<FluidState> original) {
        if ((Projectile) this instanceof FeatherEntity) {
            return Fluids.EMPTY.defaultFluidState();
        }
        return original.call(instance, pos);
    }

    @Inject(
            method = "shouldStopFishing(Lnet/minecraft/world/entity/player/Player;)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void shouldStopFishing(Player player, CallbackInfoReturnable<Boolean> cir) {
        if ((Projectile) this instanceof FeatherEntity) {
            ItemStack itemstack = player.getMainHandItem();
            ItemStack itemstack1 = player.getOffhandItem();
            boolean flag = itemstack.is(ModItems.FEATHER_ON_A_STICK.get());
            boolean flag1 = itemstack1.is(ModItems.FEATHER_ON_A_STICK.get());
            if (!this.isRemoved() && this.isAlive() && (flag || flag1) && this.distanceToSqr(player) < 1024.0D) {
                cir.setReturnValue(false);
            } else {
                this.discard();
                cir.setReturnValue(true);
            }
        }
    }
}
