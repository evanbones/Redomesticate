package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.IFrog;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Frog.class)
public abstract class FrogMixin extends Animal implements IFrog {

    @Unique
    private boolean redomesticate$hasInitialDamage = false;

    protected FrogMixin(EntityType<? extends Animal> type, Level lvl) {
        super(type, lvl);
    }

    @Shadow
    public abstract @NotNull Brain<Frog> getBrain();

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!redomesticate$hasInitialDamage && ((ITameableEntity) this).redomesticate$isTame()) {
            redomesticate$hasInitialDamage = true;
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        }
    }

    @Inject(at = @At("TAIL"), method = "customServerAiStep")
    private void customServerAiStep(CallbackInfo ci) {
        ITameableEntity tameable = (ITameableEntity) this;
        LivingEntity owner = tameable.redomesticate$getTameOwner();

        if (tameable.redomesticate$isTame() && owner != null) {
            if (owner.getLastHurtMob() != null && owner.getLastHurtMob().isAlive() && !TameableUtils.hasSameOwnerAs(this, owner.getLastHurtMob())) {
                this.setTarget(owner.getLastHurtMob());
                this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, owner.getLastHurtMob());
            }
            if (owner.getLastHurtByMob() != null && owner.getLastHurtByMob().isAlive() && !TameableUtils.hasSameOwnerAs(this, owner.getLastHurtByMob())) {
                this.setTarget(owner.getLastHurtByMob());
                this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, owner.getLastHurtByMob());
            }
        }
    }
}