package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Predicate;
//? if >=26.1 {
/*import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.trading.TradeSet;
*///?}

public class ModVillagers {
    public static final RegistrationProvider<VillagerProfession> VILLAGER_REGISTRY =
            RegistrationProvider.get(Registries.VILLAGER_PROFESSION, Constants.MOD_ID);

    public static final RegistryObject<VillagerProfession> ANIMAL_TAMER = VILLAGER_REGISTRY.register("animal_tamer", ModVillagers::buildVillagerProfession);

    private static VillagerProfession buildVillagerProfession() {
        ResourceKey<PoiType> petBedKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "pet_bed"));

        Predicate<Holder<PoiType>> heldJobSite = (poiType) -> poiType.is(petBedKey);
        Predicate<Holder<PoiType>> acquirableJobSite = (poiType) -> poiType.is(petBedKey);

        //? if >=26.1 {
        /*Int2ObjectMap<ResourceKey<TradeSet>> trades = new Int2ObjectOpenHashMap<>();
        for (int level = 1; level <= 5; level++) {
            trades.put(level, ResourceKey.create(Registries.TRADE_SET, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animal_tamer/level_" + level)));
        }
        return new VillagerProfession(Component.translatable("entity." + Constants.MOD_ID + ".villager.animal_tamer"), heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), ModSounds.PET_BED_USE.get(), trades);
        *///?} else {
        return new VillagerProfession("animal_tamer", heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), ModSounds.PET_BED_USE.get());
        //?}
    }

    public static void init() {
    }
}