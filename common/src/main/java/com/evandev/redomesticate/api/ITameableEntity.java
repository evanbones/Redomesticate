package com.evandev.redomesticate.api;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ITameableEntity {
    boolean redomesticate$isTame();

    void redomesticate$setTame(boolean value);

    @Nullable
    UUID redomesticate$getTameOwnerUUID();

    void redomesticate$setTameOwnerUUID(@Nullable UUID uuid);

    @Nullable
    LivingEntity redomesticate$getTameOwner();
}