package com.evandev.redomesticate.util;

import com.evandev.redomesticate.Constants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.UUID;

//? if >=26.1 {
/*import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
*///?}

public class ChunkLoader {
    //? if >=26.1 {
    /*private static final RegistrationProvider<TicketType> TICKET_REGISTRY = RegistrationProvider.get(Registries.TICKET_TYPE, Constants.MOD_ID);
    private static final RegistryObject<TicketType> LANTERN_TICKET = TICKET_REGISTRY.register("lantern",
            () -> new TicketType(100L, TicketType.FLAG_LOADING | TicketType.FLAG_SIMULATION));
    *///?} else {
    private static final TicketType<UUID> LANTERN_TICKET = TicketType.create(Constants.MOD_ID + "_lantern", UUID::compareTo, 100);
    //?}

    public static void init() {
    }

    public static void forceLoadChunk(Level level, ChunkPos chunkPos, UUID petId) {
        ServerLevel serverLevel = (ServerLevel) level;
        //? if >=26.1 {
        /*serverLevel.getChunkSource().addTicketWithRadius(LANTERN_TICKET.get(), chunkPos, 2);
        *///?} else {
        serverLevel.getChunkSource().chunkMap.getDistanceManager().addRegionTicket(LANTERN_TICKET, chunkPos, 31, petId);
        //?}
    }

    public static void unloadChunk(Level level, ChunkPos chunkPos, UUID petId) {
        ServerLevel serverLevel = (ServerLevel) level;
        //? if >=26.1 {
        /*serverLevel.getChunkSource().removeTicketWithRadius(LANTERN_TICKET.get(), chunkPos, 2);
        *///?} else {
        serverLevel.getChunkSource().chunkMap.getDistanceManager().removeRegionTicket(LANTERN_TICKET, chunkPos, 31, petId);
        //?}
    }
}
