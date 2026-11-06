package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.registry.ModActivities;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.content.entity.ai.AmphibianFollowOwnerBehavior;
import com.evandev.redomesticate.content.entity.ai.AmphibianStayBehavior;
import com.evandev.redomesticate.util.TameableUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.AxolotlAi;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;
//? if >=26.1 {
/*import net.minecraft.world.entity.ai.ActivityData;
import java.util.ArrayList;
import java.util.List;
*///?}

@Mixin(AxolotlAi.class)
public class AxolotlAiMixin {

    //? if >=26.1 {
    /*@Inject(method = "getActivities", at = @At("RETURN"), cancellable = true)
    private static void addPetActivities(CallbackInfoReturnable<List<ActivityData<Axolotl>>> cir) {
        List<ActivityData<Axolotl>> activities = new ArrayList<>(cir.getReturnValue());
        activities.add(ActivityData.create(ModActivities.AXOLOTL_FOLLOW.get(), ImmutableList.of(Pair.of(0, new AmphibianFollowOwnerBehavior(0.3F, 0.6F)))));
        activities.add(ActivityData.create(ModActivities.AXOLOTL_STAY.get(), ImmutableList.of(Pair.of(0, new AmphibianStayBehavior()))));
        cir.setReturnValue(activities);
    }
    *///?} else {
    @Inject(
            method = "makeBrain(Lnet/minecraft/world/entity/ai/Brain;)Lnet/minecraft/world/entity/ai/Brain;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/axolotl/AxolotlAi;initPlayDeadActivity(Lnet/minecraft/world/entity/ai/Brain;)V"
            )
    )
    private static void makeBrain(Brain<Axolotl> brain, CallbackInfoReturnable<Brain<?>> cir) {
        brain.addActivity(ModActivities.AXOLOTL_FOLLOW.get(), ImmutableList.of(Pair.of(0, new AmphibianFollowOwnerBehavior(0.3F, 0.6F))));
        brain.addActivity(ModActivities.AXOLOTL_STAY.get(), ImmutableList.of(Pair.of(0, new AmphibianStayBehavior())));
    }
    //?}

    @Inject(
            method = "updateActivity(Lnet/minecraft/world/entity/animal/axolotl/Axolotl;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void updateActivity(Axolotl axolotl, CallbackInfo ci) {
        Brain<Axolotl> brain = axolotl.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        if (activity != Activity.PLAY_DEAD && !axolotl.isPlayingDead() && axolotl instanceof ICommandableMob commandableMob) {
            if (commandableMob.redomesticate$isStayingStill()) {
                brain.setActiveActivityIfPossible(ModActivities.AXOLOTL_STAY.get());
                ci.cancel();
            } else if (commandableMob.redomesticate$isFollowingOwner()) {
                brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.PLAY_DEAD, Activity.FIGHT, ModActivities.AXOLOTL_FOLLOW.get()));
                ci.cancel();
            }
        }
    }

    //? if <26.1 {
    @Inject(
            method = "getTemptations()Ljava/util/function/Predicate;",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void getTemptationItems(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Predicate<ItemStack> original = cir.getReturnValue();
        cir.setReturnValue(stack -> original.test(stack) || stack.is(Items.TROPICAL_FISH_BUCKET) || stack.is(Items.TROPICAL_FISH));
    }
    //?}

    @Inject(
            method = "getSpeedModifierChasing(Lnet/minecraft/world/entity/LivingEntity;)F",
            remap = true,
            at = @At(
                    value = "TAIL"
            ),
            cancellable = true
    )
    private static void getSpeedModifierChasing(LivingEntity axolotl, CallbackInfoReturnable<Float> cir) {
        int speedsterLevel = TameableUtils.getEnchantLevel(axolotl, ModEnchantments.SPEEDSTER);
        cir.setReturnValue(axolotl.isInWaterOrBubble() ? 0.6F + speedsterLevel * 0.05F : 0.15F + speedsterLevel * 0.1F);
    }

    @Inject(
            method = "getSpeedModifierFollowingAdult(Lnet/minecraft/world/entity/LivingEntity;)F",
            at = @At(
                    value = "TAIL"
            ),
            cancellable = true
    )
    private static void getSpeedModifierFollowingAdult(LivingEntity axolotl, CallbackInfoReturnable<Float> cir) {
        int speedsterLevel = TameableUtils.getEnchantLevel(axolotl, ModEnchantments.SPEEDSTER);
        cir.setReturnValue(axolotl.isInWaterOrBubble() ? 0.6F + speedsterLevel * 0.05F : 0.15F + speedsterLevel * 0.1F);
    }

    @Inject(
            method = "getSpeedModifier(Lnet/minecraft/world/entity/LivingEntity;)F",
            at = @At(
                    value = "TAIL"
            ),
            cancellable = true
    )
    private static void getSpeedModifier(LivingEntity axolotl, CallbackInfoReturnable<Float> cir) {
        int speedsterLevel = TameableUtils.getEnchantLevel(axolotl, ModEnchantments.SPEEDSTER);
        cir.setReturnValue(axolotl.isInWaterOrBubble() ? 0.5F + speedsterLevel * 0.05F : 0.15F + speedsterLevel * 0.15F);
    }
}
