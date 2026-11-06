package com.evandev.redomesticate.neoforge.registry;

//? if neoforge {
/*import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.neoforge.loot.ModLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class NeoForgeModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Constants.MOD_ID);

    public static final Supplier<MapCodec<ModLootModifier>> MOD_LOOT_MODIFIER =
            GLM.register("mod_loot_modifier", () -> ModLootModifier.CODEC);

    public static void register(IEventBus eventBus) {
        GLM.register(eventBus);
    }
}
*///?}
