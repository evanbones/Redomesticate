package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.config.ModConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal {
    protected WolfMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Inject(
            at = @At("HEAD"),
            method = "getTailAngle",
            cancellable = true
    )
    private void getTailAngle(CallbackInfoReturnable<Float> cir) {
        if (!((NeutralMob) this).isAngry() && this.isTame()) {
            float f = (this.getMaxHealth() - this.getHealth()) / this.getMaxHealth() * 20F;
            cir.setReturnValue((0.55F - Math.max(f * 0.02F, 0F)) * (float) Math.PI);
        }
    }

    @Inject(
            method = "mobInteract",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Wolf;setOrderedToSit(Z)V"),
            cancellable = true
    )
    private void redomesticate$interceptWolfSitToggle(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Wolf wolf = (Wolf) (Object) this;

        if (ModConfig.get().trinaryCommandSystem && wolf.isTame() && wolf.isOwnedBy(player) && this instanceof ICommandableMob commandable) {
            if (!wolf.level().isClientSide) {
                wolf.setTarget(null);
                wolf.getNavigation().stop();
                commandable.playerSetCommand(player, wolf);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(wolf.level().isClientSide));
        }
    }
}
