package com.evandev.redomesticate.client;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.client.render.LayerPetOverlays;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public class LayerManager {

    public static boolean canApply(EntityType<?> type) {
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    //? if >=26.1 {
    /*public static void addLayerIfApplicable(EntityType<?> entityType, EntityRenderer<?, ?> renderer) {
    *///?} else {
    public static void addLayerIfApplicable(EntityType<?> entityType, EntityRenderer<?> renderer) {
    //?}
        if (BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().equals("minecraft:ender_dragon")) {
            return;
        }

        try {
            if (renderer instanceof LivingEntityRenderer livingEntityRenderer) {
                livingEntityRenderer.addLayer(new LayerPetOverlays(livingEntityRenderer));
            } else {
                Constants.LOG.warn("Could not apply pet overlays layer to {}. Renderer is not a LivingEntityRenderer.",
                        BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
            }
        } catch (Exception e) {
            Constants.LOG.warn("Failed to apply pet overlays layer to {}: {}",
                    BuiltInRegistries.ENTITY_TYPE.getKey(entityType), e.getMessage());
        }
    }
}