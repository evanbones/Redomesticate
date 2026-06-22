package com.evandev.redomesticate.platform;

import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.services.IPlatformHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.server.level.ServerPlayer;
import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public <T> RegistrationProvider<T> createRegistrationProvider(ResourceKey<? extends Registry<T>> registry, String modId) {
        return new NeoForgeRegistrationProvider<>(registry, modId);
    }

    @Override
    public RenderType getUnlitTranslucent(ResourceLocation texture) {
        return NeoForgeRenderTypes.getUnlitTranslucent(texture);
    }

    @Override
    public boolean isOre(BlockState state) {
        return state.is(Tags.Blocks.ORES);
    }

    @Override
    public void sendToAllPlayers(Object message, ResourceLocation id) {
        if (message instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToAllPlayers(payload);
        }
    }

    @Override
    public void sendToServer(Object message, ResourceLocation id) {
        if (message instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToServer(payload);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, Object message, ResourceLocation id) {
        if (message instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}