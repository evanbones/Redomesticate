package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import com.evandev.redomesticate.content.block.entity.DrumBlockEntity;
import com.evandev.redomesticate.content.block.entity.PetBedBlockEntity;
import com.evandev.redomesticate.content.block.entity.WaywardLanternBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class ModBlockEntities {

    public static final RegistrationProvider<BlockEntityType<?>> BE_REGISTRY = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final RegistryObject<BlockEntityType<PetBedBlockEntity>> PET_BED = BE_REGISTRY.register("pet_bed", () -> build(PetBedBlockEntity::new, ModBlocks.PET_BED_BLOCKS.values().stream().map(RegistryObject::get).toArray(Block[]::new)
    ));

     public static final RegistryObject<BlockEntityType<DrumBlockEntity>> DRUM = BE_REGISTRY.register("drum", () -> build(DrumBlockEntity::new,
             ModBlocks.COMMAND_DRUM.get()
     ));

    public static final RegistryObject<BlockEntityType<WaywardLanternBlockEntity>> WAYWARD_LANTERN = BE_REGISTRY.register("wayward_lantern", () -> build(WaywardLanternBlockEntity::new,
            ModBlocks.WAYWARD_LANTERN.get()
    ));

    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        //? if >=26.1 {
        /*return new BlockEntityType<>(factory, Set.of(blocks));
        *///?} else {
        return BlockEntityType.Builder.of(factory, blocks).build(null);
        //?}
    }

    public static void init() {}
}