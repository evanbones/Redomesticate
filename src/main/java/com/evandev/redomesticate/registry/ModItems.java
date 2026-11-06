package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.content.item.*;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.function.Function;

//? if >=26.1 {
/*import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
*///?}

public class ModItems {
    public static final RegistrationProvider<Item> ITEM_REGISTRY = RegistrationProvider.get(Registries.ITEM, Constants.MOD_ID);

    public static final RegistryObject<Item> COLLAR_TAG = register("collar_tag", CollarTagItem::new);
    public static final RegistryObject<Item> FEATHER_ON_A_STICK = register("feather_on_a_stick", FeatherOnAStickItem::new);
    public static final RegistryObject<Item> ROTTEN_APPLE = register("rotten_apple", RottenAppleItem::new);
    public static final RegistryObject<Item> SINISTER_CARROT = register("sinister_carrot", SinisterCarrotItem::new);
    public static final RegistryObject<Item> DEFLECTION_SHIELD = register("deflection_shield", InventoryOnlyItem::new);
    public static final RegistryObject<Item> MAGNET = register("magnet", InventoryOnlyItem::new);
    public static final RegistryObject<Item> DEED_OF_OWNERSHIP = register("deed_of_ownership", DeedOfOwnershipItem::new);

    public static RegistryObject<Item> register(String name, Function<Item.Properties, ? extends Item> factory) {
        return ITEM_REGISTRY.register(name, () -> factory.apply(properties(name)));
    }

    public static Item.Properties properties(String name) {
        //? if >=26.1 {
        /*return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)));
        *///?} else {
        return new Item.Properties();
        //?}
    }

    public static void init() {
    }
}
