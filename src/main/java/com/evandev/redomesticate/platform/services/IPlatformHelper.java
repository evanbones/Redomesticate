package com.evandev.redomesticate.platform.services;

import com.evandev.redomesticate.data.PetState;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.server.level.ServerPlayer;
import java.nio.file.Path;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    Path getConfigDirectory();

    boolean isPhysicalClient();

    <T> RegistrationProvider<T> createRegistrationProvider(ResourceKey<? extends Registry<T>> registry, String modId);

    RenderType getUnlitTranslucent(ResourceLocation texture);

    default boolean canLivingConvert(LivingEntity entity, EntityType<?> outcome) {
        return true;
    }

    default void onLivingConvert(LivingEntity entity, LivingEntity outcome) {
    }

    boolean isOre(BlockState state);

    //? if >=26.1 {
    /*PetState getPetState(Mob mob);

    void setPetState(Mob mob, PetState state);
    *///?}

    void sendToAllPlayers(Object message, ResourceLocation id);

    void sendToServer(Object message, ResourceLocation id);

    void sendToPlayer(ServerPlayer player, Object message, ResourceLocation id);
}