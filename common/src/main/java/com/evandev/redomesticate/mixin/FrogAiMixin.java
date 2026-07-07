package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.content.entity.ai.AmphibianFollowOwnerBehavior;
import com.evandev.redomesticate.content.entity.ai.AmphibianStayBehavior;
import com.evandev.redomesticate.registry.ModActivities;
import com.evandev.redomesticate.registry.ModTags;
import com.evandev.redomesticate.util.TameableUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogAi;
import net.minecraft.world.entity.animal.frog.ShootTongue;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(FrogAi.class)
public class FrogAiMixin {

    @Inject(
            method = "makeBrain(Lnet/minecraft/world/entity/ai/Brain;)Lnet/minecraft/world/entity/ai/Brain;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/frog/FrogAi;initJumpActivity(Lnet/minecraft/world/entity/ai/Brain;)V"
            )
    )
    private static void makeBrain(Brain<Frog> brain, CallbackInfoReturnable<Brain<?>> cir) {
        brain.addActivity(ModActivities.FROG_FOLLOW.get(), ImmutableList.of(Pair.of(0, new AmphibianFollowOwnerBehavior(1.25F, 1.0F)), Pair.of(2, new ShootTongue(SoundEvents.FROG_TONGUE, SoundEvents.FROG_EAT))));
        brain.addActivity(ModActivities.FROG_STAY.get(), ImmutableList.of(Pair.of(0, new AmphibianStayBehavior())));
    }

    @Inject(
            method = "updateActivity(Lnet/minecraft/world/entity/animal/frog/Frog;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void updateActivity(Frog frog, CallbackInfo ci) {
        Brain<Frog> brain = frog.getBrain();
        if (frog instanceof ICommandableMob commandableMob) {
            if (commandableMob.redomesticate$isStayingStill()) {
                brain.setActiveActivityIfPossible(ModActivities.FROG_STAY.get());
                ci.cancel();
            } else if (commandableMob.redomesticate$isFollowingOwner()) {
                if (frog.getTarget() != null && frog.getTarget().isAlive()) {
                    if (TameableUtils.hasSameOwnerAs(frog, frog.getTarget())) {
                        frog.setTarget(null);
                        brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
                        brain.eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE);
                        frog.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ModActivities.FROG_FOLLOW.get(), Activity.LAY_SPAWN, Activity.LONG_JUMP, Activity.SWIM, Activity.IDLE));
                    } else {
                        brain.setMemory(MemoryModuleType.ATTACK_TARGET, frog.getTarget());
                        brain.setMemory(MemoryModuleType.NEAREST_ATTACKABLE, frog.getTarget());
                        brain.setActiveActivityIfPossible(Activity.TONGUE);
                    }
                } else {
                    frog.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ModActivities.FROG_FOLLOW.get(), Activity.TONGUE, Activity.LAY_SPAWN, Activity.LONG_JUMP, Activity.SWIM, Activity.IDLE));
                }
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "getTemptations()Ljava/util/function/Predicate;",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void getTemptationItems(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Predicate<ItemStack> original = cir.getReturnValue();
        cir.setReturnValue(stack -> original.test(stack) || stack.is(ModTags.TAME_FROGS_WITH));
    }
}
