package com.evandev.redomesticate.content.block.entity;

import com.evandev.redomesticate.util.VersionCompat;
import com.evandev.redomesticate.util.NbtCompat;
import com.evandev.redomesticate.data.ModWorldData;
import com.evandev.redomesticate.data.request.LanternRequest;
import com.evandev.redomesticate.registry.ModBlockEntities;
import com.evandev.redomesticate.util.ChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
//? if >=26.1 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///?}

public class WaywardLanternBlockEntity extends BlockEntity {

    private final List<LanternRequest> workingRequests = new ArrayList<>();
    private final List<UUID> finishedRequests = new ArrayList<>();
    private final List<CompoundTag> pendingSpawns = new ArrayList<>();

    private int checkAgainIn = 100;

    public WaywardLanternBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WAYWARD_LANTERN.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WaywardLanternBlockEntity te) {
        if (!te.pendingSpawns.isEmpty() && level instanceof ServerLevel serverLevel) {
            Iterator<CompoundTag> spawnIt = te.pendingSpawns.iterator();
            while (spawnIt.hasNext()) {
                CompoundTag petData = spawnIt.next();

                Entity newPet = NbtCompat.loadEntityRecursive(petData, serverLevel, (entity) -> {
                    BlockPos putAt = getPlaceFor(entity, pos, level.getRandom());
                    entity.moveTo(putAt.getX() + 0.5D, putAt.getY(), putAt.getZ() + 0.5D, entity.getYRot(), entity.getXRot());
                    return entity;
                });

                if (newPet != null) {
                    serverLevel.addFreshEntityWithPassengers(newPet);

                    UUID ownerId = petData.contains("LanternOwner") ? NbtCompat.getUUID(petData, "LanternOwner") : null;
                    if (ownerId != null) {
                        Player player = serverLevel.getPlayerByUUID(ownerId);
                        if (player != null) {
                            VersionCompat.sendMessage(player, Component.translatable("message.redomesticate.wayward_lantern_return", newPet.getName()), false);
                        }
                    }
                }
                spawnIt.remove();
            }
        }

        if (!te.finishedRequests.isEmpty()) {
            ModWorldData data = ModWorldData.get(level);
            if (data != null) {
                te.workingRequests.removeIf(lanternRequest -> te.finishedRequests.contains(lanternRequest.getPetUUID()));
                for (UUID uuid : te.finishedRequests) {
                    data.removeMatchingLanternRequests(uuid);
                }
                te.finishedRequests.clear();
            }
        }

        if (te.workingRequests.isEmpty()) {
            if (te.checkAgainIn > 0) {
                te.checkAgainIn--;
            } else {
                te.checkAgainIn = 200 + level.getRandom().nextInt(400);
                ModWorldData data = ModWorldData.get(level);
                if (data != null) {
                    for (Player player : getPlayers(level, pos)) {
                        te.workingRequests.addAll(data.getLanternRequestsFor(player.getUUID()));
                    }
                }
            }
        } else {
            if (level instanceof ServerLevel serverLevel) {
                Iterator<LanternRequest> iterator = te.workingRequests.iterator();
                while (iterator.hasNext()) {
                    LanternRequest request = iterator.next();

                    if (!request.areChunksLoaded()) {
                        loadChunksAround(serverLevel, request.getPetUUID(), request.getChunkPosition(), true);
                        request.setChunksLoaded(true);
                    }

                    Entity entityFromChunk = serverLevel.getEntity(request.getPetUUID());
                    request.incrementLoadTimeout();

                    if (entityFromChunk != null || request.getLoadTimeout() > 100) {
                        if (entityFromChunk != null) {

                            double distSqr = Vec3.atCenterOf(request.getChunkPosition()).distanceToSqr(Vec3.atCenterOf(pos));
                            if (distSqr > 24 * 24) {

                                CompoundTag clonedData = NbtCompat.saveAsPassenger(entityFromChunk);
                                if (clonedData != null) {
                                    NbtCompat.putUUID(clonedData, "LanternOwner", request.getOwnerUUID());
                                    te.pendingSpawns.add(clonedData);

                                    entityFromChunk.discard();
                                }
                            }
                            te.finishedRequests.add(request.getPetUUID());
                        }

                        loadChunksAround(serverLevel, request.getPetUUID(), request.getChunkPosition(), false);

                        request.resetLoadTimeout();
                        request.setChunksLoaded(false);
                        iterator.remove();
                    }
                }
            }
        }
    }

    private static void loadChunksAround(ServerLevel serverLevel, UUID petId, BlockPos center, boolean load) {
        //? if >=26.1 {
        /*int centerX = ChunkPos.containing(center).x();
        int centerZ = ChunkPos.containing(center).z();
        *///?} else {
        int centerX = new ChunkPos(center).x;
        int centerZ = new ChunkPos(center).z;
        //?}
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                ChunkPos targetChunk = new ChunkPos(centerX + i, centerZ + j);
                if (load) {
                    ChunkLoader.forceLoadChunk(serverLevel, targetChunk, petId);
                } else {
                    ChunkLoader.unloadChunk(serverLevel, targetChunk, petId);
                }
            }
        }
    }

    private static List<Player> getPlayers(Level level, BlockPos pos) {
        double dist = 64 * 64;
        List<Player> withinDist = new ArrayList<>();
        for (Player player : level.players()) {
            if (player.distanceToSqr(Vec3.atCenterOf(pos)) < dist) {
                withinDist.add(player);
            }
        }
        return withinDist;
    }

    private static BlockPos getPlaceFor(Entity entity, BlockPos lanternPos, RandomSource random) {
        int maxDist = (int) Math.max(entity.getBbWidth() + 1, 10);
        for (int i = 0; i < 10; i++) {
            BlockPos at = lanternPos.offset(random.nextInt(maxDist) - maxDist / 2, 1, random.nextInt(maxDist) - maxDist / 2);
            while (entity.level().getBlockState(at).isAir() && at.getY() > entity.level().getMinBuildHeight() && entity.level().noCollision(entity.getType().getSpawnAABB(at.getX() + 0.5F, at.getY() - 1, at.getZ() + 0.5F))) {
                at = at.below();
            }
            if (entity.level().noCollision(entity.getType().getSpawnAABB(at.getX() + 0.5F, at.getY(), at.getZ() + 0.5F))) {
                return at;
            }
            if (entity.isInWall()) {
                return lanternPos.above();
            }
        }
        return lanternPos.above();
    }

    //? if >=26.1 {
    /*@Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.getInt("CheckAgainIn").ifPresent(i -> this.checkAgainIn = i);
        input.read("PendingSpawns", CompoundTag.CODEC.listOf()).ifPresent(this.pendingSpawns::addAll);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("CheckAgainIn", this.checkAgainIn);
        output.store("PendingSpawns", CompoundTag.CODEC.listOf(), this.pendingSpawns);
    }
    *///?} else {
    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CheckAgainIn")) {
            this.checkAgainIn = NbtCompat.getInt(tag, "CheckAgainIn");
        }
        if (tag.contains("PendingSpawns")) {
            ListTag spawnsList = NbtCompat.getCompoundList(tag, "PendingSpawns");
            for (int i = 0; i < spawnsList.size(); i++) {
                this.pendingSpawns.add(NbtCompat.getCompound(spawnsList, i));
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("CheckAgainIn", this.checkAgainIn);

        ListTag spawnsList = new ListTag();
        spawnsList.addAll(this.pendingSpawns);
        tag.put("PendingSpawns", spawnsList);
    }
    //?}
}