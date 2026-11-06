package com.evandev.redomesticate.data.request;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.UUID;

public class LanternRequest {
    private final String entityType;
    private final long timestamp;
    private final String nametag;

    private final UUID petUUID;
    private final UUID ownerUUID;

    private final BlockPos chunkPosition;
    private boolean chunksForceLoaded = false;
    private int loadTimeout = 0;

    public LanternRequest(UUID petUUID, String entityType, UUID ownerUUID, BlockPos chunkPosition, long timestamp, String nametag) {
        this.petUUID = petUUID;
        this.entityType = entityType;
        this.chunkPosition = chunkPosition;
        this.ownerUUID = ownerUUID;
        this.timestamp = timestamp;
        this.nametag = nametag;
    }

    public UUID getPetUUID() {
        return petUUID;
    }

    public String getEntityTypeLoc() {
        return this.entityType;
    }

    public EntityType getEntityType() {
        return BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(this.entityType));
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getNametag() {
        return this.nametag;
    }

    public BlockPos getChunkPosition() {
        return chunkPosition;
    }

    public String toString() {
        if (getNametag() == null || getNametag().isEmpty()) {
            return this.entityType;
        } else {
            return getNametag() + "|" + this.entityType;
        }
    }

    public boolean areChunksLoaded() {
        return this.chunksForceLoaded;
    }

    public void setChunksLoaded(boolean state) {
        this.chunksForceLoaded = state;
    }

    public int getLoadTimeout() {
        return this.loadTimeout;
    }

    public void incrementLoadTimeout() {
        this.loadTimeout++;
    }

    public void resetLoadTimeout() {
        this.loadTimeout = 0;
    }
}
