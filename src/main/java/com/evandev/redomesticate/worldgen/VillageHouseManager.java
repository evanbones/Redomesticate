package com.evandev.redomesticate.worldgen;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.config.ModConfig;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VillageHouseManager {
    public static final List<ResourceLocation> VILLAGE_REPLACEMENT_POOLS = List.of(ResourceLocation.parse("minecraft:village/plains/houses"), ResourceLocation.parse("minecraft:village/desert/houses"), ResourceLocation.parse("minecraft:village/savanna/houses"), ResourceLocation.parse("minecraft:village/snowy/houses"), ResourceLocation.parse("minecraft:village/taiga/houses"));
    private static final List<Pair<ResourceLocation, Consumer<StructureTemplatePool>>> REGISTRY = new ArrayList<>();

    public static void addToPool(StructureTemplatePool pool, StructurePoolElement element, int weight) {
        if (weight > 0 && pool != null) {
            ObjectArrayList<StructurePoolElement> templates = new ObjectArrayList<>(pool.templates);
            if (!templates.contains(element)) {
                for (int i = 0; i < weight; ++i) {
                    templates.add(element);
                }

                List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(pool.rawTemplates);
                rawTemplates.add(new Pair<>(element, weight));
                pool.templates = templates;
                pool.rawTemplates = rawTemplates;
            }
        }
    }

    public static void addAllHouses(RegistryAccess registryAccess) {
        int weight = ModConfig.get().petStoreVillageWeight;
        StructurePoolElement plains = new PetshopStructurePoolElement(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "plains_petshop"), StructurePoolElement.EMPTY);
        REGISTRY.add(new Pair<>(ResourceLocation.parse("minecraft:village/plains/houses"), (pool) -> VillageHouseManager.addToPool(pool, plains, weight)));
        StructurePoolElement desert = new PetshopStructurePoolElement(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "desert_petshop"), StructurePoolElement.EMPTY);
        REGISTRY.add(new Pair<>(ResourceLocation.parse("minecraft:village/desert/houses"), (pool) -> VillageHouseManager.addToPool(pool, desert, weight)));
        StructurePoolElement savanna = new PetshopStructurePoolElement(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "savanna_petshop"), StructurePoolElement.EMPTY);
        REGISTRY.add(new Pair<>(ResourceLocation.parse("minecraft:village/savanna/houses"), (pool) -> VillageHouseManager.addToPool(pool, savanna, weight)));
        StructurePoolElement snowy = new PetshopStructurePoolElement(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "snowy_petshop"), StructurePoolElement.EMPTY);
        REGISTRY.add(new Pair<>(ResourceLocation.parse("minecraft:village/snowy/houses"), (pool) -> VillageHouseManager.addToPool(pool, snowy, weight)));
        StructurePoolElement taiga = new PetshopStructurePoolElement(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "taiga_petshop"), StructurePoolElement.EMPTY);
        REGISTRY.add(new Pair<>(ResourceLocation.parse("minecraft:village/taiga/houses"), (pool) -> VillageHouseManager.addToPool(pool, taiga, weight)));

        try {
            for (ResourceLocation villagePool : VILLAGE_REPLACEMENT_POOLS) {
                StructureTemplatePool pool = registryAccess.registryOrThrow(Registries.TEMPLATE_POOL).getOptional(villagePool).orElse(null);
                if (pool != null) {
                    for (Pair<ResourceLocation, Consumer<StructureTemplatePool>> pair : REGISTRY) {
                        if (villagePool.equals(pair.getFirst())) {
                            pair.getSecond().accept(pool);

                        }
                    }
                }
            }
        } catch (Exception e) {
            Constants.LOG.error("Could not add village houses!");
            Constants.LOG.error(e.toString());
        }
    }
}
