package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.content.entity.ai.UniversalAIManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal {

    protected FoxMixin(EntityType<? extends Animal> foxType, Level level) {
        super(foxType, level);
    }

    @Shadow
    abstract List<UUID> getTrustedUUIDs();

    @WrapOperation(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack wrapFinishUsingItem(ItemStack instance, Level level, LivingEntity livingEntity, Operation<ItemStack> original) {
        FoodProperties food = instance.get(DataComponents.FOOD);
        if (!instance.isEmpty() && food != null) {
            this.heal(food.nutrition() * 2);
        }
        return original.call(instance, level, livingEntity);
    }

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void redomesticate$tameOnGrowUp(CallbackInfo ci) {
        if (!this.level().isClientSide() && ModConfig.get().tameableFox && !this.isBaby()) {
            ITameableEntity tameable = (ITameableEntity) this;

            if (!tameable.redomesticate$isTame()) {
                List<UUID> trusted = this.getTrustedUUIDs().stream().filter(Objects::nonNull).toList();
                if (!trusted.isEmpty()) {
                    tameable.redomesticate$setTame(true);
                    tameable.redomesticate$setTameOwnerUUID(trusted.getFirst());

                    this.redomesticate$removeUntamedGoals();
                    UniversalAIManager.applyPetAI(this);

                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "registerGoals()V")
    private void redomesticate$onRegisterGoals(CallbackInfo ci) {
        if (((ITameableEntity) this).redomesticate$isTame()) {
            redomesticate$removeUntamedGoals();
        }
    }

    @Unique
    public void redomesticate$removeUntamedGoals() {
        try {
            this.goalSelector.getAvailableGoals().stream().filter((wrapped) -> {
                String name = wrapped.getGoal().getClass().getSimpleName();
                return name.equals("AvoidEntityGoal") || name.equals("SleepGoal") || name.equals("SitAndLookDownGoal");
            }).filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);

            this.goalSelector.getAvailableGoals().removeIf((wrapped) -> {
                String name = wrapped.getGoal().getClass().getSimpleName();
                return name.equals("AvoidEntityGoal") || name.equals("SleepGoal") || name.equals("SitAndLookDownGoal");
            });
        } catch (Exception e) {
            Constants.LOG.warn("Encountered error modifying Fox AI", e);
        }
    }
}