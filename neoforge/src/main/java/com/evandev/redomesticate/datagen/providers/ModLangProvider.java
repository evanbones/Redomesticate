package com.evandev.redomesticate.datagen.providers;

import com.evandev.redomesticate.LangDefinition;
import com.evandev.redomesticate.util.LangUtil;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.redomesticate", "Redomesticate");

        // Items
        add("item.redomesticate.collar_tag", "Collar Tag");
        add("item.redomesticate.rotten_apple", "Rotten Apple");
        add("item.redomesticate.sinister_carrot", "Sinister Carrot");
        add("item.redomesticate.deflection_shield", "Deflection Shield Model");
        add("item.redomesticate.magnet", "Magnet Model");
        add("item.redomesticate.feather_on_a_stick", "Feather on a Stick");
        add("item.redomesticate.deed_of_ownership", "Deed of Ownership");
        add("item.redomesticate.deed_of_ownership.desc", "Bound to %s");
        add("item.redomesticate.deed_of_ownership.desc_unbound", "Unbound");

        // Blocks
        add("block.redomesticate.pet_bed_white", "White Pet Bed");
        add("block.redomesticate.pet_bed_orange", "Orange Pet Bed");
        add("block.redomesticate.pet_bed_magenta", "Magenta Pet Bed");
        add("block.redomesticate.pet_bed_light_blue", "Light Blue Pet Bed");
        add("block.redomesticate.pet_bed_yellow", "Yellow Pet Bed");
        add("block.redomesticate.pet_bed_lime", "Lime Pet Bed");
        add("block.redomesticate.pet_bed_pink", "Pink Pet Bed");
        add("block.redomesticate.pet_bed_gray", "Gray Pet Bed");
        add("block.redomesticate.pet_bed_light_gray", "Light Gray Pet Bed");
        add("block.redomesticate.pet_bed_cyan", "Cyan Pet Bed");
        add("block.redomesticate.pet_bed_purple", "Purple Pet Bed");
        add("block.redomesticate.pet_bed_blue", "Blue Pet Bed");
        add("block.redomesticate.pet_bed_brown", "Brown Pet Bed");
        add("block.redomesticate.pet_bed_green", "Green Pet Bed");
        add("block.redomesticate.pet_bed_red", "Red Pet Bed");
        add("block.redomesticate.pet_bed_black", "Black Pet Bed");
        add("block.redomesticate.drum", "Command Drum");
        add("block.redomesticate.wayward_lantern", "Wayward Lantern");

        // Messages
        add("message.redomesticate.command_0", "%s is wandering");
        add("message.redomesticate.command_1", "%s is staying");
        add("message.redomesticate.command_2", "%s is following");
        add("message.redomesticate.drum_command_0", "Ordered %s to wander");
        add("message.redomesticate.drum_command_1", "Ordered %s to stay");
        add("message.redomesticate.drum_command_2", "Ordered %s to follow");
        add("message.redomesticate.respawn", "%s has respawned at its bed");
        add("message.redomesticate.remove_respawn", "Removed respawn bed of %s");
        add("message.redomesticate.goodbye", "%s will not respawn. Goodbye...");
        add("message.redomesticate.enchantments", "Enchantments:");
        add("message.redomesticate.set_owner", "%s is now the owner of %s");
        add("message.redomesticate.wayward_lantern_return", "%s has found its way to a nearby Wayward Lantern");
        add("message.redomesticate.bed_owned", "This bed belongs to %s");
        add("message.redomesticate.bed_claimed", "This bed is claimed");
        add("message.redomesticate.bed_vacant", "This bed is unoccupied");

        // Entities
        add("entity.minecraft.villager.redomesticate.animal_tamer", "Animal Tamer");
        add("entity.redomesticate.chain_lightning", "Lightning");
        add("entity.redomesticate.recall_ball", "Recall Orb");
        add("entity.redomesticate.feather", "Feather");
        add("entity.redomesticate.following_jukebox", "Floating Jukebox");
        add("entity.redomesticate.psychic_wall", "Psychic Wall");

        // Enchantments
        add("enchantment.redomesticate.health_boost", "Extra Health");
        add("enchantment.redomesticate.health_boost.desc", "Increases pet's max health by 10 for each level.");
        add("enchantment.redomesticate.fireproof", "Fireproof");
        add("enchantment.redomesticate.fireproof.desc", "Grants pet immunity to fire and lava damage.");
        add("enchantment.redomesticate.immunity_frame", "Immunity Frame");
        add("enchantment.redomesticate.immunity_frame.desc", "Each level makes pet immune for an additional second after attack.");
        add("enchantment.redomesticate.deflection", "Deflecting");
        add("enchantment.redomesticate.deflection.desc", "Pet is protected by a ghostly shield which deflects projectile attacks.");
        add("enchantment.redomesticate.poison_resistance", "Poison Resistance");
        add("enchantment.redomesticate.poison_resistance.desc", "Grants pet immunity to poison effect.");
        add("enchantment.redomesticate.chain_lightning", "Chain Lightning");
        add("enchantment.redomesticate.chain_lightning.desc", "Summons a lightning bolt that chains from and damages mobs each time a pet attacks, level increases how many mobs can be hit by lightning.");
        add("enchantment.redomesticate.speedster", "Speedster");
        add("enchantment.redomesticate.speedster.desc", "Increases pet's movement speed.");
        add("enchantment.redomesticate.frost_fang", "Frost Fang");
        add("enchantment.redomesticate.frost_fang.desc", "Pet attacks now slows mobs shortly and deals freezing damage.");
        add("enchantment.redomesticate.magnetic", "Magnetic");
        add("enchantment.redomesticate.magnetic.desc", "Pet aided by ghostly magnet that pulls targeted mobs towards it.");
        add("enchantment.redomesticate.linked_inventory", "Linked Inventory");
        add("enchantment.redomesticate.linked_inventory.desc", "Pet can pick up items and teleport them to owner's inventory.");
        add("enchantment.redomesticate.total_recall", "Total Recall");
        add("enchantment.redomesticate.total_recall.desc", "When below 2 health, pet enters a recall orb and is protected until released by its owner.");
        add("enchantment.redomesticate.health_siphon", "Health Siphon");
        add("enchantment.redomesticate.health_siphon.desc", "Any damage done to pet is transferred to its owner.");
        add("enchantment.redomesticate.bubbling", "Bubbling");
        add("enchantment.redomesticate.bubbling.desc", "Pet attacks trap mob inside giant bubble that floats upwards.");
        add("enchantment.redomesticate.herding", "Herding");
        add("enchantment.redomesticate.herding.desc", "Pet can be followed by wild animals, level increases maximum amount of followers.");
        add("enchantment.redomesticate.amphibious", "Amphibious");
        add("enchantment.redomesticate.amphibious.desc", "Pet cannot dry out or drown on land or in water, increases speed in water, pet will not float but swim quickly.");
        add("enchantment.redomesticate.vampire", "Vampire's Familiar");
        add("enchantment.redomesticate.vampire.desc", "Pet heals amount of damage done, level increases percentage of dealt damage healed.");
        add("enchantment.redomesticate.void_cloud", "Void Cloud");
        add("enchantment.redomesticate.void_cloud.desc", "Pet is protected from falling off cliffs or into void by a large cloud that lifts it up and returns it to its owner.");
        add("enchantment.redomesticate.charisma", "Charismatic");
        add("enchantment.redomesticate.charisma.desc", "Pet lowers trade prices for its owner, level increases the bargain.");
        add("enchantment.redomesticate.undead_curse", "Curse of Afterlife");
        add("enchantment.redomesticate.undead_curse.desc", "Pet returns as a hostile, zombie pet after death.");
        add("enchantment.redomesticate.infamy_curse", "Curse of Infamy");
        add("enchantment.redomesticate.infamy_curse.desc", "Pet can naturally make any nearby monster hostile to it.");
        add("enchantment.redomesticate.shadow_hands", "Shadow Hands");
        add("enchantment.redomesticate.shadow_hands.desc", "Pet uses dark magic to attack targets with shadowy hands, level increases speed and amount of hands.");
        add("enchantment.redomesticate.disc_jockey", "Disc Jockey");
        add("enchantment.redomesticate.disc_jockey.desc", "Pet is followed by a floating jukebox that can play music discs.");
        add("enchantment.redomesticate.defusal", "Defusal");
        add("enchantment.redomesticate.defusal.desc", "Pet can nullify explosion damage to terrain and other mobs, level increases range.");
        add("enchantment.redomesticate.warping_bite", "Warping Bite");
        add("enchantment.redomesticate.warping_bite.desc", "Pet will randomly teleport targeted mobs away from it.");
        add("enchantment.redomesticate.ore_scenting", "Ore Scenting");
        add("enchantment.redomesticate.ore_scenting.desc", "Pet can sniff out ore blocks and alert its owner, level determines distance, amount and frequency of ore detection.");
        add("enchantment.redomesticate.gluttonous", "Gluttonous");
        add("enchantment.redomesticate.gluttonous.desc", "Pet can eat any food item, regardless of dietary restrictions.");
        add("enchantment.redomesticate.psychic_wall", "Psychic Wall");
        add("enchantment.redomesticate.psychic_wall.desc", "Pet summons a wall of psychic energy during combat to provide cover, level determines size of wall and length of the effect.");
        add("enchantment.redomesticate.intimidation", "Intimidation");
        add("enchantment.redomesticate.intimidation.desc", "Pet can scare away hostile mobs with its fearsome appearance, level determines range and frequency of the effect.");
        add("enchantment.redomesticate.blight_curse", "Curse of Blight");
        add("enchantment.redomesticate.blight_curse.desc", "Plants will wither and die around the pet.");
        add("enchantment.redomesticate.tethered_teleport", "Tethered Teleport");
        add("enchantment.redomesticate.tethered_teleport.desc", "Pet will teleport with its owner, even across dimensions.");
        add("enchantment.redomesticate.immaturity_curse", "Curse of Immaturity");
        add("enchantment.redomesticate.immaturity_curse.desc", "Pet appears and behaves as a baby version of itself, with reduced attack damage.");
        add("enchantment.redomesticate.muffled", "Muffled");
        add("enchantment.redomesticate.muffled.desc", "Pet will be silent and not emit vibrations when moving.");
        add("enchantment.redomesticate.blazing_protection", "Blazing Protection");
        add("enchantment.redomesticate.blazing_protection.desc", "Pet will be protected from damage by 2 blazing bars per level, which knockback and set attackers on fire.");
        add("enchantment.redomesticate.healing_aura", "Healing Aura");
        add("enchantment.redomesticate.healing_aura.desc", "Pet will occasionally heal owner and other pets around it, level determines strength of healing effect.");
        add("enchantment.redomesticate.rejuvenation", "Rejuvenation");
        add("enchantment.redomesticate.rejuvenation.desc", "When injured, pet may absorb experience orbs to heal itself.");

        // Subtitles
        add("redomesticate.sound.subtitle.collar_tag", "Equipped collar tag");
        add("redomesticate.sound.subtitle.magnet_loop", "Magnet buzzes");
        add("redomesticate.sound.subtitle.chain_lightning", "Lightning zaps");
        add("redomesticate.sound.subtitle.giant_bubble_inflate", "Giant Bubble inflates");
        add("redomesticate.sound.subtitle.giant_bubble_pop", "Giant Bubble pops");
        add("redomesticate.sound.subtitle.pet_bed_use", "Pet Bed adjusted");
        add("redomesticate.sound.subtitle.drum", "Command Drum beats");
        add("redomesticate.sound.subtitle.psychic_wall", "Psychic Wall hums");
        add("redomesticate.sound.subtitle.psychic_wall_deflect", "Psychic Wall deflects");
        add("redomesticate.sound.subtitle.blazing_protection", "Blazing bar disappears");

        // Integrations & Misc
        add("config.jade.plugin_redomesticate.collar_tag", "Collar Description");
        add(LangUtil.conf("rotten_apple"), "Apples become rotten apples when disappearing");
        add(LangUtil.conf("rotten_apple.tooltip"), "Apples become rotten apples when disappearing");
        add(LangDefinition.has_pet_bed_at_pos, "Has Pet Bed at (%s)");
        add(LangDefinition.health_text, "Health");
        add(LangDefinition.network_failed, "Network Failed");

        // Configuration
        add("config.redomesticate.title", "Redomesticate Config");
        add("config.redomesticate.category.general", "General");
        add("config.redomesticate.category.loot", "Loot");

        add("redomesticate.configuration.trinaryCommandSystem", "Trinary Command System");
        add("redomesticate.configuration.trinaryCommandSystem.tooltip", "Allows tamed pets to be commanded to wander, sit, or follow.");
        add("redomesticate.configuration.tameableHorse", "Tameable Horses");
        add("redomesticate.configuration.tameableHorse.tooltip", "Allows Horses, Donkeys, and Llamas to use pet beds and enchantments.");
        add("redomesticate.configuration.tameableFox", "Tameable Foxes");
        add("redomesticate.configuration.tameableFox.tooltip", "Makes Foxes fully tameable via breeding.");
        add("redomesticate.configuration.swingThroughPets", "Swing Through Pets");
        add("redomesticate.configuration.swingThroughPets.tooltip", "Prevents owners from accidentally hitting their own pets during combat.");
        add("redomesticate.configuration.petBedRespawns", "Pet Bed Respawns");
        add("redomesticate.configuration.petBedRespawns.tooltip", "Allows tamed pets to respawn at their assigned Pet Bed the morning after they die.");
        add("redomesticate.configuration.rabbitsScareRavagers", "Rabbits Scare Ravagers");
        add("redomesticate.configuration.rabbitsScareRavagers.tooltip", "Restores the old mechanic where Rabbits scare away Ravagers.");
        add("redomesticate.configuration.petstore_village_weight", "Animal Tamer Village Weight");
        add("redomesticate.configuration.petstore_village_weight.tooltip", "Spawn weight of the Animal Tamer house in villages. Set to 0 to disable.");
        add("redomesticate.configuration.petCurseEnchantmentsLootOnly", "Pet Curse Enchantments Loot Only");
        add("redomesticate.configuration.petCurseEnchantmentsLootOnly.tooltip", "Restricts pet curse enchantments to loot generation only.");
        add("redomesticate.configuration.disablePetTeleportation", "Disable Pet Teleportation");
        add("redomesticate.configuration.disablePetTeleportation.tooltip", "If enabled, pets will no longer teleport to you when following from a distance.");
        add("redomesticate.configuration.petWontAttackWhenInjured", "Injured Pets Flee");
        add("redomesticate.configuration.petWontAttackWhenInjured.tooltip", "If true, tamed pets will refuse to attack dangerous enemies when their health is critically low.");
        add("redomesticate.configuration.petInjuredStatusHealthRatio", "Injured Health Threshold");
        add("redomesticate.configuration.petInjuredStatusHealthRatio.tooltip", "The health percentage (0.0 to 1.0) at which a pet is considered injured and stops attacking.");
        add("redomesticate.configuration.enablePetRoamingRadius", "Enable Pet Roaming Radius");
        add("redomesticate.configuration.enablePetRoamingRadius.tooltip", "If enabled, pets will be prevented from wandering outside a set radius from their pet bed or home position.");
        add("redomesticate.configuration.petRoamingRadius", "Pet Roaming Radius");
        add("redomesticate.configuration.petRoamingRadius.tooltip", "The maximum block distance a pet is allowed to wander from its pet bed or home position.");


        add("config.redomesticate.category.enchantments", "Enchantments");
        add("redomesticate.configuration.enableAmphibious", "Enable Amphibious");
        add("redomesticate.configuration.enableAmphibious.tooltip", "Whether the Amphibious enchantment is enabled in the game.");
        add("redomesticate.configuration.enableBlightCurse", "Enable Curse of Blight");
        add("redomesticate.configuration.enableBlightCurse.tooltip", "Whether the Curse of Blight enchantment is enabled in the game.");
        add("redomesticate.configuration.enableBubbling", "Enable Bubbling");
        add("redomesticate.configuration.enableBubbling.tooltip", "Whether the Bubbling enchantment is enabled in the game.");
        add("redomesticate.configuration.enableChainLightning", "Enable Chain Lightning");
        add("redomesticate.configuration.enableChainLightning.tooltip", "Whether the Chain Lightning enchantment is enabled in the game.");
        add("redomesticate.configuration.enableCharisma", "Enable Charisma");
        add("redomesticate.configuration.enableCharisma.tooltip", "Whether the Charisma enchantment is enabled in the game.");
        add("redomesticate.configuration.enableDiscJockey", "Enable Disc Jockey");
        add("redomesticate.configuration.enableDiscJockey.tooltip", "Whether the Disc Jockey enchantment is enabled in the game.");
        add("redomesticate.configuration.enableHealthBoost", "Enable Health Boost");
        add("redomesticate.configuration.enableHealthBoost.tooltip", "Whether the Health Boost enchantment is enabled in the game.");
        add("redomesticate.configuration.enableImmunityFrame", "Enable Immunity Frame");
        add("redomesticate.configuration.enableImmunityFrame.tooltip", "Whether the Immunity Frame enchantment is enabled in the game.");
        add("redomesticate.configuration.enableFireproof", "Enable Fireproof");
        add("redomesticate.configuration.enableFireproof.tooltip", "Whether the Fireproof enchantment is enabled in the game.");
        add("redomesticate.configuration.enableDeflection", "Enable Deflection");
        add("redomesticate.configuration.enableDeflection.tooltip", "Whether the Deflection enchantment is enabled in the game.");
        add("redomesticate.configuration.enableSpeedster", "Enable Speedster");
        add("redomesticate.configuration.enableSpeedster.tooltip", "Whether the Speedster enchantment is enabled in the game.");
        add("redomesticate.configuration.enableHealingAura", "Enable Healing Aura");
        add("redomesticate.configuration.enableHealingAura.tooltip", "Whether the Healing Aura enchantment is enabled in the game.");
        add("redomesticate.configuration.enableHealthSiphon", "Enable Health Siphon");
        add("redomesticate.configuration.enableHealthSiphon.tooltip", "Whether the Health Siphon enchantment is enabled in the game.");
        add("redomesticate.configuration.enableRejuvenation", "Enable Rejuvenation");
        add("redomesticate.configuration.enableRejuvenation.tooltip", "Whether the Rejuvenation enchantment is enabled in the game.");
        add("redomesticate.configuration.enableVampire", "Enable Vampire");
        add("redomesticate.configuration.enableVampire.tooltip", "Whether the Vampire enchantment is enabled in the game.");
        add("redomesticate.configuration.enableLinkedInventory", "Enable Linked Inventory");
        add("redomesticate.configuration.enableLinkedInventory.tooltip", "Whether the Linked Inventory enchantment is enabled in the game.");
        add("redomesticate.configuration.enablePsychicWall", "Enable Psychic Wall");
        add("redomesticate.configuration.enablePsychicWall.tooltip", "Whether the Psychic Wall enchantment is enabled in the game.");
        add("redomesticate.configuration.enableShepherd", "Enable Shepherd");
        add("redomesticate.configuration.enableShepherd.tooltip", "Whether the Shepherd enchantment is enabled in the game.");
        add("redomesticate.configuration.enableMagnetic", "Enable Magnetic");
        add("redomesticate.configuration.enableMagnetic.tooltip", "Whether the Magnetic enchantment is enabled in the game.");
        add("redomesticate.configuration.enableInfamyCurse", "Enable Curse of Infamy");
        add("redomesticate.configuration.enableInfamyCurse.tooltip", "Whether the Curse of Infamy enchantment is enabled in the game.");
        add("redomesticate.configuration.enableGluttonous", "Enable Gluttonous");
        add("redomesticate.configuration.enableGluttonous.tooltip", "Whether the Gluttonous enchantment is enabled in the game.");
        add("redomesticate.configuration.enableIntimidation", "Enable Intimidation");
        add("redomesticate.configuration.enableIntimidation.tooltip", "Whether the Intimidation enchantment is enabled in the game.");
        add("redomesticate.configuration.enableOreScenting", "Enable Ore Scenting");
        add("redomesticate.configuration.enableOreScenting.tooltip", "Whether the Ore Scenting enchantment is enabled in the game.");
        add("redomesticate.configuration.enableMuffled", "Enable Muffled");
        add("redomesticate.configuration.enableMuffled.tooltip", "Whether the Muffled enchantment is enabled in the game.");
        add("redomesticate.configuration.enablePoisonResistance", "Enable Poison Resistance");
        add("redomesticate.configuration.enablePoisonResistance.tooltip", "Whether the Poison Resistance enchantment is enabled in the game.");
        add("redomesticate.configuration.enableFrostFang", "Enable Frost Fang");
        add("redomesticate.configuration.enableFrostFang.tooltip", "Whether the Frost Fang enchantment is enabled in the game.");
        add("redomesticate.configuration.enableWarpingBite", "Enable Warping Bite");
        add("redomesticate.configuration.enableWarpingBite.tooltip", "Whether the Warping Bite enchantment is enabled in the game.");
        add("redomesticate.configuration.enableShadowHands", "Enable Shadow Hands");
        add("redomesticate.configuration.enableShadowHands.tooltip", "Whether the Shadow Hands enchantment is enabled in the game.");
        add("redomesticate.configuration.enableTetheredTeleport", "Enable Tethered Teleport");
        add("redomesticate.configuration.enableTetheredTeleport.tooltip", "Whether the Tethered Teleport enchantment is enabled in the game.");
        add("redomesticate.configuration.enableImmaturityCurse", "Enable Curse of Immaturity");
        add("redomesticate.configuration.enableImmaturityCurse.tooltip", "Whether the Curse of Immaturity enchantment is enabled in the game.");
        add("redomesticate.configuration.enableBlazingProtection", "Enable Blazing Protection");
        add("redomesticate.configuration.enableBlazingProtection.tooltip", "Whether the Blazing Protection enchantment is enabled in the game.");
        add("redomesticate.configuration.enableTotalRecall", "Enable Total Recall");
        add("redomesticate.configuration.enableTotalRecall.tooltip", "Whether the Total Recall enchantment is enabled in the game.");
        add("redomesticate.configuration.enableDefusal", "Enable Defusal");
        add("redomesticate.configuration.enableDefusal.tooltip", "Whether the Defusal enchantment is enabled in the game.");
        add("redomesticate.configuration.enableVoidCloud", "Enable Void Cloud");
        add("redomesticate.configuration.enableVoidCloud.tooltip", "Whether the Void Cloud enchantment is enabled in the game.");
        add("redomesticate.configuration.enableUndeadCurse", "Enable Afterlife Curse");
        add("redomesticate.configuration.enableUndeadCurse.tooltip", "Whether the Curse of Afterlife enchantment is enabled in the game.");

        // Loot Chances
        add("redomesticate.configuration.blazing_protection_loot_chance", "Blazing Protection Loot Chance");
        add("redomesticate.configuration.blazing_protection_loot_chance.tooltip", "Percent chance of generating a Blazing Protection book in Nether Fortresses.");
        add("redomesticate.configuration.sinister_carrot_loot_chance", "Sinister Carrot Loot Chance");
        add("redomesticate.configuration.sinister_carrot_loot_chance.tooltip", "Percent chance of generating a Sinister Carrot in Woodland Mansions.");
        add("redomesticate.configuration.bubbling_loot_chance", "Bubbling Loot Chance");
        add("redomesticate.configuration.bubbling_loot_chance.tooltip", "Percent chance of generating a Bubbling book in Buried Treasure.");
        add("redomesticate.configuration.vampirism_loot_chance", "Vampirism Loot Chance");
        add("redomesticate.configuration.vampirism_loot_chance.tooltip", "Percent chance of generating a Vampire's Familiar book in Woodland Mansions.");
        add("redomesticate.configuration.voidCloudLootChance", "Void Cloud Loot Chance");
        add("redomesticate.configuration.voidCloudLootChance.tooltip", "Percent chance of generating a Void Cloud book in End Cities.");
        add("redomesticate.configuration.muffledLootChance", "Muffled Loot Chance");
        add("redomesticate.configuration.muffledLootChance.tooltip", "Percent chance of generating a Muffled book in Ancient Cities.");
        add("redomesticate.configuration.ore_scenting_loot_chance", "Ore Scenting Loot Chance");
        add("redomesticate.configuration.ore_scenting_loot_chance.tooltip", "Percent chance of generating an Ore Scenting book in Mineshafts.");
    }
}