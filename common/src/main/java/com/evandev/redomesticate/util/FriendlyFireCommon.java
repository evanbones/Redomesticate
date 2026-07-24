package com.evandev.redomesticate.util;

import com.evandev.redomesticate.config.ModConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FriendlyFireCommon {

    public static boolean preventAttack(Entity target, DamageSource source, float amount) {
        return isProtected(target, source.getEntity());
    }

    public static boolean isProtected(Entity victim, Entity attacker) {
        if (!ModConfig.get().swingThroughPets) {
            return false;
        }

        if (attacker == null) {
            return false;
        }

        if (attacker instanceof Player player && player.isShiftKeyDown()) {
            return false;
        }

        final UUID ownerId = getOwner(victim);

        return ownerId != null && ownerId.equals(attacker.getUUID());
    }

    @Nullable
    public static UUID getOwner(Entity entity) {
        UUID owner = TameableUtils.getOwnerUUIDOf(entity);
        if (owner != null) {
            return owner;
        }

        if (entity instanceof AbstractHorse horse) {
            return horse.getOwnerUUID();
        }

        return null;
    }
}