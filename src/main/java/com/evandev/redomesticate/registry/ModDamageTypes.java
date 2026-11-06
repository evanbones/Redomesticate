package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> SIPHON = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "siphon"));

    public static DamageSource causeSiphonDamage(RegistryAccess registryAccess) {
        HolderLookup.Provider registries = registryAccess;
        return new DamageSource(registries.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SIPHON));
    }

    public static void init() {
    }
}
