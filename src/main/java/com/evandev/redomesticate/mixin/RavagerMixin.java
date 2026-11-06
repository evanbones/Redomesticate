package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.config.ModConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ravager.class)
public abstract class RavagerMixin extends Monster {

    protected RavagerMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals()V", at = @At("TAIL"))
    private void redomesticate$addRabbitAvoidGoal(CallbackInfo ci) {
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(
                (Ravager) (Object) this,
                Rabbit.class,
                8.0F,
                1.0D,
                1.2D,
                (entity) -> ModConfig.get().rabbitsScareRavagers
        ));
    }
}