package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.content.entity.ai.goal.OwnerHurtByTarget2Goal;
import com.evandev.redomesticate.content.entity.ai.goal.OwnerHurtTarget2Goal;
import com.evandev.redomesticate.content.entity.ai.goal.RabbitMeleeGoal;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends Animal {

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_TYPE_ID;

    protected RabbitMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Inject(at = @At("TAIL"), method = "registerGoals()V")
    private void registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(Items.HAY_BLOCK), false));
        if (((ITameableEntity) this).redomesticate$isTame()) {
            redomesticate$removeUntamedGoals();
        }
    }

    @Unique
    public void redomesticate$removeUntamedGoals() {
        try {
            this.goalSelector.getAvailableGoals().stream().filter((wrapped) -> wrapped.getGoal() instanceof AvoidEntityGoal)
                    .filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
            this.goalSelector.getAvailableGoals().removeIf((wrapped) -> wrapped.getGoal() instanceof AvoidEntityGoal);
            this.targetSelector.getAvailableGoals().removeIf((wrapped) -> wrapped.getGoal() instanceof NearestAttackableTargetGoal);
        } catch (Exception e) {
            Constants.LOG.warn("Encountered error modifying Rabbit AI");
        }
    }

    @Inject(at = @At("HEAD"), method = "setVariant(Lnet/minecraft/world/entity/animal/Rabbit$Variant;)V", cancellable = true)
    private void setRabbitType(Rabbit.Variant type, CallbackInfo ci) {
        ci.cancel();
        if (type == Rabbit.Variant.EVIL) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
            this.getAttribute(Attributes.ARMOR).setBaseValue(8.0D);
            this.heal(22.0F);
            this.goalSelector.addGoal(4, new RabbitMeleeGoal((Rabbit) (Object) this));
            this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());

            if (!((ITameableEntity) this).redomesticate$isTame()) {
                this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
                this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Wolf.class, true));
            } else {
                this.targetSelector.addGoal(2, new OwnerHurtTarget2Goal(this));
                this.targetSelector.addGoal(3, new OwnerHurtByTarget2Goal(this));
                redomesticate$removeUntamedGoals();
            }
            if (!this.hasCustomName()) {
                this.setCustomName(Component.translatable(Util.makeDescriptionId("entity", ResourceLocation.withDefaultNamespace("killer_bunny"))));
            }
        }
        this.entityData.set(DATA_TYPE_ID, type.id());
    }
}