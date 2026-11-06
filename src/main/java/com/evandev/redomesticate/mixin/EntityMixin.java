package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.content.entity.PsychicWallEntity;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyReturnValue(method = "fireImmune()Z", at = @At("RETURN"))
    private boolean isFireImmune(boolean original) {
        if (original || !((Object) this instanceof LivingEntity livingThis)) return original;
        return TameableUtils.isTamed(livingThis) && TameableUtils.hasEnchant(livingThis, ModEnchantments.FIREPROOF);
    }

    @ModifyReturnValue(method = "isPushedByFluid()Z", at = @At("RETURN"))
    protected boolean pushedByWater(boolean original) {
        if (!original || !((Object) this instanceof LivingEntity livingThis)) return original;
        return !(TameableUtils.isTamed(livingThis) && TameableUtils.hasEnchant(livingThis, ModEnchantments.AMPHIBIOUS));
    }

    @ModifyReturnValue(method = "isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"))
    private boolean redomesticate$universalAlliances(boolean original, Entity other) {
        if (original || !((Object) this instanceof ITameableEntity tameable) || !tameable.redomesticate$isTame()) return original;
        if (other == tameable.redomesticate$getTameOwner()) return true;
        return (Object) this instanceof LivingEntity livingThis && other instanceof LivingEntity livingOther
                && TameableUtils.hasSameOwnerAs(livingThis, livingOther);
    }

    @ModifyReturnValue(method = "canCollideWith(Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"))
    protected boolean canCollideWith(boolean original, Entity other) {
        return original && !(other instanceof PsychicWallEntity wall && wall.isSameTeam((Entity) (Object) this));
    }

    @ModifyReturnValue(method = "dismountsUnderwater()Z", at = @At("RETURN"))
    protected boolean rideableInWater(boolean original) {
        if (original || !((Object) this instanceof LivingEntity livingThis)) return original;
        return TameableUtils.isTamed(livingThis) && TameableUtils.hasEnchant(livingThis, ModEnchantments.AMPHIBIOUS);
    }

    @ModifyReturnValue(method = "getMovementEmission()Lnet/minecraft/world/entity/Entity$MovementEmission;", at = @At("RETURN"))
    protected Entity.MovementEmission getMovementEmission(Entity.MovementEmission original) {
        if ((Object) this instanceof LivingEntity livingThis
                && TameableUtils.isTamed(livingThis) && TameableUtils.hasEnchant(livingThis, ModEnchantments.MUFFLED)) {
            return Entity.MovementEmission.NONE;
        }
        return original;
    }

    @Inject(method = "gameEvent(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    protected void gameEvent(Holder<GameEvent> gameEvent, Entity entity, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity livingThis) {
            if (TameableUtils.isTamed(livingThis) && TameableUtils.hasEnchant(livingThis, ModEnchantments.MUFFLED)) {
                ci.cancel();
            }
        }
    }
}