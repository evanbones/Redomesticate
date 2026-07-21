package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.config.ModConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Parrot.class)
public abstract class ParrotMixin extends TamableAnimal {

    protected ParrotMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = "mobInteract",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Parrot;setOrderedToSit(Z)V"),
            cancellable = true
    )
    private void redomesticate$interceptParrotSitToggle(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Parrot parrot = (Parrot) (Object) this;

        if (ModConfig.get().trinaryCommandSystem && !player.isShiftKeyDown() && parrot.isTame() && parrot.isOwnedBy(player) && this instanceof ICommandableMob commandable) {

            if (!parrot.level().isClientSide) {
                parrot.setTarget(null);
                parrot.getNavigation().stop();
                commandable.playerSetCommand(player, parrot);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(parrot.level().isClientSide));
        }
    }
}