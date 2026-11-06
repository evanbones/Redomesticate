package com.evandev.redomesticate.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

//? if >=26.1 {
/*import net.minecraft.core.UUIDUtil;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntitySpawnReason;
//? if >=26.2 {
/^import net.minecraft.world.entity.EntitySpawnRequest;
^///?}
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
*///?}

public class NbtCompat {

    public static int getInt(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getIntOr(key, 0);
        *///?} else {
        return tag.getInt(key);
        //?}
    }

    public static long getLong(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getLongOr(key, 0L);
        *///?} else {
        return tag.getLong(key);
        //?}
    }

    public static float getFloat(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getFloatOr(key, 0F);
        *///?} else {
        return tag.getFloat(key);
        //?}
    }

    public static double getDouble(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getDoubleOr(key, 0D);
        *///?} else {
        return tag.getDouble(key);
        //?}
    }

    public static boolean getBoolean(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getBooleanOr(key, false);
        *///?} else {
        return tag.getBoolean(key);
        //?}
    }

    public static String getString(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getStringOr(key, "");
        *///?} else {
        return tag.getString(key);
        //?}
    }

    public static int[] getIntArray(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getIntArray(key).orElse(new int[0]);
        *///?} else {
        return tag.getIntArray(key);
        //?}
    }

    public static CompoundTag getCompound(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getCompoundOrEmpty(key);
        *///?} else {
        return tag.getCompound(key);
        //?}
    }

    public static ListTag getCompoundList(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.getListOrEmpty(key);
        *///?} else {
        return tag.getList(key, 10);
        //?}
    }

    public static CompoundTag getCompound(ListTag list, int index) {
        //? if >=26.1 {
        /*return list.getCompoundOrEmpty(index);
        *///?} else {
        return list.getCompound(index);
        //?}
    }

    public static boolean hasUUID(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.read(key, UUIDUtil.CODEC).isPresent();
        *///?} else {
        return tag.hasUUID(key);
        //?}
    }

    public static @Nullable UUID getUUID(CompoundTag tag, String key) {
        //? if >=26.1 {
        /*return tag.read(key, UUIDUtil.CODEC).orElse(null);
        *///?} else {
        return tag.hasUUID(key) ? tag.getUUID(key) : null;
        //?}
    }

    public static void putUUID(CompoundTag tag, String key, UUID uuid) {
        //? if >=26.1 {
        /*tag.store(key, UUIDUtil.CODEC, uuid);
        *///?} else {
        tag.putUUID(key, uuid);
        //?}
    }

    public static Set<String> keys(CompoundTag tag) {
        //? if >=26.1 {
        /*return tag.keySet();
        *///?} else {
        return tag.getAllKeys();
        //?}
    }

    public static Tag saveItem(ItemStack stack, HolderLookup.Provider registries) {
        //? if >=26.1 {
        /*return ItemStack.OPTIONAL_CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), stack).getOrThrow();
        *///?} else {
        return stack.saveOptional(registries);
        //?}
    }

    public static ItemStack loadItem(CompoundTag tag, HolderLookup.Provider registries) {
        //? if >=26.1 {
        /*return ItemStack.OPTIONAL_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag).result().orElse(ItemStack.EMPTY);
        *///?} else {
        return ItemStack.parseOptional(registries, tag);
        //?}
    }

    public static void copyForConversion(Entity from, Entity to) {
        CompoundTag tag = saveEntity(from);
        for (String key : new String[]{"UUID", "Pos", "Motion", "Rotation", "Passengers", "id"}) {
            tag.remove(key);
        }
        loadEntity(to, tag);
    }

    public static CompoundTag saveEntity(Entity entity) {
        //? if >=26.1 {
        /*TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
        entity.saveWithoutId(output);
        return output.buildResult();
        *///?} else {
        CompoundTag tag = new CompoundTag();
        entity.saveWithoutId(tag);
        return tag;
        //?}
    }

    public static @Nullable CompoundTag saveAsPassenger(Entity entity) {
        //? if >=26.1 {
        /*TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
        return entity.saveAsPassenger(output) ? output.buildResult() : null;
        *///?} else {
        CompoundTag tag = new CompoundTag();
        return entity.saveAsPassenger(tag) ? tag : null;
        //?}
    }

    public static void loadEntity(Entity entity, CompoundTag tag) {
        //? if >=26.1 {
        /*entity.load(TagValueInput.create(ProblemReporter.DISCARDING, entity.registryAccess(), tag));
        *///?} else {
        entity.load(tag);
        //?}
    }

    public static @Nullable Entity loadEntityRecursive(CompoundTag tag, Level level, Function<Entity, Entity> postLoad) {
        //? if >=26.1 {
        /*//? if >=26.2 {
        /^return EntityType.loadEntityRecursive(tag, level, new EntitySpawnRequest(EntitySpawnReason.LOAD, false), postLoad::apply);
        ^///?} else {
        return EntityType.loadEntityRecursive(tag, level, EntitySpawnReason.LOAD, postLoad::apply);
        //?}
        *///?} else {
        return EntityType.loadEntityRecursive(tag, level, postLoad);
        //?}
    }
}
