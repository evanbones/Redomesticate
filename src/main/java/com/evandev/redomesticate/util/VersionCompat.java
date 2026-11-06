package com.evandev.redomesticate.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
//? if >=26.1 {
/*import net.minecraft.world.item.enchantment.EnchantmentHelper;
*///?} else {
import net.minecraft.world.item.EnchantedBookItem;
//?}
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

//? if >=26.3 {
/*import net.minecraft.world.item.component.SwingAnimation;
*///?}
//? if >=26.1 {
/*import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.GameRules;
*///?} else {
import net.minecraft.world.level.GameRules;
//?}

public class VersionCompat {

    public static void sendMessage(Player player, Component message, boolean actionBar) {
        //? if >=26.1 {
        /*if (actionBar) player.sendOverlayMessage(message);
        else player.sendSystemMessage(message);
        *///?} else {
        player.displayClientMessage(message, actionBar);
        //?}
    }

    public static long getDayTime(Level level) {
        //? if >=26.1 {
        /*return level.getOverworldClockTime();
        *///?} else {
        return level.dayTime();
        //?}
    }

    public static InteractionResult sidedSuccess(boolean clientSide) {
        //? if >=26.1 {
        /*return InteractionResult.SUCCESS;
        *///?} else {
        return InteractionResult.sidedSuccess(clientSide);
        //?}
    }

    public static @Nullable MinecraftServer getServer(Entity entity) {
        return entity.level().getServer();
    }

    public static <T extends Entity> @Nullable T create(EntityType<T> type, Level level) {
        //? if >=26.1 {
        /*return type.create(level, EntitySpawnReason.MOB_SUMMONED);
        *///?} else {
        return type.create(level);
        //?}
    }

    public static void spawnAtLocation(Entity entity, ItemStack stack) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            //? if >=26.1 {
            /*entity.spawnAtLocation(serverLevel, stack);
            *///?} else {
            entity.spawnAtLocation(stack);
            //?}
        }
    }

    public static @Nullable UUID getOwnerUUID(OwnableEntity entity) {
        //? if >=26.1 {
        /*EntityReference<LivingEntity> owner = entity.getOwnerReference();
        return owner == null ? null : owner.getUUID();
        *///?} else {
        return entity.getOwnerUUID();
        //?}
    }

    public static void setOwnerUUID(TamableAnimal entity, @Nullable UUID uuid) {
        //? if >=26.1 {
        /*entity.setOwnerReference(uuid == null ? null : EntityReference.of(uuid));
        *///?} else {
        entity.setOwnerUUID(uuid);
        //?}
    }

    public static boolean isDamageSourceBlocked(LivingEntity entity, DamageSource source) {
        //? if >=26.1 {
        /*return entity.getItemBlockingWith() != null;
        *///?} else {
        return entity.isDamageSourceBlocked(source);
        //?}
    }

    public static boolean showDeathMessages(ServerLevel level) {
        //? if >=26.1 {
        /*return level.getGameRules().get(GameRules.SHOW_DEATH_MESSAGES);
        *///?} else {
        return level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);
        //?}
    }

    public static <T> Optional<Holder<T>> getHolder(RegistryAccess access, ResourceKey<? extends Registry<? extends T>> registry, ResourceLocation id) {
        //? if >=26.1 {
        /*return access.lookup(registry).flatMap(reg -> reg.<T>get(id).map(holder -> holder));
        *///?} else {
        return access.<T>registry(registry).flatMap(reg -> reg.getHolder(id).map(holder -> holder));
        //?}
    }

    public static ItemStack createEnchantedBook(EnchantmentInstance enchantment) {
        //? if >=26.1 {
        /*return EnchantmentHelper.createBook(enchantment);
        *///?} else {
        return EnchantedBookItem.createForEnchantment(enchantment);
        //?}
    }

    public static void swing(LivingEntity entity, InteractionHand hand, boolean updateSelf) {
        //? if >=26.3 {
        /*entity.swing(hand, SwingAnimation.DEFAULT, updateSelf);
        *///?} else {
        entity.swing(hand, updateSelf);
        //?}
    }

    public static void startRiding(Entity rider, Entity vehicle) {
        //? if >=26.1 {
        /*rider.startRiding(vehicle, true, true);
        *///?} else {
        rider.startRiding(vehicle, true);
        //?}
    }

    // The accessor itself must be defined from the entity class (NeoForge checks the caller), so only the serializer lives here.
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static EntityDataSerializer<Object> syncedUUIDSerializer() {
        //? if >=26.1 {
        /*return (EntityDataSerializer) EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE;
        *///?} else {
        return (EntityDataSerializer) EntityDataSerializers.OPTIONAL_UUID;
        //?}
    }

    public static void defineSyncedUUID(SynchedEntityData.Builder builder, EntityDataAccessor<Object> accessor) {
        builder.define(accessor, Optional.empty());
    }

    @SuppressWarnings("unchecked")
    public static @Nullable UUID getSyncedUUID(SynchedEntityData data, EntityDataAccessor<Object> accessor) {
        //? if >=26.1 {
        /*return ((Optional<EntityReference<LivingEntity>>) data.get(accessor)).map(EntityReference::getUUID).orElse(null);
        *///?} else {
        return ((Optional<UUID>) data.get(accessor)).orElse(null);
        //?}
    }

    public static void setSyncedUUID(SynchedEntityData data, EntityDataAccessor<Object> accessor, @Nullable UUID uuid) {
        //? if >=26.1 {
        /*data.set(accessor, Optional.ofNullable(uuid).map(EntityReference::<LivingEntity>of));
        *///?} else {
        data.set(accessor, Optional.ofNullable(uuid));
        //?}
    }
}
