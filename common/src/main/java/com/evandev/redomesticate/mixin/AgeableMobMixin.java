package com.evandev.redomesticate.mixin;


import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(AgeableMob.class)
public class AgeableMobMixin extends PathfinderMob {

    protected AgeableMobMixin(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @ModifyReturnValue(method = "isBaby()Z", at = @At("RETURN"))
    private boolean isBaby(boolean original) {
        return original || TameableUtils.isTamed(this) && TameableUtils.hasEnchant(this, ModEnchantments.IMMATURITY_CURSE);
    }
}
