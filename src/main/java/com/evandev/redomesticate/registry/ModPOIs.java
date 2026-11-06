package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class ModPOIs {
    public static final RegistrationProvider<PoiType> POI_REGISTRY = RegistrationProvider.get(Registries.POINT_OF_INTEREST_TYPE, Constants.MOD_ID);

    public static final RegistryObject<PoiType> PET_BED = POI_REGISTRY.register("pet_bed", () -> new PoiType(getBeds(), 1, 1));

    public static Set<BlockState> getBeds() {
        return ModBlocks.PET_BED_BLOCKS.values().stream().flatMap((petbed) -> {
            return petbed.get().getStateDefinition().getPossibleStates().stream();
        }).collect(ImmutableSet.toImmutableSet());
    }

    public static void init() {
    }
}