package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {
    protected CatMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = "mobInteract",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Cat;setOrderedToSit(Z)V"),
            cancellable = true
    )
    private void redomesticate$interceptCatSitToggle(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Cat cat = (Cat) (Object) this;

        if (ModConfig.get().trinaryCommandSystem && cat.getType().is(ModTags.COMMAND_WHITELIST) && cat.isTame() && cat.isOwnedBy(player) && this instanceof ICommandableMob commandable) {

            if (!cat.level().isClientSide) {
                cat.setTarget(null);
                cat.getNavigation().stop();
                commandable.playerSetCommand(player, cat);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(cat.level().isClientSide));
        }
    }
}
