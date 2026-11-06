package com.evandev.redomesticate.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IPetbedDataEntity {
    Map<ResourceLocation, Integer> redomesticate$getCachedEnchants();

    void redomesticate$setCachedEnchants(Map<ResourceLocation, Integer> enchants);

    CompoundTag redomesticate$getEntityData();

    void redomesticate$setEntityData(CompoundTag nbt);
}
