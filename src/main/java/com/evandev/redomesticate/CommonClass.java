package com.evandev.redomesticate;

import com.evandev.redomesticate.util.ChunkLoader;

import com.evandev.redomesticate.client.event.OutlineColorCallback;
import com.evandev.redomesticate.client.registry.OreColorRegistry;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.*;
import com.evandev.redomesticate.content.entity.HighlightedBlockEntity;

public class CommonClass {

    public static void init() {
        Constants.LOG.info("Initializing {} common...", Constants.MOD_NAME);

        OutlineColorCallback.LISTENERS.add(entity -> {
            if (entity instanceof HighlightedBlockEntity highlighted) {
                return OreColorRegistry.getBlockColor(highlighted.getBlockState());
            }
            return null;
        });

        ModConfig.load();

        ModSounds.init();
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        ModDamageTypes.init();
        ModBlockEntities.init();
        ModEnchantments.init();
        ModActivities.init();
        ModParticles.init();
        ModPOIs.init();
        ModVillagers.init();
        ModVillagePieces.init();
        ModCreativeTabs.init();
        ChunkLoader.init();
    }
}