package com.evandev.redomesticate.fabric.registry;

//? if fabric {
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.registry.ModItems;
import com.evandev.redomesticate.config.ModConfig;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
//? if >=26.3 {
/*import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
*///?} else {
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
//?}

public class FabricModLoot {

    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (key.equals(BuiltInLootTables.WOODLAND_MANSION)) {
                tableBuilder.pool(LootPool.lootPool()
                        .setRolls(exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance((float) ModConfig.get().sinisterCarrotLootChance))
                        .add(LootItem.lootTableItem(ModItems.SINISTER_CARROT.get())
                                .apply(SetItemCountFunction.setCount(between(1, 2)))).build()
                );
                addEnchantedBookPool(tableBuilder, registries, ModEnchantments.VAMPIRE, (float) ModConfig.get().vampirismLootChance);
            }

            if (key.equals(BuiltInLootTables.BURIED_TREASURE)) {
                addEnchantedBookPool(tableBuilder, registries, ModEnchantments.BUBBLING, (float) ModConfig.get().bubblingLootChance);
            }

            if (key.equals(BuiltInLootTables.ABANDONED_MINESHAFT)) {
                addEnchantedBookPool(tableBuilder, registries, ModEnchantments.ORE_SCENTING, (float) ModConfig.get().oreScentingLootChance);
            }

            if (key.equals(BuiltInLootTables.NETHER_BRIDGE)) {
                addEnchantedBookPool(tableBuilder, registries, ModEnchantments.BLAZING_PROTECTION, (float) ModConfig.get().blazingProtectionLootChance);
            }

            if (key.equals(BuiltInLootTables.END_CITY_TREASURE)) {
                addEnchantedBookPool(tableBuilder, registries, ModEnchantments.VOID_CLOUD, (float) ModConfig.get().voidCloudLootChance);
            }

            if (key.equals(BuiltInLootTables.ANCIENT_CITY)) {
                addEnchantedBookPool(tableBuilder, registries, ModEnchantments.MUFFLED, (float) ModConfig.get().muffledLootChance);
            }

            if (key.location().getPath().equals("chests/pet_shop") && key.location().getNamespace().equals("redomesticate")) {
                tableBuilder.pool(LootPool.lootPool()
                        .setRolls(between(1, 2))
                        .when(LootItemRandomChanceCondition.randomChance(0.5F))
                        .add(LootItem.lootTableItem(ModItems.COLLAR_TAG.get())
                                .setWeight(1)
                                //? if >=26.3 {
                                /*.apply(EnchantWithLevelsFunction.enchantWithLevels(registries.lookupOrThrow(Registries.ENCHANTMENT), between(12, 40)))
                                *///?} else {
                                .apply(EnchantWithLevelsFunction.enchantWithLevels(registries, between(12, 40)))
                                //?}
                        )
                        .add(LootItem.lootTableItem(ModItems.COLLAR_TAG.get())
                                .setWeight(2)
                        ).build()
                );
            }
        });
    }

    private static void addEnchantedBookPool(LootTable.Builder tableBuilder,
                                             HolderLookup.Provider registries,
                                             ResourceKey<Enchantment> enchantment,
                                             float chance) {

        var enchantRegistry = registries.lookupOrThrow(Registries.ENCHANTMENT);
        var enchantHolder = enchantRegistry.getOrThrow(enchantment);

        tableBuilder.pool(LootPool.lootPool()
                .setRolls(exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(Items.BOOK)
                        .apply(new EnchantRandomlyFunction.Builder().withEnchantment(enchantHolder))
                ).build()
        );
    }

    //? if >=26.3 {
    /*private static Holder<ContextIntProvider> exactly(int value) {
        return ContextIntProviders.exactly(value);
    }

    private static Holder<ContextIntProvider> between(int min, int max) {
        return ContextIntProviders.between(min, max);
    }
    *///?} else {
    private static NumberProvider exactly(int value) {
        return ConstantValue.exactly(value);
    }

    private static NumberProvider between(int min, int max) {
        return UniformGenerator.between(min, max);
    }
    //?}
}
//?}
