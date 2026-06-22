package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModTags {

    // Items
    public static final TagKey<Item> COLLAR_TAG_KEY = registerItem("collar_tag_key");
    public static final TagKey<Item> PET_BED_KEY = registerItem("pet_beds");
    public static final TagKey<Item> TAME_FROGS_WITH = registerItem("tame_frogs_with");

    // Enchantments
    public static final TagKey<Enchantment> TRADABLE_ENCHANTMENT_KEY = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "trade_enchantment_book"));
    public static final TagKey<Enchantment> INFUSE_EXTRA = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enchantinginfuser:infuse_extra"));

    // Entities
    public static final TagKey<EntityType<?>> PETSTORE_FISHTANK = registerEntity("petstore_fishtank");
    public static final TagKey<EntityType<?>> PETSTORE_CAGE_0 = registerEntity("petstore_cage_0");
    public static final TagKey<EntityType<?>> PETSTORE_CAGE_1 = registerEntity("petstore_cage_1");
    public static final TagKey<EntityType<?>> PETSTORE_CAGE_2 = registerEntity("petstore_cage_2");
    public static final TagKey<EntityType<?>> PETSTORE_CAGE_3 = registerEntity("petstore_cage_3");
    public static final TagKey<EntityType<?>> REFUSES_PET_BEDS = registerEntity("refuses_pet_beds");
    public static final TagKey<EntityType<?>> INFAMY_TARGET_ATTRACTED = registerEntity("infamy_target_attracted");
    public static final TagKey<EntityType<?>> USES_BRAIN_AI = registerEntity("uses_brain_ai");
    public static final TagKey<EntityType<?>> COMMAND_WHITELIST = registerEntity("command_whitelist");

    private static TagKey<EntityType<?>> registerEntity(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static TagKey<Item> registerItem(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}