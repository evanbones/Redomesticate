package com.evandev.redomesticate.platform;

import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.services.IPlatformHelper;
import com.evandev.redomesticate.event.EventProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {
    private static final TagKey<Block> C_ORES = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "ores"));

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public <T> RegistrationProvider<T> createRegistrationProvider(ResourceKey<? extends Registry<T>> registry, String modId) {
        return new FabricRegistrationProvider<>(registry, modId);
    }

    @Override
    public RenderType getUnlitTranslucent(ResourceLocation texture) {
        return RenderType.entityTranslucentEmissive(texture);
    }

    @Override
    public boolean isOre(BlockState state) {
        return state.is(C_ORES);
    }

    @Override
    public void sendToAllPlayers(Object message, ResourceLocation id) {
        if (message instanceof CustomPacketPayload payload) {
            MinecraftServer server = EventProxy.currentServer;
            if (server != null) {
                for (ServerPlayer player : PlayerLookup.all(server)) {
                    ServerPlayNetworking.send(player, payload);
                }
            }
        }
    }

    @Override
    public void sendToServer(Object message, ResourceLocation id) {
        if (message instanceof CustomPacketPayload payload) {
            ClientPlayNetworking.send(payload);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, Object message, ResourceLocation id) {
        if (message instanceof CustomPacketPayload payload) {
            ServerPlayNetworking.send(player, payload);
        }
    }
}