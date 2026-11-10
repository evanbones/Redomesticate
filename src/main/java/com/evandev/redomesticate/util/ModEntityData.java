package com.evandev.redomesticate.util;

import com.evandev.redomesticate.api.IPetbedDataEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.Objects;

public class ModEntityData {
    public static CompoundTag getOrCreateEntityTag(LivingEntity entity) {
        CompoundTag tag = getEntityTag(entity);
        return tag == null ? new CompoundTag() : tag;
    }

    public static CompoundTag getEntityTag(LivingEntity entity) {
        return entity instanceof IPetbedDataEntity ? ((IPetbedDataEntity) entity).redomesticate$getEntityData() : new CompoundTag();
    }

    public static void setEntityTag(LivingEntity entity, CompoundTag tag) {
        if (entity instanceof IPetbedDataEntity petbedData) {
            Map<ResourceLocation, Integer> prevEnchants = petbedData.redomesticate$getCachedEnchants();
            petbedData.redomesticate$setEntityData(tag);
            Map<ResourceLocation, Integer> enchants = TameableUtils.getEnchants(entity);
            petbedData.redomesticate$setCachedEnchants(enchants);
            if (!Objects.equals(prevEnchants, enchants)) {
                TameableUtils.onUpdateEnchants(prevEnchants, entity);
            }
        }
    }
}
