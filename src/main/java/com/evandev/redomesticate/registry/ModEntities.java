package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.content.entity.*;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

//? if >=26.1 {
/*import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
*///?}

public class ModEntities {
    public static final RegistrationProvider<EntityType<?>> ENTITY_REGISTRY = RegistrationProvider.get(Registries.ENTITY_TYPE, Constants.MOD_ID);

    public static final RegistryObject<EntityType<ChainLightningEntity>> CHAIN_LIGHTNING = ENTITY_REGISTRY.register("chain_lightning", () -> build("chain_lightning", EntityType.Builder.of(
                    ChainLightningEntity::new,
                    MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .fireImmune()));

    public static final RegistryObject<EntityType<GiantBubbleEntity>> GIANT_BUBBLE = ENTITY_REGISTRY.register("giant_bubble", () -> build("giant_bubble", EntityType.Builder.of(
                    GiantBubbleEntity::new,
                    MobCategory.MISC)
            .sized(1.2F, 1.8F)
            .fireImmune()));

    public static final RegistryObject<EntityType<PsychicWallEntity>> PSYCHIC_WALL = ENTITY_REGISTRY.register("psychic_wall", () -> build("psychic_wall", EntityType.Builder.of(
                    PsychicWallEntity::new,
                    MobCategory.MISC)
            .sized(1F, 1F)
            .fireImmune()));

    public static final RegistryObject<EntityType<HighlightedBlockEntity>> HIGHLIGHTED_BLOCK = ENTITY_REGISTRY.register("highlighted_block", () -> build("highlighted_block", EntityType.Builder.of(
                    HighlightedBlockEntity::new,
                    MobCategory.MISC)
            .sized(1.0F, 1.0F)
            .fireImmune()));

    public static final RegistryObject<EntityType<FeatherEntity>> FEATHER = ENTITY_REGISTRY.register("feather", () -> build("feather", EntityType.Builder.<FeatherEntity>of(
                    FeatherEntity::new,
                    MobCategory.MISC)
            .sized(0.2F, 0.2F)
            .fireImmune()));

    public static final RegistryObject<EntityType<FollowingJukeboxEntity>> FOLLOWING_JUKEBOX = ENTITY_REGISTRY.register("following_jukebox", () -> build("following_jukebox", EntityType.Builder.of(
                    FollowingJukeboxEntity::new,
                    MobCategory.MISC)
            .sized(0.65F, 0.65F)
            .fireImmune()));

    public static final RegistryObject<EntityType<RecallBallEntity>> RECALL_BALL = ENTITY_REGISTRY.register("recall_ball", () -> build("recall_ball", EntityType.Builder.of(
                    RecallBallEntity::new,
                    MobCategory.MISC)
            .sized(0.8F, 0.8F)
            .fireImmune()));

    private static <T extends Entity> EntityType<T> build(String name, EntityType.Builder<T> builder) {
        //? if >=26.1 {
        /*return builder.build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)));
        *///?} else {
        return builder.build(name);
        //?}
    }

    public static void init() {
    }
}