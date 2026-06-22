package com.evandev.redomesticate.platform.services;

import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.server.level.ServerPlayer;
import java.nio.file.Path;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Gets the configuration directory for the current platform.
     *
     * @return The path to the config directory.
     */
    Path getConfigDirectory();

    /**
     * Checks if the code is running on the physical client.
     *
     * @return True if on the client, false if on a dedicated server.
     */
    boolean isPhysicalClient();

    <T> RegistrationProvider<T> createRegistrationProvider(ResourceKey<? extends Registry<T>> registry, String modId);

    /**
     * Gets an unlit translucent render type for the given texture.
     */
    RenderType getUnlitTranslucent(ResourceLocation texture);

    default boolean canLivingConvert(LivingEntity entity, EntityType<?> outcome) {
        return true;
    }

    default void onLivingConvert(LivingEntity entity, LivingEntity outcome) {
    }

    boolean isOre(BlockState state);

    void sendToAllPlayers(Object message, ResourceLocation id);

    void sendToServer(Object message, ResourceLocation id);

    void sendToPlayer(ServerPlayer player, Object message, ResourceLocation id);
}