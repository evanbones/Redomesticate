package com.evandev.redomesticate.fabric.client;

//? if fabric {
import com.evandev.redomesticate.client.LayerManager;
import com.evandev.redomesticate.client.event.OutlineColorCallback;
import com.evandev.redomesticate.client.particle.*;
import com.evandev.redomesticate.client.registry.OreColorRegistry;
import com.evandev.redomesticate.client.render.*;
import com.evandev.redomesticate.content.entity.HighlightedBlockEntity;
import com.evandev.redomesticate.fabric.network.FabricNetworking;
import com.evandev.redomesticate.registry.ModEntities;
import com.evandev.redomesticate.registry.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;

//? if <26.1 {
import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.content.item.DeedOfOwnershipItem;
import com.evandev.redomesticate.content.item.FeatherOnAStickItem;
import com.evandev.redomesticate.registry.ModItems;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
//?}

public class RedomesticateFabricClient implements ClientModInitializer {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.CHAIN_LIGHTNING.get(), ChainLightningRender::new);
        EntityRendererRegistry.register(ModEntities.GIANT_BUBBLE.get(), GiantBubbleRender::new);
        EntityRendererRegistry.register(ModEntities.PSYCHIC_WALL.get(), PsychicWallRender::new);
        EntityRendererRegistry.register(ModEntities.HIGHLIGHTED_BLOCK.get(), HighlightedBlockRender::new);
        EntityRendererRegistry.register(ModEntities.FOLLOWING_JUKEBOX.get(), JukeboxFollowerRender::new);
        EntityRendererRegistry.register(ModEntities.RECALL_BALL.get(), RecallBallRender::new);
        EntityRendererRegistry.register(ModEntities.FEATHER.get(), FeatherRender::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.DEFLECTION_SHIELD.get(), new ParticleDeflectionShield.Factory());
        ParticleFactoryRegistry.getInstance().register(ModParticles.MAGNET.get(), ParticleMagnet.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.ZZZ.get(), ParticleZZZ.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.GIANT_POP.get(), ParticleGiantPop.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SIMPLE_BUBBLE.get(), ParticleSimpleBubble.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.VAMPIRE.get(), ParticleVampire.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SNIFF.get(), ParticleSniff.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.PSYCHIC_WALL.get(), ParticlePsychicWall.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.INTIMIDATION.get(), new ParticleIntimidation.Factory());
        ParticleFactoryRegistry.getInstance().register(ModParticles.BLIGHT.get(), ParticleBlight.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.QUESTION_MARK_PARTICLE_TYPE.get(), ParticleQuestionMark.Factory::new);

        //? if <26.1 {
        ItemProperties.register(ModItems.DEED_OF_OWNERSHIP.get(),
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bound"),
                (stack, level, entity, seed) -> DeedOfOwnershipItem.isBound(stack) ? 1.0F : 0.0F
        );

        ItemProperties.register(ModItems.FEATHER_ON_A_STICK.get(), ResourceLocation.withDefaultNamespace("cast"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                boolean isMainHand = entity.getMainHandItem() == stack;
                boolean isOffHand = entity.getOffhandItem() == stack;

                if (entity.getMainHandItem().getItem() instanceof FeatherOnAStickItem) {
                    isOffHand = false;
                }

                return (isMainHand || isOffHand) && entity instanceof Player player && player.fishing != null ? 1.0F : 0.0F;
            }
        });
        //?}

        //? if >=26.1 {
        /*LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (LayerManager.canApply(entityType)) {
                registrationHelper.register((RenderLayer) new LayerPetOverlays(entityRenderer));
            }
        });
        *///?} else {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (LayerManager.canApply(entityType) && entityType != EntityType.ENDER_DRAGON) {
                if (entityRenderer instanceof LivingEntityRenderer livingRenderer) {
                    registrationHelper.register(new LayerPetOverlays(livingRenderer));
                } else {
                    Constants.LOG.warn("Could not apply pet overlays layer to {}. Renderer is not a LivingEntityRenderer.",
                            BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
                }
            }
        });
        //?}

        OutlineColorCallback.LISTENERS.add(entity -> {
            if (entity instanceof HighlightedBlockEntity blockEntity) {
                return OreColorRegistry.getBlockColor(blockEntity.getBlockState());
            }
            return null;
        });

        FabricNetworking.initClient();
    }
}
//?}
