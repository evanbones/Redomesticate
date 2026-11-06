package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.content.block.DrumBlock;
import com.evandev.redomesticate.content.block.PetBedBlock;
import com.evandev.redomesticate.content.block.WaywardLanternBlock;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.LinkedHashMap;
import java.util.function.Function;

//? if >=26.1 {
/*import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
*///?}

public class ModBlocks {

    public static final RegistrationProvider<Block> BLOCK_REGISTRY = RegistrationProvider.get(Registries.BLOCK, Constants.MOD_ID);
    public static final LinkedHashMap<DyeColor, RegistryObject<Block>> PET_BED_BLOCKS = new LinkedHashMap<>();

    public static final RegistryObject<Block> WAYWARD_LANTERN = registerBlockAndItem("wayward_lantern", WaywardLanternBlock::new);
    public static final RegistryObject<Block> COMMAND_DRUM = registerBlockAndItem("drum",
            properties -> new DrumBlock(properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1F).noOcclusion())
    );

    static {
        DyeColor[] BED_ORDER = new DyeColor[]{
                DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK,
                DyeColor.BROWN, DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW,
                DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE,
                DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK
        };

        for (DyeColor color : BED_ORDER) {
            RegistryObject<Block> blockObj = registerBlockAndItem("pet_bed_" + color.getSerializedName(),
                    properties -> new PetBedBlock(properties, color));

            PET_BED_BLOCKS.put(color, blockObj);
        }
    }

    public static RegistryObject<Block> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, ? extends Block> factory) {
        RegistryObject<Block> blockObj = BLOCK_REGISTRY.register(name, () -> factory.apply(properties(name)));
        ModItems.register(name, properties -> new BlockItem(blockObj.get(), blockItemProperties(properties)));
        return blockObj;
    }

    private static BlockBehaviour.Properties properties(String name) {
        //? if >=26.1 {
        /*return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)));
        *///?} else {
        return BlockBehaviour.Properties.of();
        //?}
    }

    private static Item.Properties blockItemProperties(Item.Properties properties) {
        //? if >=26.1 {
        /*return properties.useBlockDescriptionPrefix();
        *///?} else {
        return properties;
        //?}
    }

    public static void init() {
    }
}
