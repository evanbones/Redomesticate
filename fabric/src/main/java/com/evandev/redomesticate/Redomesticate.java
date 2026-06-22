package com.evandev.redomesticate;

import com.evandev.redomesticate.api.taming.TamingDefinition;
import com.evandev.redomesticate.api.taming.TransformationDefinition;
import com.evandev.redomesticate.event.EventProxy;
import com.evandev.redomesticate.event.InteractionHandler;
import com.evandev.redomesticate.mixin.accessor.PoiTypesAccessor;
import com.evandev.redomesticate.network.FabricNetworking;
import com.evandev.redomesticate.registry.FabricModLoot;
import com.evandev.redomesticate.registry.ModPOIs;
import com.evandev.redomesticate.registry.ModVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;

public class Redomesticate implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricNetworking.initMain();
        FabricModLoot.init();

        ServerLifecycleEvents.SERVER_STARTING.register(EventProxy::serverStart);
        ServerTickEvents.END_WORLD_TICK.register(EventProxy::onServerTick);
        ServerEntityEvents.ENTITY_LOAD.register(EventProxy::onEntityJoinWorldEvent);
        ServerEntityEvents.ENTITY_UNLOAD.register(EventProxy::onEntityLeaveWorld);
        EntityTrackingEvents.START_TRACKING.register((entity, player) -> EventProxy.onPlayerStartTracking(player, entity));
        ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
            EventProxy.onEntityTravelToDimension(newEntity, destination);
        });

        DynamicRegistries.registerSynced(TamingDefinition.REGISTRY_KEY, TamingDefinition.CODEC);
        DynamicRegistries.registerSynced(TransformationDefinition.REGISTRY_KEY, TransformationDefinition.CODEC);

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            EventProxy.onBlockBreak(world, pos, state, player);
            return true;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return InteractionHandler.handleEntityInteraction(player, hand, entity);
        });

        Int2ObjectMap<List<VillagerTrades.ItemListing>> tempMap = new Int2ObjectOpenHashMap<>();
        EventProxy.onVillagerTrades(ModVillagers.ANIMAL_TAMER.get(), tempMap);

        for (int level = 1; level <= 5; level++) {
            List<VillagerTrades.ItemListing> tradesForLevel = tempMap.get(level);
            if (tradesForLevel != null) {
                TradeOfferHelper.registerVillagerOffers(ModVillagers.ANIMAL_TAMER.get(), level, factories -> {
                    factories.addAll(tradesForLevel);
                });
            }
        }

        ResourceKey<PoiType> petBedKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "pet_bed"));
        BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolder(petBedKey).ifPresent(petBedHolder -> PoiTypesAccessor.registerBlockStates(petBedHolder, ModPOIs.getBeds()));
    }
}