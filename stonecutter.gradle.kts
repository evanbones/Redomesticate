plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
    id("me.modmuss50.mod-publish-plugin") version "2.2.1" apply false
}

stonecutter active "1.21.1-fabric"

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "neoforge")

    replacements {
        string(eval(node.metadata.version, ">=26.3")) {
            replace("RegistryCodecs.homogeneousList(", "RegistryCodecs.holderSet(")
            replace("poseStack.mulPose(", "poseStack.rotate(")
            replace("matrixStackIn.mulPose(", "matrixStackIn.rotate(")
            replace("net.minecraft.core.RegistryCodecs;", "net.minecraft.core.registries.codec.RegistryCodecs;")
            replace("com.mojang.blaze3d.pipeline.", "com.mojang.renderpearl.api.pipeline.")
            replace("com.mojang.blaze3d.platform.CompareOp;", "com.mojang.renderpearl.api.pipeline.CompareOp;")
            replace("com.mojang.blaze3d.PrimitiveTopology;", "com.mojang.renderpearl.api.pipeline.PrimitiveTopology;")
            replace("com.mojang.blaze3d.vertex.VertexFormat;", "com.mojang.renderpearl.api.vertex.VertexFormat;")
            replace("PushReaction.BLOCK", "PushReaction.IMMOVEABLE")
            replace("getAttackAnim(", "getSwingAnimation(")
        }
        string(eval(node.metadata.version, ">=26.1")) {
            replace("ResourceLocation", "Identifier")
            replace("net.minecraft.Util;", "net.minecraft.util.Util;")
            replace("net.minecraft.client.renderer.RenderType;", "net.minecraft.client.renderer.rendertype.RenderType;")
            replace("net.minecraft.world.entity.animal.Cat;", "net.minecraft.world.entity.animal.feline.Cat;")
            replace("net.minecraft.world.entity.animal.Fox;", "net.minecraft.world.entity.animal.fox.Fox;")
            replace("net.minecraft.world.entity.animal.IronGolem;", "net.minecraft.world.entity.animal.golem.IronGolem;")
            replace("net.minecraft.world.entity.animal.Parrot;", "net.minecraft.world.entity.animal.parrot.Parrot;")
            replace("net.minecraft.world.entity.animal.Rabbit;", "net.minecraft.world.entity.animal.rabbit.Rabbit;")
            replace("net.minecraft.world.entity.animal.Wolf;", "net.minecraft.world.entity.animal.wolf.Wolf;")
            replace("net.minecraft.world.entity.animal.horse.AbstractHorse;", "net.minecraft.world.entity.animal.equine.AbstractHorse;")
            replace("net.minecraft.world.entity.npc.Villager;", "net.minecraft.world.entity.npc.villager.Villager;")
            replace("net.minecraft.world.entity.npc.VillagerProfession;", "net.minecraft.world.entity.npc.villager.VillagerProfession;")
            replace("net.minecraft.world.entity.npc.VillagerTrades;", "net.minecraft.world.item.trading.VillagerTrades;")
            replace("net.minecraft.world.entity.projectile.AbstractArrow;", "net.minecraft.world.entity.projectile.arrow.AbstractArrow;")
            replace("net.minecraft.world.level.GameRules;", "net.minecraft.world.level.gamerules.GameRules;")
            replace("PayloadTypeRegistry.playC2S()", "PayloadTypeRegistry.serverboundPlay()")
            replace("PayloadTypeRegistry.playS2C()", "PayloadTypeRegistry.clientboundPlay()")
            replace("ServerTickEvents.END_WORLD_TICK", "ServerTickEvents.END_LEVEL_TICK")
            replace("ServerEntityWorldChangeEvents", "ServerEntityLevelChangeEvents")
            replace("AFTER_ENTITY_CHANGE_WORLD", "AFTER_ENTITY_CHANGE_LEVEL")
            replace("POINT_OF_INTEREST_TYPE.getHolder(", "POINT_OF_INTEREST_TYPE.get(")
            replace("ParticleFactoryRegistry", "ParticleProviderRegistry")
            replace("LivingEntityFeatureRendererRegistrationCallback", "LivingEntityRenderLayerRegistrationCallback")
            replace("this.pickSprite(sprites);", "this.setSprite(sprites.get(this.random));")
            replace("public int getLightColor(float", "public int getLightCoords(float")
            replace("super.getLightColor(", "super.getLightCoords(")
            replace("this.getLightColor(", "this.getLightCoords(")
            replace("LevelRenderer.getLightColor(", "LevelRenderer.getLightCoords(")
            replace("public @NotNull ParticleRenderType getRenderType() {", "public @NotNull SingleQuadParticle.Layer getLayer() {")
            replace("return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;", "return SingleQuadParticle.Layer.TRANSLUCENT;")
            replace("return ParticleRenderType.PARTICLE_SHEET_OPAQUE;", "return SingleQuadParticle.Layer.OPAQUE;")
            replace("import net.minecraft.client.particle.ParticleRenderType;", "import net.minecraft.client.particle.SingleQuadParticle;")
            replace("double xSpeed, double ySpeed, double zSpeed) {", "double xSpeed, double ySpeed, double zSpeed, RandomSource random) {")
            replace("RenderType.entityCutoutNoCull(", "RenderTypes.entityCutout(")
            replace("RenderType.entityTranslucent(", "RenderTypes.entityTranslucent(")
            replace("RenderType.entityTranslucentEmissive(", "RenderTypes.entityTranslucentEmissive(")
            replace("RenderType.outline(", "RenderTypes.outline(")
            replace("RenderType.lightning(", "RenderTypes.lightning(")
            replace("RenderType.leash(", "RenderTypes.leash(")
            replace("RenderType.lines(", "RenderTypes.lines(")
            replace("Lnet/minecraft/world/entity/animal/Cat;", "Lnet/minecraft/world/entity/animal/feline/Cat;")
            replace("Lnet/minecraft/world/entity/animal/Parrot;", "Lnet/minecraft/world/entity/animal/parrot/Parrot;")
            replace("Lnet/minecraft/world/entity/animal/Wolf;", "Lnet/minecraft/world/entity/animal/wolf/Wolf;")
            replace("Lnet/minecraft/world/entity/animal/Rabbit\$Variant;", "Lnet/minecraft/world/entity/animal/rabbit/Rabbit\$Variant;")
            replace("FMLEnvironment.dist.", "FMLEnvironment.getDist().")
            replace("event.setCost(", "event.setXpCost(")
            replace("event.getOriginalDamage(), event.getNewDamage())", "event.getOriginalDamage(), event.getInflictedDamage())")
            replace("@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)", "@EventBusSubscriber(modid = Constants.MOD_ID)")
            replace("@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)", "@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)")
            replace("\"textures/entity/blaze.png\"", "\"textures/entity/blaze/blaze.png\"")
            replace("Direction.getNearest(", "Direction.getApproximateNearest(")
            replace("Ingredient.CODEC_NONEMPTY", "Ingredient.CODEC")
            replace("TagParser.parseTag(", "TagParser.parseCompoundFully(")
            replace("BuiltInRegistries.ENTITY_TYPE.get(", "BuiltInRegistries.ENTITY_TYPE.getValue(")
            replace("BuiltInRegistries.REGISTRY.get(", "BuiltInRegistries.REGISTRY.getValue(")
            replace(".location()", ".identifier()")
            replace("getMinBuildHeight()", "getMinY()")
            replace("getMaxBuildHeight()", "getMaxY()")
            replace("registryAccess().registryOrThrow(", "registryAccess().lookupOrThrow(")
            replace("registryAccess.registryOrThrow(", "registryAccess.lookupOrThrow(")
            replace("isInWaterOrBubble()", "isInWater()")
            replace("hasRestriction()", "hasHome()")
            replace("getRestrictCenter()", "getHomePosition()")
            replace("getRestrictRadius()", "getHomeRadius()")
            replace("clearRestriction()", "clearHome()")
            replace(".restrictTo(", ".setHomeTo(")
            replace("newMob.moveTo(", "newMob.snapTo(")
            replace("teleportedEntity.moveTo(", "teleportedEntity.snapTo(")
            replace("this.moveTo(d0", "this.snapTo(d0")
            replace("axolotl.moveTo(", "axolotl.snapTo(")
            replace("this.tamable.moveTo(", "this.tamable.snapTo(")
            replace("entity.moveTo(putAt", "entity.snapTo(putAt")
        }
    }
}

stonecutter tasks {
    order("publishModrinth")
    order("publishCurseforge")
}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}
