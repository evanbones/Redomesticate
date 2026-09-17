package com.evandev.redomesticate.data;

import com.evandev.redomesticate.data.request.LanternRequest;
import com.evandev.redomesticate.data.request.RespawnRequest;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModWorldData extends SavedData {

    private static final String IDENTIFIER = "redomesticate_world_data";
    private final List<RespawnRequest> respawnRequestList = new ArrayList<>();
    private final List<LanternRequest> lanternRequestList = new ArrayList<>();

    private ModWorldData() {
        super();
    }

    public static ModWorldData create() {
        return new ModWorldData();
    }

    public static ModWorldData get(Level world) {
        if (world instanceof ServerLevel serverLevel) {
            ServerLevel overworld = serverLevel.getServer().getLevel(Level.OVERWORLD);
            if (overworld != null) {
                DimensionDataStorage storage = overworld.getDataStorage();
                ModWorldData data = storage.computeIfAbsent(new Factory<>(ModWorldData::create, ModWorldData::load, DataFixTypes.SAVED_DATA_COMMAND_STORAGE), IDENTIFIER);
                return data;
            }
        }
        return null;
    }

    public static ModWorldData load(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
        ModWorldData data = new ModWorldData();
        if (nbt.contains("RespawnList")) {
            ListTag listtag = nbt.getList("RespawnList", 10);
            for (int i = 0; i < listtag.size(); ++i) {
                CompoundTag innerTag = listtag.getCompound(i);
                data.respawnRequestList.add(new RespawnRequest(innerTag.getString("EntityType"), innerTag.getString("DimensionIn"), innerTag.getCompound("EntityData"),
                        new BlockPos(innerTag.getInt("X"), innerTag.getInt("Y"), innerTag.getInt("Z")), innerTag.getLong("Timestamp"), innerTag.getString("EntityNametag")));
            }
        }
        if (nbt.contains("LanternList")) {
            ListTag listtag = nbt.getList("LanternList", 10);
            for (int i = 0; i < listtag.size(); ++i) {
                CompoundTag innerTag = listtag.getCompound(i);
                data.lanternRequestList.add(new LanternRequest(innerTag.getUUID("PetUUID"), innerTag.getString("EntityType"), innerTag.getUUID("OwnerUUID"), new BlockPos(innerTag.getInt("X"), innerTag.getInt("Y"), innerTag.getInt("Z")), innerTag.getLong("Timestamp"), innerTag.getString("EntityNametag")));
            }
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider provider) {
        if (!this.respawnRequestList.isEmpty()) {
            ListTag listTag = new ListTag();
            for (RespawnRequest request : respawnRequestList) {
                CompoundTag tag = new CompoundTag();
                tag.putString("EntityType", request.getEntityTypeLoc());
                tag.putString("DimensionIn", request.getDimension());
                tag.put("EntityData", request.getEntityData());
                tag.putInt("X", request.getBedPosition().getX());
                tag.putInt("Y", request.getBedPosition().getY());
                tag.putInt("Z", request.getBedPosition().getZ());
                tag.putLong("Timestamp", request.getTimestamp());
                tag.putString("EntityNametag", request.getNametag());
                listTag.add(tag);
            }
            compound.put("RespawnList", listTag);
        }
        if (!this.lanternRequestList.isEmpty()) {
            ListTag listTag = new ListTag();
            for (LanternRequest request : lanternRequestList) {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("PetUUID", request.getPetUUID());
                tag.putString("EntityType", request.getEntityTypeLoc());
                tag.putUUID("OwnerUUID", request.getOwnerUUID());
                tag.putLong("Timestamp", request.getTimestamp());
                tag.putString("EntityNametag", request.getNametag());
                tag.putInt("X", request.getChunkPosition().getX());
                tag.putInt("Y", request.getChunkPosition().getY());
                tag.putInt("Z", request.getChunkPosition().getZ());
                listTag.add(tag);
            }
            compound.put("LanternList", listTag);
        }
        return compound;
    }

    public void addRespawnRequest(RespawnRequest request) {
        this.respawnRequestList.add(request);
        this.setDirty();
    }

    public void removeRespawnRequest(RespawnRequest request) {
        this.respawnRequestList.remove(request);
        this.setDirty();
    }

    public List<RespawnRequest> getRespawnRequestsFor(Level level, BlockPos pos) {
        List<RespawnRequest> list = new ArrayList<>();
        String dimension = level.dimension().location().toString();
        for (RespawnRequest request : this.respawnRequestList) {
            String reqDim = TameableUtils.normalizeDimension(request.getDimension());
            if (dimension.equals(reqDim) && pos.equals(request.getBedPosition())) {
                list.add(request);
            }
        }
        return list;
    }

    public void addLanternRequest(LanternRequest request) {
        this.lanternRequestList.add(request);
        this.setDirty();
    }

    public void removeLanternRequest(LanternRequest request) {
        this.lanternRequestList.remove(request);
        this.setDirty();
    }

    public void removeMatchingLanternRequests(UUID reloaded) {
        boolean removed = this.lanternRequestList.removeIf(request -> request.getPetUUID().equals(reloaded));
        if (removed) {
            this.setDirty();
        }
    }

    public List<LanternRequest> getLanternRequestsFor(UUID uuid) {
        List<LanternRequest> list = new ArrayList<>();
        for (LanternRequest request : this.lanternRequestList) {
            if (uuid.equals(request.getOwnerUUID())) {
                list.add(request);
            }
        }
        return list;
    }
}