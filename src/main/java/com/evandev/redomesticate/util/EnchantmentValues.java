package com.evandev.redomesticate.util;

import com.evandev.redomesticate.registry.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public class EnchantmentValues {

    public static float get(LivingEntity pet, ResourceKey<Enchantment> enchantment, String name, LevelBasedValue fallback) {
        return get(pet, enchantment, TameableUtils.getEnchantLevel(pet, enchantment), name, fallback);
    }

    public static float get(LivingEntity pet, ResourceKey<Enchantment> enchantment, int level, String name, LevelBasedValue fallback) {
        if (level <= 0) {
            return 0;
        }
        LevelBasedValue value = VersionCompat.<Enchantment>getHolder(pet.registryAccess(), Registries.ENCHANTMENT, enchantment.location())
                .map(holder -> holder.value().effects().get(ModEnchantments.PET_VALUES.get()))
                .map(values -> values.get(name))
                .orElse(fallback);
        return value.calculate(level);
    }

    public static int getInt(LivingEntity pet, ResourceKey<Enchantment> enchantment, String name, LevelBasedValue fallback) {
        return Math.round(get(pet, enchantment, name, fallback));
    }

    public static int getInt(LivingEntity pet, ResourceKey<Enchantment> enchantment, int level, String name, LevelBasedValue fallback) {
        return Math.round(get(pet, enchantment, level, name, fallback));
    }
}
