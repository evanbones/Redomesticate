package com.evandev.redomesticate.data.request;

import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class RespawnRequest {
    private final String entityType;
    private final String dimension;
    private final CompoundTag entityData;
    private final BlockPos bedPosition;
    private final long timestamp;
    private final String nametag;

    public RespawnRequest(String entityType, String dimension, CompoundTag entityData, BlockPos bedPosition, long timestamp, String nametag) {
        this.entityType = entityType;
        this.dimension = TameableUtils.normalizeDimension(dimension);
        this.entityData = entityData;
        this.bedPosition = bedPosition;
        this.timestamp = timestamp;
        this.nametag = nametag;
    }

    public String getEntityTypeLoc() {
        return this.entityType;
    }

    public EntityType getEntityType() {
        return BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(this.entityType));
    }

    public String getDimension() {
        return dimension;
    }

    public CompoundTag getEntityData() {
        return entityData;
    }

    public BlockPos getBedPosition() {
        return bedPosition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static String processDimension(ResourceKey<Level> key) {
        return key.location().toString();
    }

    public String getNametag() {
        return this.nametag;
    }
}
