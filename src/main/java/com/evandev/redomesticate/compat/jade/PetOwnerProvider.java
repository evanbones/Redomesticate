package com.evandev.redomesticate.compat.jade;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.config.IPluginConfig;

import java.util.UUID;

public enum PetOwnerProvider implements IEntityComponentProvider, StreamServerDataProvider<EntityAccessor, Component> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        decodeFromData(accessor).ifPresent(name -> tooltip.add(Component.translatable("jade.owner", name)));
    }

    @Override
    public @Nullable Component streamData(EntityAccessor accessor) {
        Entity entity = accessor.getEntity();
        if (entity instanceof OwnableEntity || !(accessor.getLevel() instanceof ServerLevel level)) {
            return null;
        }
        UUID uuid = TameableUtils.getOwnerUUIDOf(entity);
        if (uuid == null) {
            return null;
        }
        Entity owner = level.getServer().getPlayerList().getPlayer(uuid);
        if (owner != null) {
            return owner.getDisplayName();
        }
        //? if >=26.1 {
        /*String name = snownee.jade.util.PlayerNameLookup.get(uuid, level.getServer().services());
        *///?} else {
        String name = snownee.jade.util.CommonProxy.getLastKnownUsername(uuid);
        //?}
        return name == null ? null : Component.literal(name);
    }

    @Override
    public boolean shouldRequestData(EntityAccessor accessor) {
        return !(accessor.getEntity() instanceof OwnableEntity);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Component> streamCodec() {
        return ComponentSerialization.STREAM_CODEC;
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "pet_owner");
    }
}
