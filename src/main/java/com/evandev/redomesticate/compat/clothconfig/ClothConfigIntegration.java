package com.evandev.redomesticate.compat.clothconfig;

import com.evandev.redomesticate.config.ModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class ClothConfigIntegration {

    public static Screen createScreen(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.redomesticate.title"));

        builder.setSavingRunnable(ModConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.redomesticate.category.general"));
        ConfigCategory loot = builder.getOrCreateCategory(Component.translatable("config.redomesticate.category.loot"));
        ConfigCategory enchantments = builder.getOrCreateCategory(Component.translatable("config.redomesticate.category.enchantments"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // General Category
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.trinaryCommandSystem"), config.trinaryCommandSystem)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.trinaryCommandSystem.tooltip"))
                .setSaveConsumer(val -> config.trinaryCommandSystem = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.tameableHorse"), config.tameableHorse)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.tameableHorse.tooltip"))
                .setSaveConsumer(val -> config.tameableHorse = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.tameableFox"), config.tameableFox)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.tameableFox.tooltip"))
                .setSaveConsumer(val -> config.tameableFox = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.swingThroughPets"), config.swingThroughPets)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.swingThroughPets.tooltip"))
                .setSaveConsumer(val -> config.swingThroughPets = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.rotten_apple"), config.rottenApple)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.rotten_apple.tooltip"))
                .setSaveConsumer(val -> config.rottenApple = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.petBedRespawns"), config.petBedRespawns)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.petBedRespawns.tooltip"))
                .setSaveConsumer(val -> config.petBedRespawns = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.rabbitsScareRavagers"), config.rabbitsScareRavagers)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.rabbitsScareRavagers.tooltip"))
                .setSaveConsumer(val -> config.rabbitsScareRavagers = val).build());

        general.addEntry(entryBuilder.startIntField(Component.translatable("redomesticate.configuration.petstore_village_weight"), config.petStoreVillageWeight)
                .setDefaultValue(17)
                .setTooltip(Component.translatable("redomesticate.configuration.petstore_village_weight.tooltip"))
                .setSaveConsumer(val -> config.petStoreVillageWeight = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.disablePetTeleportation"), config.disablePetTeleportation)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("redomesticate.configuration.disablePetTeleportation.tooltip"))
                .setSaveConsumer(val -> config.disablePetTeleportation = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.petWontAttackWhenInjured"), config.petWontAttackWhenInjured)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.petWontAttackWhenInjured.tooltip"))
                .setSaveConsumer(val -> config.petWontAttackWhenInjured = val).build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.petInjuredStatusHealthRatio"), config.petInjuredStatusHealthRatio)
                .setDefaultValue(0.2D)
                .setMin(0.0D)
                .setMax(1.0D)
                .setTooltip(Component.translatable("redomesticate.configuration.petInjuredStatusHealthRatio.tooltip"))
                .setSaveConsumer(val -> config.petInjuredStatusHealthRatio = val).build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.enablePetRoamingRadius"), config.enablePetRoamingRadius)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("redomesticate.configuration.enablePetRoamingRadius.tooltip"))
                .setSaveConsumer(val -> config.enablePetRoamingRadius = val).build());

        general.addEntry(entryBuilder.startIntField(Component.translatable("redomesticate.configuration.petRoamingRadius"), config.petRoamingRadius)
                .setDefaultValue(32)
                .setMin(2)
                .setMax(256)
                .setTooltip(Component.translatable("redomesticate.configuration.petRoamingRadius.tooltip"))
                .setSaveConsumer(val -> config.petRoamingRadius = val).build());

        // Loot Category
        loot.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration.petCurseEnchantmentsLootOnly"), config.petCurseEnchantmentsLootOnly)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration.petCurseEnchantmentsLootOnly.tooltip"))
                .setSaveConsumer(val -> config.petCurseEnchantmentsLootOnly = val).build());

        loot.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.blazing_protection_loot_chance"), config.blazingProtectionLootChance)
                .setDefaultValue(0.2D)
                .setTooltip(Component.translatable("redomesticate.configuration.blazing_protection_loot_chance.tooltip"))
                .setSaveConsumer(val -> config.blazingProtectionLootChance = val).build());

        loot.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.sinister_carrot_loot_chance"), config.sinisterCarrotLootChance)
                .setDefaultValue(0.3D)
                .setTooltip(Component.translatable("redomesticate.configuration.sinister_carrot_loot_chance.tooltip"))
                .setSaveConsumer(val -> config.sinisterCarrotLootChance = val).build());

        loot.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.bubbling_loot_chance"), config.bubblingLootChance)
                .setDefaultValue(0.65D)
                .setTooltip(Component.translatable("redomesticate.configuration.bubbling_loot_chance.tooltip"))
                .setSaveConsumer(val -> config.bubblingLootChance = val).build());

        loot.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.vampirism_loot_chance"), config.vampirismLootChance)
                .setDefaultValue(0.22D)
                .setTooltip(Component.translatable("redomesticate.configuration.vampirism_loot_chance.tooltip"))
                .setSaveConsumer(val -> config.vampirismLootChance = val).build());

        loot.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.voidCloudLootChance"), config.voidCloudLootChance)
                .setDefaultValue(0.19D)
                .setTooltip(Component.translatable("redomesticate.configuration.voidCloudLootChance.tooltip"))
                .setSaveConsumer(val -> config.voidCloudLootChance = val).build());

        loot.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.muffledLootChance"), config.muffledLootChance)
                .setDefaultValue(0.19D)
                .setTooltip(Component.translatable("redomesticate.configuration.muffledLootChance.tooltip"))
                .setSaveConsumer(val -> config.muffledLootChance = val).build());

        loot.addEntry(entryBuilder.startDoubleField(Component.translatable("redomesticate.configuration.ore_scenting_loot_chance"), config.oreScentingLootChance)
                .setDefaultValue(0.15D)
                .setTooltip(Component.translatable("redomesticate.configuration.ore_scenting_loot_chance.tooltip"))
                .setSaveConsumer(val -> config.oreScentingLootChance = val).build());

        // Enchantments Category
        addEnchantToggle(enchantments, entryBuilder, "enableAmphibious", config.enableAmphibious, val -> config.enableAmphibious = val);
        addEnchantToggle(enchantments, entryBuilder, "enableBlightCurse", config.enableBlightCurse, val -> config.enableBlightCurse = val);
        addEnchantToggle(enchantments, entryBuilder, "enableBubbling", config.enableBubbling, val -> config.enableBubbling = val);
        addEnchantToggle(enchantments, entryBuilder, "enableChainLightning", config.enableChainLightning, val -> config.enableChainLightning = val);
        addEnchantToggle(enchantments, entryBuilder, "enableCharisma", config.enableCharisma, val -> config.enableCharisma = val);
        addEnchantToggle(enchantments, entryBuilder, "enableDiscJockey", config.enableDiscJockey, val -> config.enableDiscJockey = val);
        addEnchantToggle(enchantments, entryBuilder, "enableHealthBoost", config.enableHealthBoost, val -> config.enableHealthBoost = val);
        addEnchantToggle(enchantments, entryBuilder, "enableImmunityFrame", config.enableImmunityFrame, val -> config.enableImmunityFrame = val);
        addEnchantToggle(enchantments, entryBuilder, "enableFireproof", config.enableFireproof, val -> config.enableFireproof = val);
        addEnchantToggle(enchantments, entryBuilder, "enableDeflection", config.enableDeflection, val -> config.enableDeflection = val);
        addEnchantToggle(enchantments, entryBuilder, "enableSpeedster", config.enableSpeedster, val -> config.enableSpeedster = val);
        addEnchantToggle(enchantments, entryBuilder, "enableHealingAura", config.enableHealingAura, val -> config.enableHealingAura = val);
        addEnchantToggle(enchantments, entryBuilder, "enableHealthSiphon", config.enableHealthSiphon, val -> config.enableHealthSiphon = val);
        addEnchantToggle(enchantments, entryBuilder, "enableRejuvenation", config.enableRejuvenation, val -> config.enableRejuvenation = val);
        addEnchantToggle(enchantments, entryBuilder, "enableVampire", config.enableVampire, val -> config.enableVampire = val);
        addEnchantToggle(enchantments, entryBuilder, "enableLinkedInventory", config.enableLinkedInventory, val -> config.enableLinkedInventory = val);
        addEnchantToggle(enchantments, entryBuilder, "enablePsychicWall", config.enablePsychicWall, val -> config.enablePsychicWall = val);
        addEnchantToggle(enchantments, entryBuilder, "enableShepherd", config.enableShepherd, val -> config.enableShepherd = val);
        addEnchantToggle(enchantments, entryBuilder, "enableMagnetic", config.enableMagnetic, val -> config.enableMagnetic = val);
        addEnchantToggle(enchantments, entryBuilder, "enableInfamyCurse", config.enableInfamyCurse, val -> config.enableInfamyCurse = val);
        addEnchantToggle(enchantments, entryBuilder, "enableGluttonous", config.enableGluttonous, val -> config.enableGluttonous = val);
        addEnchantToggle(enchantments, entryBuilder, "enableIntimidation", config.enableIntimidation, val -> config.enableIntimidation = val);
        addEnchantToggle(enchantments, entryBuilder, "enableOreScenting", config.enableOreScenting, val -> config.enableOreScenting = val);
        addEnchantToggle(enchantments, entryBuilder, "enableMuffled", config.enableMuffled, val -> config.enableMuffled = val);
        addEnchantToggle(enchantments, entryBuilder, "enablePoisonResistance", config.enablePoisonResistance, val -> config.enablePoisonResistance = val);
        addEnchantToggle(enchantments, entryBuilder, "enableFrostFang", config.enableFrostFang, val -> config.enableFrostFang = val);
        addEnchantToggle(enchantments, entryBuilder, "enableWarpingBite", config.enableWarpingBite, val -> config.enableWarpingBite = val);
        addEnchantToggle(enchantments, entryBuilder, "enableShadowHands", config.enableShadowHands, val -> config.enableShadowHands = val);
        addEnchantToggle(enchantments, entryBuilder, "enableTetheredTeleport", config.enableTetheredTeleport, val -> config.enableTetheredTeleport = val);
        addEnchantToggle(enchantments, entryBuilder, "enableImmaturityCurse", config.enableImmaturityCurse, val -> config.enableImmaturityCurse = val);
        addEnchantToggle(enchantments, entryBuilder, "enableBlazingProtection", config.enableBlazingProtection, val -> config.enableBlazingProtection = val);
        addEnchantToggle(enchantments, entryBuilder, "enableTotalRecall", config.enableTotalRecall, val -> config.enableTotalRecall = val);
        addEnchantToggle(enchantments, entryBuilder, "enableDefusal", config.enableDefusal, val -> config.enableDefusal = val);
        addEnchantToggle(enchantments, entryBuilder, "enableVoidCloud", config.enableVoidCloud, val -> config.enableVoidCloud = val);
        addEnchantToggle(enchantments, entryBuilder, "enableUndeadCurse", config.enableUndeadCurse, val -> config.enableUndeadCurse = val);

        return builder.build();
    }

    private static void addEnchantToggle(ConfigCategory category, ConfigEntryBuilder entryBuilder, String name, boolean currentValue, Consumer<Boolean> saveConsumer) {
        category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("redomesticate.configuration." + name), currentValue)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("redomesticate.configuration." + name + ".tooltip"))
                .setSaveConsumer(saveConsumer).build());
    }
}