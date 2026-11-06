package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {
    public static final ResourceKey<LootTable> PET_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "chests/petshop_chest"));
    public static final ResourceKey<LootTable> PET_PROTECTION_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "chests/pet_protection_loot"));
}