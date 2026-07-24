package com.evandev.redomesticate.datagen.providers;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.registry.ModBlocks;
import com.evandev.redomesticate.registry.ModItems;
import com.evandev.redomesticate.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(ModTags.COLLAR_TAG_KEY).add(ModItems.COLLAR_TAG.get());

        this.tag(ItemTags.VANISHING_ENCHANTABLE).add(ModItems.COLLAR_TAG.get());
        this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).add(ModItems.COLLAR_TAG.get());

        Item[] petBedItems = ModBlocks.PET_BED_BLOCKS.values().stream()
                .map(blockObj -> blockObj.get().asItem())
                .toArray(Item[]::new);

        this.tag(ModTags.PET_BED_KEY).add(petBedItems);
    }

    @Override
    public @NotNull String getName() {
        return Constants.MOD_NAME + " item tags";
    }
}