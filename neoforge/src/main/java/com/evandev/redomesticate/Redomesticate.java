package com.evandev.redomesticate;

import com.evandev.redomesticate.api.taming.TamingDefinition;
import com.evandev.redomesticate.api.taming.TransformationDefinition;
import com.evandev.redomesticate.client.ClientConfigSetup;
import com.evandev.redomesticate.event.EventProxy;
import com.evandev.redomesticate.event.InteractionHandler;
import com.evandev.redomesticate.platform.NeoForgeRegistrationProvider;
import com.evandev.redomesticate.registry.NeoForgeModLootModifiers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.entity.*;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@Mod(Constants.MOD_ID)
public class Redomesticate {

    public Redomesticate(IEventBus modEventBus, ModContainer modContainer) {
        CommonClass.init();
        NeoForgeRegistrationProvider.registerAll(modEventBus);
        NeoForgeModLootModifiers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onNewDatapackRegistry);

        NeoForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist.isClient()) {
            ClientConfigSetup.register(modContainer);
        }
    }

    public void onNewDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(TamingDefinition.REGISTRY_KEY, TamingDefinition.CODEC, TamingDefinition.CODEC);
        event.dataPackRegistry(TransformationDefinition.REGISTRY_KEY, TransformationDefinition.CODEC, TransformationDefinition.CODEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        InteractionResult result = InteractionHandler.handleEntityInteraction(
                event.getEntity(),
                event.getHand(),
                event.getTarget()
        );
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        EventProxy.serverStart(event.getServer());
    }

    @SubscribeEvent
    public void onServerTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            EventProxy.onServerTick(serverLevel);
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (EventProxy.onLivingDrops(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        int[] outCost = new int[1];
        ItemStack result = EventProxy.onUpdateAnvil(event.getLeft(), event.getRight(), outCost);
        if (!result.isEmpty()) {
            event.setOutput(result);
            event.setCost(outCost[0]);
        }
    }

    @SubscribeEvent
    public void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            if (EventProxy.onSetAttackTarget(mob, event.getOriginalAboutToBeSetTarget())) {
                event.setNewAboutToBeSetTarget(null);
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingDamageEvent.Pre event) {
        float original = event.getNewDamage();
        float newDamage = EventProxy.onLivingDamageModifier(event.getEntity(), event.getSource(), original);

        if (newDamage != original) {
            event.setNewDamage(newDamage);
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingIncomingDamageEvent event) {
        if (EventProxy.onTameHurt(event.getEntity(), event.getSource()) ||
                EventProxy.onLivingDamage(event.getEntity(), event.getSource(), event.getAmount())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        if (EventProxy.onEntityMount(event.getEntityBeingMounted(), event.getEntityMounting(), event.isDismounting())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingDamagePost(LivingDamageEvent.Post event) {
        EventProxy.onEntityHurt(event.getEntity(), event.getSource(), event.getOriginalDamage(), event.getNewDamage());
    }

    @SubscribeEvent
    public void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        EventProxy.onEntityTravelToDimension(event.getEntity(), event.getEntity().getServer().getLevel(event.getDimension()));
    }

    @SubscribeEvent
    public void onEntityTeleport(EntityTeleportEvent event) {
        EventProxy.onEntityTeleport(event.getEntity(), event.getPrev(), event.getTarget());
    }

    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent event) {
        if (EventProxy.onProjectileImpactEvent(event.getProjectile(), event.getRayTraceResult())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        EventProxy.onEntityJoinWorldEvent(event.getEntity(), event.getLevel());
    }

    @SubscribeEvent
    public void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        EventProxy.onEntityLeaveWorld(event.getEntity(), event.getLevel());
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        EventProxy.onLivingDie(event.getEntity(), event.getSource());
    }

    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event) {
        EventProxy.onExplosion(event.getLevel(), event.getExplosion());
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EventProxy.onBlockBreak((ServerLevel) event.getLevel(), event.getPos(), event.getState(), event.getPlayer());
    }

    @SubscribeEvent
    public void onItemExpire(ItemExpireEvent event) {
        EventProxy.onItemDespawnEvent(event.getEntity());
    }

    @SubscribeEvent
    public void onLivingTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity living) {
            EventProxy.onLivingUpdate(living);
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        EventProxy.onVillagerTrades(event.getType(), event.getTrades());
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EventProxy.onPlayerStartTracking(player, event.getTarget());
        }
    }
}