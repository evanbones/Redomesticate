package com.evandev.redomesticate.config;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.platform.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = Services.PLATFORM.getConfigDirectory().resolve(Constants.MOD_ID + ".json").toFile();
    private static ModConfig INSTANCE;
    private transient final Map<ResourceLocation, Boolean> enchantmentCache = new ConcurrentHashMap<>();

    // General
    public boolean trinaryCommandSystem = true;
    public boolean tameableHorse = true;
    public boolean tameableFox = true;
    public boolean swingThroughPets = true;
    public boolean rottenApple = true;
    public boolean petBedRespawns = true;
    public boolean rabbitsScareRavagers = true;
    public int petStoreVillageWeight = 17;
    public boolean disablePetTeleportation = false;
    public boolean petWontAttackWhenInjured = true;
    public double petInjuredStatusHealthRatio = 0.2D;

    // Loot Chances
    public boolean petCurseEnchantmentsLootOnly = true;
    public double blazingProtectionLootChance = 0.2D;
    public double sinisterCarrotLootChance = 0.3D;
    public double bubblingLootChance = 0.65D;
    public double vampirismLootChance = 0.22D;
    public double voidCloudLootChance = 0.19D;
    public double muffledLootChance = 0.19D;
    public double oreScentingLootChance = 0.15D;

    // Enchantments
    public boolean enableAmphibious = true;
    public boolean enableBlightCurse = true;
    public boolean enableBubbling = true;
    public boolean enableChainLightning = true;
    public boolean enableCharisma = true;
    public boolean enableDiscJockey = true;
    public boolean enableHealthBoost = true;
    public boolean enableImmunityFrame = true;
    public boolean enableFireproof = true;
    public boolean enableDeflection = true;
    public boolean enableSpeedster = true;
    public boolean enableHealingAura = true;
    public boolean enableHealthSiphon = true;
    public boolean enableRejuvenation = true;
    public boolean enableVampire = true;
    public boolean enableLinkedInventory = true;
    public boolean enablePsychicWall = true;
    public boolean enableShepherd = true;
    public boolean enableMagnetic = true;
    public boolean enableInfamyCurse = true;
    public boolean enableGluttonous = true;
    public boolean enableIntimidation = true;
    public boolean enableOreScenting = true;
    public boolean enableMuffled = true;
    public boolean enablePoisonResistance = true;
    public boolean enableFrostFang = true;
    public boolean enableWarpingBite = true;
    public boolean enableShadowHands = true;
    public boolean enableTetheredTeleport = true;
    public boolean enableImmaturityCurse = true;
    public boolean enableBlazingProtection = true;
    public boolean enableTotalRecall = true;
    public boolean enableDefusal = true;
    public boolean enableVoidCloud = true;
    public boolean enableUndeadCurse = true;
    public boolean enablePetRoamingRadius = false;
    public int petRoamingRadius = 32;

    public static ModConfig get() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
            } catch (Exception e) {
                Constants.LOG.error("Failed to load " + Constants.MOD_ID + ".json", e);
                INSTANCE = new ModConfig();
                save();
            }
        } else {
            INSTANCE = new ModConfig();
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save " + Constants.MOD_ID + ".json", e);
        }
    }

    public boolean isEnchantmentEnabled(ResourceKey<Enchantment> enchantKey) {
        return enchantKey != null && isEnchantmentEnabled(enchantKey.location());
    }

    public boolean isEnchantmentEnabled(ResourceLocation location) {
        if (location == null) return true;

        return enchantmentCache.computeIfAbsent(location, loc -> switch (location.getPath()) {
            case "amphibious" -> enableAmphibious;
            case "blight_curse" -> enableBlightCurse;
            case "bubbling" -> enableBubbling;
            case "chain_lightning" -> enableChainLightning;
            case "charisma" -> enableCharisma;
            case "disc_jockey" -> enableDiscJockey;
            case "health_boost" -> enableHealthBoost;
            case "immunity_frame" -> enableImmunityFrame;
            case "fireproof" -> enableFireproof;
            case "deflection" -> enableDeflection;
            case "speedster" -> enableSpeedster;
            case "healing_aura" -> enableHealingAura;
            case "health_siphon" -> enableHealthSiphon;
            case "rejuvenation" -> enableRejuvenation;
            case "vampire" -> enableVampire;
            case "linked_inventory" -> enableLinkedInventory;
            case "psychic_wall" -> enablePsychicWall;
            case "herding" -> enableShepherd;
            case "magnetic" -> enableMagnetic;
            case "infamy_curse" -> enableInfamyCurse;
            case "gluttonous" -> enableGluttonous;
            case "intimidation" -> enableIntimidation;
            case "ore_scenting" -> enableOreScenting;
            case "muffled" -> enableMuffled;
            case "poison_resistance" -> enablePoisonResistance;
            case "frost_fang" -> enableFrostFang;
            case "warping_bite" -> enableWarpingBite;
            case "shadow_hands" -> enableShadowHands;
            case "tethered_teleport" -> enableTetheredTeleport;
            case "immaturity_curse" -> enableImmaturityCurse;
            case "blazing_protection" -> enableBlazingProtection;
            case "total_recall" -> enableTotalRecall;
            case "defusal" -> enableDefusal;
            case "void_cloud" -> enableVoidCloud;
            case "undead_curse" -> enableUndeadCurse;
            default -> true;
        });
    }
}