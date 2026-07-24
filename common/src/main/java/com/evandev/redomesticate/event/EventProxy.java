package com.evandev.redomesticate.event;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.IPetbedDataEntity;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.api.PetCommand;
import com.evandev.redomesticate.client.data.RenderData;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.content.block.PetBedBlock;
import com.evandev.redomesticate.content.block.entity.PetBedBlockEntity;
import com.evandev.redomesticate.content.entity.ChainLightningEntity;
import com.evandev.redomesticate.content.entity.FollowingJukeboxEntity;
import com.evandev.redomesticate.content.entity.GiantBubbleEntity;
import com.evandev.redomesticate.content.entity.PsychicWallEntity;
import com.evandev.redomesticate.content.entity.ai.UniversalAIManager;
import com.evandev.redomesticate.data.CollarTickTracker;
import com.evandev.redomesticate.data.ModWorldData;
import com.evandev.redomesticate.data.TeleportData;
import com.evandev.redomesticate.data.request.LanternRequest;
import com.evandev.redomesticate.data.request.RespawnRequest;
import com.evandev.redomesticate.data.trades.BuyingItemTrade;
import com.evandev.redomesticate.data.trades.EnchantItemTrade;
import com.evandev.redomesticate.data.trades.SellingItemTrade;
import com.evandev.redomesticate.data.trades.SellingRandomEnchantedBook;
import com.evandev.redomesticate.registry.*;
import com.evandev.redomesticate.util.FriendlyFireCommon;
import com.evandev.redomesticate.util.TameableUtils;
import com.evandev.redomesticate.worldgen.VillageHouseManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Predicate;

public class EventProxy {
    public static final ResourceLocation FROST_FANG_SLOW = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "frost_fang_slow");
    private static final Map<Level, CollarTickTracker> COLLAR_TICK_TRACKER_MAP = new HashMap<>();
    public static List<TeleportData> teleportingPets = new ArrayList<>();
    public static MinecraftServer currentServer;

    public static boolean onLivingDrops(LivingEntity entity) {
        return TameableUtils.isTamed(entity) && TameableUtils.getPetBedPos(entity) != null;
    }

    public static boolean canTickCollar(Entity entity) {
        if (entity.level().isClientSide()) {
            return true;
        } else {
            CollarTickTracker tracker = COLLAR_TICK_TRACKER_MAP.get(entity.level());
            return tracker == null || !tracker.isEntityBlocked(entity);
        }
    }

    public static void blockCollarTick(Entity entity) {
        if (!entity.level().isClientSide()) {
            CollarTickTracker tracker = COLLAR_TICK_TRACKER_MAP.computeIfAbsent(entity.level(), k -> new CollarTickTracker());
            tracker.addBlockedEntityTick(entity.getUUID(), 5);
        }
    }

    public static void serverStart(MinecraftServer server) {
        currentServer = server;
        RegistryAccess registryAccess = server.registryAccess();
        VillageHouseManager.addAllHouses(registryAccess);
    }

    public static boolean onTameHurt(LivingEntity livingEntity, DamageSource source) {
        if (!TameableUtils.isTamed(livingEntity)) {
            return false;
        }

        if (source.getDirectEntity() instanceof Player player && TameableUtils.isPetOf(player, livingEntity) && !player.isShiftKeyDown()) {
            return true;
        }

        return livingEntity.isBaby() && source.getEntity() instanceof Player player2 && !player2.isShiftKeyDown();
    }

    public static void onServerTick(ServerLevel level) {
        CollarTickTracker tracker = COLLAR_TICK_TRACKER_MAP.get(level);
        if (tracker != null) {
            tracker.tick();
        }

        for (final var data : teleportingPets) {
            Entity entity = data.entity();
            ServerLevel endpointWorld = data.level();
            UUID ownerUUID = data.ownerUuid();

            if (!entity.isAlive()) continue;

            Entity player = endpointWorld.getPlayerByUUID(ownerUUID);
            if (player != null) {
                Vec3 toPos = player.position();
                EntityDimensions dimensions = entity.getDimensions(entity.getPose());
                AABB suffocationBox = new AABB(-dimensions.width() / 2.0F, 0, -dimensions.width() / 2.0F, dimensions.width() / 2.0F, dimensions.height(), dimensions.width() / 2.0F);

                while (!endpointWorld.noCollision(entity, suffocationBox.move(toPos.x, toPos.y, toPos.z)) && toPos.y < 300) {
                    toPos = toPos.add(0, 1, 0);
                }

                if (entity.level().dimension() != endpointWorld.dimension()) {
                    entity.unRide();
                    entity.setLevel(endpointWorld);
                    Entity teleportedEntity = entity.getType().create(endpointWorld);
                    if (teleportedEntity != null) {
                        teleportedEntity.restoreFrom(entity);
                        teleportedEntity.moveTo(toPos.x, toPos.y, toPos.z, entity.getYRot(), entity.getXRot());
                        teleportedEntity.setYHeadRot(entity.getYHeadRot());
                        teleportedEntity.fallDistance = 0.0F;
                        teleportedEntity.setPortalCooldown();
                        endpointWorld.addFreshEntity(teleportedEntity);
                    }
                    entity.remove(Entity.RemovalReason.DISCARDED);
                } else {
                    entity.fallDistance = 0.0F;
                    ChunkPos chunkpos = new ChunkPos(BlockPos.containing(toPos.x, toPos.y, toPos.z));
                    endpointWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 0, entity.getId());
                    entity.teleportTo(toPos.x, toPos.y, toPos.z);
                    entity.setPortalCooldown();
                }
            }
        }
        teleportingPets.clear();
    }

    public static void onEntityTravelToDimension(Entity entity, Level toLevel) {
        if (entity.level() instanceof ServerLevel && entity instanceof Player player) {
            teleportNearbyPets(player, player.position(), player.position(), player.level(), toLevel);
        }
    }

    public static void onEntityTeleport(Entity entity, Vec3 prev, Vec3 target) {
        if (entity instanceof Player player) {
            teleportNearbyPets(player, prev, target, player.level(), player.level());
        }
    }

    private static void teleportNearbyPets(Player owner, Vec3 fromPos, Vec3 toPos, Level fromLevel, Level toLevel) {
        double dist = 20;
        Predicate<Entity> enchantedPet = (animal) -> animal instanceof Mob && TameableUtils.isPetOf(owner, animal) && TameableUtils.isValidTeleporter(owner, (Mob) animal);

        for (Mob entity : fromLevel.getEntitiesOfClass(Mob.class, new AABB(fromPos.x - dist, fromPos.y - dist, fromPos.z - dist, fromPos.x + dist, fromPos.y + dist, fromPos.z + dist), EntitySelector.NO_SPECTATORS.and(enchantedPet))) {
            teleportingPets.add(new TeleportData(entity, (ServerLevel) toLevel, owner.getUUID()));
        }
    }

    public static boolean onProjectileImpactEvent(Projectile projectile, HitResult hitResult) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity hit = entityHitResult.getEntity();
            if (projectile.getOwner() instanceof Player player) {
                if (TameableUtils.isPetOf(player, hit)) {
                    return true;
                }
            }
            if (TameableUtils.isTamed(hit)) {
                if (projectile instanceof AbstractArrow arrow) {
                    if (arrow.getPierceLevel() > 0) {
                        arrow.setPierceLevel((byte) 0);
                        arrow.remove(Entity.RemovalReason.DISCARDED);
                        return true;
                    }
                }
                if (TameableUtils.hasEnchant((LivingEntity) hit, ModEnchantments.DEFLECTION)) {
                    float xRot = projectile.getXRot();
                    float yRot = projectile.yRotO;
                    Vec3 vec3 = projectile.position().subtract(hit.position()).normalize().scale(hit.getBbWidth() + 0.5F);
                    Vec3 vec32 = hit.position().add(vec3);
                    hit.level().addParticle(ModParticles.DEFLECTION_SHIELD.get(), vec32.x, vec32.y, vec32.z, xRot, yRot, 0.0F);
                    projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-0.2D));
                    projectile.setYRot(yRot + 180);
                    projectile.setXRot(xRot + 180);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean onEntityMount(Entity entityBeingMounted, Entity entityMounting, boolean isDismounting) {
        return entityBeingMounted instanceof GiantBubbleEntity && isDismounting && entityBeingMounted.isAlive();
    }

    public static float onLivingDamageModifier(LivingEntity entity, DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity attacker && TameableUtils.isTamed(attacker)) {
            if (TameableUtils.hasEnchant(attacker, ModEnchantments.IMMATURITY_CURSE)) {
                return (float) Math.ceil(amount * 0.7F);
            }
        }
        return amount;
    }

    public static boolean onLivingDamage(LivingEntity entity, DamageSource source, float amount) {
        if (entity.isDamageSourceBlocked(source)) {
            return false;
        }

        var level = entity.level();
        boolean isCanceled = false;

        if (!level.isClientSide() && FriendlyFireCommon.preventAttack(entity, source, amount)) {
            isCanceled = true;
            entity.setLastHurtByMob(null);
            if (source.getEntity() instanceof LivingEntity trueSource) {
                trueSource.setLastHurtByMob(null);
            }
        }

        if (TameableUtils.isTamed(entity) && !source.is(ModDamageTypes.SIPHON)) {
            boolean flag = false;

            if (TameableUtils.hasEnchant(entity, ModEnchantments.IMMUNITY_FRAME)) {
                int enchantLvl = TameableUtils.getEnchantLevel(entity, ModEnchantments.IMMUNITY_FRAME);
                if (TameableUtils.getImmuneTime(entity) <= 0) {
                    TameableUtils.setImmuneTime(entity, 20 + enchantLvl * 20);
                } else {
                    flag = true;
                    isCanceled = true;
                }
            }

            if ((source.is(DamageTypes.DROWN) || source.is(DamageTypes.DRY_OUT)) && TameableUtils.hasEnchant(entity, ModEnchantments.AMPHIBIOUS)) {
                isCanceled = true;
                flag = true;
            }

            if (!flag && (source.is(DamageTypes.FALL) || source.is(DamageTypes.FELL_OUT_OF_WORLD)) && TameableUtils.hasEnchant(entity, ModEnchantments.VOID_CLOUD)) {
                isCanceled = true;
                flag = true;
            }

            if (!flag && TameableUtils.hasEnchant(entity, ModEnchantments.BLAZING_PROTECTION)) {
                int bars = TameableUtils.getBlazingProtectionBars(entity);
                if (bars > 0) {
                    Entity attacker = source.getEntity();
                    if (attacker instanceof LivingEntity livingAttacker && !TameableUtils.hasSameOwnerAs(livingAttacker, entity)) {
                        livingAttacker.igniteForTicks(20 * (5 + entity.getRandom().nextInt(3)));
                        livingAttacker.knockback(0.4, entity.getX() - livingAttacker.getX(), entity.getZ() - livingAttacker.getZ());
                    }
                    isCanceled = true;
                    flag = true;
                    if (attacker != null) {
                        for (int i = 0; i < 3 + entity.getRandom().nextInt(3); i++) {
                            attacker.level().addParticle(ParticleTypes.FLAME, entity.getRandomX(0.8F), entity.getRandomY(), entity.getRandomZ(0.8F), 0.0F, 0.0F, 0.0F);
                        }
                    }
                    entity.playSound(ModSounds.BLAZING_PROTECTION.get(), 1, entity.getVoicePitch());
                    TameableUtils.setBlazingProtectionBars(entity, bars - 1);
                    TameableUtils.setBlazingProtectionCooldown(entity, 600);
                }
            }

            if (!flag && TameableUtils.hasEnchant(entity, ModEnchantments.HEALTH_SIPHON)) {
                Entity owner = TameableUtils.getOwnerOf(entity);
                if (owner != null && owner.isAlive() && owner.distanceTo(entity) < 100 && owner != entity) {
                    owner.hurt(source, amount);
                    isCanceled = true;
                    flag = true;
                    entity.hurt(ModDamageTypes.causeSiphonDamage(owner.level().registryAccess()), 0.0F);
                }
            }

            if (!flag && TameableUtils.hasEnchant(entity, ModEnchantments.TOTAL_RECALL) && entity.getHealth() - amount <= 2.0D) {
                UUID owner = TameableUtils.getOwnerUUIDOf(entity);
                if (owner != null) {
                    if (entity instanceof Mob mob) {
                        mob.playAmbientSound();
                    }
                    entity.playSound(SoundEvents.ENDER_CHEST_CLOSE, 1.0F, 1.5F);
                    CompoundTag tag = new CompoundTag();
                    entity.saveWithoutId(tag);
                    entity.stopRiding();

                    isCanceled = true;
                }
            }
        }

        if (source.getEntity() instanceof LivingEntity attacker && TameableUtils.isTamed(attacker)) {
            int lightningLevel = TameableUtils.getEnchantLevel(attacker, ModEnchantments.CHAIN_LIGHTNING);
            int bubblingLevel = TameableUtils.getEnchantLevel(attacker, ModEnchantments.BUBBLING);
            int vampireLevel = TameableUtils.getEnchantLevel(attacker, ModEnchantments.VAMPIRE);

            if (lightningLevel > 0) {
                ChainLightningEntity lightning = ModEntities.CHAIN_LIGHTNING.get().create(entity.level());
                if (lightning != null) {
                    lightning.setCreatorEntityID(attacker.getId());
                    lightning.setFromEntityID(attacker.getId());
                    lightning.setToEntityID(entity.getId());
                    lightning.copyPosition(entity);
                    lightning.setChainsLeft(3 + lightningLevel * 3);
                    entity.level().addFreshEntity(lightning);
                    entity.playSound(ModSounds.CHAIN_LIGHTNING.get(), 1F, 1F);
                }
            }
            if (vampireLevel > 0) {
                if (attacker.getHealth() < attacker.getMaxHealth()) {
                    float f = Mth.clamp(amount * vampireLevel * 0.5F, 1F, 10F);
                    attacker.heal(f);
                    if (entity.level() instanceof ServerLevel) {
                        for (int i = 0; i < 5 + entity.getRandom().nextInt(3); i++) {
                            double f1 = entity.getRandomX(0.7F);
                            double f2 = entity.getY(0.4F + entity.getRandom().nextFloat() * 0.2F);
                            double f3 = entity.getRandomZ(0.7F);
                            Vec3 motion = attacker.getEyePosition().subtract(f1, f2, f3).normalize().scale(0.2F);
                            ((ServerLevel) entity.level()).sendParticles(ModParticles.VAMPIRE.get(), f1, f2, f3, 1, motion.x, motion.y, motion.z, 0.2F);
                        }
                    }
                }
            }
            if (bubblingLevel > 0) {
                if (!(entity.getRootVehicle() instanceof GiantBubbleEntity) && (entity.onGround() || entity.isInWaterOrBubble() || entity.isInLava())) {
                    GiantBubbleEntity bubble = ModEntities.GIANT_BUBBLE.get().create(entity.level());
                    if (bubble != null) {
                        bubble.copyPosition(entity);
                        entity.startRiding(bubble, true);
                        bubble.setPopsIn(bubblingLevel * 40 + 40);
                        entity.level().addFreshEntity(bubble);
                        entity.playSound(ModSounds.GIANT_BUBBLE_INFLATE.get(), 1F, 1F);
                    }
                }
            }

            if (TameableUtils.hasEnchant(attacker, ModEnchantments.FROST_FANG)) {
                entity.setTicksFrozen(entity.getTicksRequiredToFreeze() + 200);
                Vec3 vec3 = entity.getEyePosition().subtract(attacker.getEyePosition()).normalize().scale(attacker.getBbWidth() + 0.5F);
                Vec3 vec32 = attacker.getEyePosition().add(vec3);
                for (int i = 0; i < 3 + attacker.getRandom().nextInt(3); i++) {
                    float f1 = 0.2F * (attacker.getRandom().nextFloat() - 1.0F);
                    float f2 = 0.2F * (attacker.getRandom().nextFloat() - 1.0F);
                    float f3 = 0.2F * (attacker.getRandom().nextFloat() - 1.0F);
                    attacker.level().addParticle(ParticleTypes.SNOWFLAKE, vec32.x + f1, vec32.y + f2, vec32.z + f3, 0.0F, 0.0F, 0.0F);
                }
                TameableUtils.setFrozenTimeTag(entity, 60);
            }

            if (!entity.level().isClientSide && TameableUtils.hasEnchant(attacker, ModEnchantments.WARPING_BITE)) {
                for (int i = 0; i < 16; ++i) {
                    double d3 = entity.getX() + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double d4 = Mth.clamp(entity.getY() + (double) (attacker.getRandom().nextInt(16) - 8), entity.level().getMinBuildHeight(), entity.level().getMinBuildHeight() + ((ServerLevel) entity.level()).getLogicalHeight() - 1);
                    double d5 = entity.getZ() + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (entity.randomTeleport(d3, d4, d5, true)) {
                        SoundEvent soundevent = entity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                        entity.playSound(soundevent, 1.0F, 1.0F);
                        break;
                    }
                }
            }
            if (!isCanceled) {
                List<LivingEntity> nearbyHealers = TameableUtils.getNearbyHealers(entity);
                if (!nearbyHealers.isEmpty()) {
                    for (LivingEntity healer : nearbyHealers) {
                        TameableUtils.setHealingAuraImpulse(healer, true);
                    }
                }
            }
        }
        return isCanceled;
    }

    public static void onEntityHurt(LivingEntity hurtEntity, DamageSource source, float originalDamage, float newDamage) {
        if (FriendlyFireCommon.preventAttack(hurtEntity, source, newDamage)) {
            hurtEntity.setLastHurtByMob(null);
            if (source.getEntity() instanceof LivingEntity trueSource) {
                trueSource.setLastHurtByMob(null);
            }
        }
    }

    public static void onPlayerStartTracking(ServerPlayer player, Entity target) {
        if (target instanceof LivingEntity living) {
            TameableUtils.syncToPlayer(living, player);
        }
    }

    public static void onEntityJoinWorldEvent(Entity entity, Level level) {
        if (level.isClientSide()) return;

        if (entity instanceof Mob mob) {
            ITameableEntity tameable = (ITameableEntity) mob;

            if (tameable.redomesticate$isTame()) {
                UniversalAIManager.applyPetAI(mob);
                TameableUtils.onUpdateEnchants(null, mob);

                if (TameableUtils.hasEnchant(mob, ModEnchantments.HEALTH_BOOST)) {
                    mob.setHealth((float) Math.max(mob.getHealth(), TameableUtils.getSafePetHealth(mob)));
                }

                if (mob.isAlive()) {
                    ModWorldData data = ModWorldData.get(level);
                    if (data != null) {
                        data.removeMatchingLanternRequests(mob.getUUID());
                    }
                    BlockPos bedPos = TameableUtils.getPetBedPos(mob);
                    if (bedPos != null && level.hasChunkAt(bedPos)) {
                        if (level.getBlockEntity(bedPos) instanceof PetBedBlockEntity petBed) {
                            if (petBed.getOwnerUUID() == null) {
                                petBed.setOwnerUUID(mob.getUUID());
                            } else if (!mob.getUUID().equals(petBed.getOwnerUUID())) {
                                TameableUtils.removePetBedPos(mob);
                            }
                        } else {
                            TameableUtils.removePetBedPos(mob);
                        }
                    }
                }
            }
        }
    }

    public static void onEntityLeaveWorld(Entity entity, Level level) {
        if (entity instanceof LivingEntity living) {
            if (!living.level().isClientSide && living.isAlive() && TameableUtils.isTamed(living) && TameableUtils.shouldUnloadToLantern(living)) {
                UUID ownerUUID = TameableUtils.getOwnerUUIDOf(entity);
                String saveName = entity.hasCustomName() ? entity.getCustomName().getString() : "";
                ModWorldData data = ModWorldData.get(living.level());
                if (data != null) {
                    LanternRequest request = new LanternRequest(living.getUUID(), BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString(), ownerUUID, living.blockPosition(), entity.level().dayTime(), saveName);
                    data.addLanternRequest(request);
                }
            }
            if (TameableUtils.couldBeTamed(living) && TameableUtils.hasEnchant(living, ModEnchantments.HEALTH_BOOST)) {
                TameableUtils.setSafePetHealth(living, living.getHealth());
            }
        }
    }

    public static void onLivingDie(LivingEntity entity, DamageSource source) {
        if (TameableUtils.isTamed(entity)) {
            BlockPos bedPos = TameableUtils.getPetBedPos(entity);
            if (bedPos != null) {
                CompoundTag data = new CompoundTag();
                entity.saveWithoutId(data);
                String saveName = entity.hasCustomName() ? entity.getCustomName().getString() : "";
                RespawnRequest request = new RespawnRequest(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString(), TameableUtils.getPetBedDimension(entity), data, bedPos, entity.level().dayTime(), saveName);
                ModWorldData worldData = ModWorldData.get(entity.level());
                if (worldData != null) {
                    worldData.addRespawnRequest(request);
                }
                if (!entity.level().isClientSide && entity.level().getBlockEntity(bedPos) instanceof PetBedBlockEntity petBed) {
                    petBed.setOwnerUUID(null);
                }
            } else {
                if (!entity.level().isClientSide() && TameableUtils.hasCollar(entity)) {
                    Map<ResourceLocation, Integer> entityEnchantments = TameableUtils.getEnchants(entity);

                    ResourceLocation vanishingKey = ResourceLocation.withDefaultNamespace("vanishing_curse");
                    boolean hasVanishing = entityEnchantments != null && entityEnchantments.containsKey(vanishingKey);

                    if (!hasVanishing) {
                        ItemStack collar = new ItemStack(ModItems.COLLAR_TAG.get());
                        if (entityEnchantments != null) {
                            var reg = entity.level().registryAccess().registry(Registries.ENCHANTMENT);
                            if (reg.isPresent()) {
                                for (Map.Entry<ResourceLocation, Integer> entry : entityEnchantments.entrySet()) {
                                    var oneEnchant = reg.get().get(entry.getKey());
                                    if (oneEnchant != null) {
                                        collar.enchant(reg.get().wrapAsHolder(oneEnchant), entry.getValue());
                                    }
                                }
                            }
                        }
                        if (entity.hasCustomName()) {
                            collar.set(DataComponents.CUSTOM_NAME, entity.getCustomName());
                        }
                        entity.spawnAtLocation(collar);
                    }
                }
            }
            if (!(entity instanceof TamableAnimal)) {
                Entity owner = TameableUtils.getOwnerOf(entity);
                if (!entity.level().isClientSide && entity.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && owner instanceof ServerPlayer serverPlayer) {
                    serverPlayer.sendSystemMessage(entity.getCombatTracker().getDeathMessage());
                }
            }
        }
    }

    public static boolean onExplosion(Level level, Explosion explosion) {
        float dist = 30;
        Vec3 center = explosion.center();
        Vec3 bottom = center.add(-dist, -dist, -dist);
        Vec3 top = center.add(dist, dist, dist);
        Predicate<Entity> defusal = (animal) -> TameableUtils.isTamed(animal) && TameableUtils.hasEnchant((LivingEntity) animal, ModEnchantments.DEFUSAL);
        boolean flag = false;
        for (LivingEntity defuser : level.getEntitiesOfClass(LivingEntity.class, new AABB(bottom, top), EntitySelector.NO_SPECTATORS.and(defusal))) {
            float eLevel = 10 * TameableUtils.getEnchantLevel(defuser, ModEnchantments.DEFUSAL);
            if (defuser.distanceToSqr(center) <= eLevel * eLevel) {
                flag = true;
                break;
            }
        }
        if (flag) {
            float pitch = 1.5F + new Random().nextFloat();
            level.playSound(null, center.x, center.y, center.z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, pitch);
            if (level instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 5; i++) {
                    serverLevel.sendParticles(ParticleTypes.CLOUD, center.x, center.y + 1.0F, center.z, 5, 0, 0F, 0, 0.2F);
                }
            }
            return true;
        }
        return false;
    }

    public static void onBlockBreak(Level level, BlockPos pos, BlockState state, Player player) {
        if (state.getBlock() instanceof PetBedBlock) {
            if (level.getBlockEntity(pos) instanceof PetBedBlockEntity petBedBlockEntity) {
                petBedBlockEntity.removeAllRequestsFor(player);
                petBedBlockEntity.resetBedsForNearbyPets();
            }
        }
    }

    public static void onItemDespawnEvent(ItemEntity itemEntity) {
        if (itemEntity.getItem().getItem() == Items.APPLE && ModConfig.get().rottenApple) {
            if (new Random().nextFloat() < 0.1F * itemEntity.getItem().getCount()) {
                itemEntity.getItem().shrink(1);
                ItemEntity rotten = new ItemEntity(itemEntity.level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), new ItemStack(ModItems.ROTTEN_APPLE.get()));
                itemEntity.level().addFreshEntity(rotten);
            }
        }
    }

    public static void onLivingUpdate(LivingEntity entity) {
        int frozenTime = TameableUtils.getFrozenTime(entity);

        if (entity instanceof Mob pet) {
            if (TameableUtils.couldBeTamed(pet) && !canTickCollar(pet)) {
                return;
            }

            if (!pet.level().isClientSide() && TameableUtils.isTamed(pet)) {
                if (pet.tickCount % 100 == 0) {
                    BlockPos bedPos = TameableUtils.getPetBedPos(pet);
                    if (bedPos != null && pet.level().hasChunkAt(bedPos)) {
                        if (pet.level().getBlockEntity(bedPos) instanceof PetBedBlockEntity petBed) {
                            if (petBed.getOwnerUUID() == null) {
                                petBed.setOwnerUUID(pet.getUUID());
                            } else if (!pet.getUUID().equals(petBed.getOwnerUUID())) {
                                TameableUtils.removePetBedPos(pet);
                            }
                        } else {
                            TameableUtils.removePetBedPos(pet);
                        }
                    }
                }
                if (ModConfig.get().enablePetRoamingRadius) {
                    BlockPos bedPos = TameableUtils.getPetBedPos(pet);
                    boolean shouldRestrict = false;

                    if (bedPos != null) {
                        if (pet instanceof ICommandableMob commandable) {
                            shouldRestrict = commandable.redomesticate$getPetCommand() == PetCommand.WANDER;
                        } else if (pet instanceof TamableAnimal tamable) {
                            shouldRestrict = !tamable.isOrderedToSit();
                        } else {
                            shouldRestrict = true;
                        }
                    }

                    if (shouldRestrict) {
                        if (!pet.hasRestriction() || !pet.getRestrictCenter().equals(bedPos) || pet.getRestrictRadius() != ModConfig.get().petRoamingRadius) {
                            pet.restrictTo(bedPos, ModConfig.get().petRoamingRadius);
                        }
                    } else if (pet.hasRestriction()) {
                        pet.clearRestriction();
                    }
                } else if (pet.hasRestriction() && TameableUtils.getPetBedPos(pet) != null) {
                    pet.clearRestriction();
                }
            }

            if (pet instanceof IPetbedDataEntity dataEntity) {
                Map<ResourceLocation, Integer> enchants = dataEntity.redomesticate$getCachedEnchants();

                if (enchants != null && !enchants.isEmpty()) {
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.BLAZING_PROTECTION) && !entity.level().isClientSide()) {
                        int bars = TameableUtils.getBlazingProtectionBars(pet);
                        if (bars < 2 * TameableUtils.getEnchantLevel(pet, ModEnchantments.BLAZING_PROTECTION)) {
                            int cooldown = TameableUtils.getBlazingProtectionCooldown(pet);
                            if (cooldown > 0) {
                                cooldown--;
                            } else {
                                TameableUtils.setBlazingProtectionBars(pet, bars + 1);
                                cooldown = 200;
                            }
                            TameableUtils.setBlazingProtectionCooldown(pet, cooldown);
                        }
                    }

                    if (TameableUtils.hasEnchant(pet, ModEnchantments.VOID_CLOUD) && !pet.isInWaterOrBubble() && pet.fallDistance > 3.0F && !pet.onGround()) {
                        Entity owner = TameableUtils.getOwnerOf(pet);
                        boolean shouldMoveToOwnerXZ = owner != null && Math.abs(owner.getY() - pet.getY()) < 1;
                        double targetX = shouldMoveToOwnerXZ ? owner.getX() : pet.getX();
                        double targetY = Math.max(pet.level().getMinBuildHeight() + 0.5F, owner == null ? 64F : owner.getY() < pet.getY() ? owner.getY() + 0.6F : owner.getY(1.0F) + pet.getBbHeight());
                        if (owner != null && owner.getRootVehicle() == pet) {
                            targetY = Math.min(pet.level().getMinBuildHeight() + 0.5F, pet.getY() - 0.5F);
                        }
                        double targetZ = shouldMoveToOwnerXZ ? owner.getZ() : pet.getZ();
                        if (pet.verticalCollision) {
                            pet.setOnGround(true);
                            targetX += (pet.getRandom().nextFloat() - 0.5F) * 4;
                            targetZ += (pet.getRandom().nextFloat() - 0.5F) * 4;
                        }
                        Vec3 move = new Vec3(targetX - pet.getX(), targetY - pet.getY(), targetZ - pet.getZ());
                        pet.setDeltaMovement(pet.getDeltaMovement().add(move.normalize().scale(0.15D)).multiply(0.5F, 0.5F, 0.5F));
                        if (pet.level() instanceof ServerLevel) {
                            TameableUtils.setFallDistance(pet, pet.fallDistance);
                            ((ServerLevel) pet.level()).sendParticles(ParticleTypes.REVERSE_PORTAL, pet.getRandomX(1.5F), pet.getY() - pet.getRandom().nextFloat(), pet.getRandomZ(1.5F), 0, 0, -0.2F, 0, 1.0D);
                        }
                    }

                    if (TameableUtils.hasEnchant(pet, ModEnchantments.IMMUNITY_FRAME) && !entity.level().isClientSide()) {
                        int i = TameableUtils.getImmuneTime(pet);
                        if (i > 0) {
                            TameableUtils.setImmuneTime(pet, i - 1);
                        }
                    }
                    if (pet.hasEffect(MobEffects.POISON) && TameableUtils.hasEnchant(pet, ModEnchantments.POISON_RESISTANCE)) {
                        pet.removeEffect(MobEffects.POISON);
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.BLIGHT_CURSE)) {
                        TameableUtils.destroyRandomPlants(pet);
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.REJUVENATION)) {
                        TameableUtils.absorbExpOrbs(pet);
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.INFAMY_CURSE)) {
                        TameableUtils.aggroRandomMonsters(pet);
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.AMPHIBIOUS)) {
                        pet.setAirSupply(pet.getMaxAirSupply());
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.INTIMIDATION)) {
                        TameableUtils.scareRandomMonsters(pet, TameableUtils.getEnchantLevel(pet, ModEnchantments.INTIMIDATION));
                    }

                    if (TameableUtils.hasEnchant(pet, ModEnchantments.DISC_JOCKEY) && !entity.level().isClientSide && entity.tickCount % 10 == 0) {
                        boolean hasJukebox = false;
                        for (FollowingJukeboxEntity e : pet.level().getEntitiesOfClass(FollowingJukeboxEntity.class, pet.getBoundingBox().inflate(16))) {
                            if (pet.getUUID().equals(e.getFollowerUUID())) {
                                hasJukebox = true;
                                break;
                            }
                        }
                        if (!hasJukebox) {
                            FollowingJukeboxEntity follower = ModEntities.FOLLOWING_JUKEBOX.get().create(pet.level());
                            if (follower != null) {
                                follower.setFollowingUUID(pet.getUUID());
                                follower.copyPosition(pet);
                                pet.level().addFreshEntity(follower);
                            }
                        }
                    }

                    int shadowHandsLevel = TameableUtils.getEnchantLevel(pet, ModEnchantments.SHADOW_HANDS);
                    if (shadowHandsLevel > 0) {
                        RenderData.updateVisualDataForMob(entity, TameableUtils.getShadowPunchTimes(pet));
                        if (!pet.level().isClientSide()) {
                            var targetEntity = TameableUtils.getPetAttackTarget(pet);
                            Entity punching = ((targetEntity instanceof Player) || (targetEntity instanceof TamableAnimal)) ? null : targetEntity;
                            int[] punchProgress = TameableUtils.getShadowPunchTimes(pet);
                            if (punching != null && punching.isAlive() && pet.hasLineOfSight(punching) && pet.distanceTo(punching) < 16) {
                                int[] striking = TameableUtils.getShadowPunchStriking(pet);
                                if (punchProgress.length < shadowHandsLevel) {
                                    int[] clean = new int[shadowHandsLevel];
                                    TameableUtils.setShadowPunchTimes(pet, clean);
                                    TameableUtils.setShadowPunchStriking(pet, clean);
                                } else {
                                    int cooldown = TameableUtils.getShadowPunchCooldown(pet);
                                    if (cooldown <= 0) {
                                        boolean flag = false;
                                        int start = shadowHandsLevel == 1 ? 0 : pet.getRandom().nextInt(shadowHandsLevel - 1);
                                        for (int i = start; i < shadowHandsLevel; i++) {
                                            if (striking[i] == 0) {
                                                striking[i] = 1;
                                                flag = true;
                                                break;
                                            }
                                        }
                                        if (flag) {
                                            TameableUtils.setShadowPunchCooldown(pet, 5);
                                        }
                                    } else {
                                        TameableUtils.setShadowPunchCooldown(pet, cooldown - 1);
                                    }
                                    for (int i = 0; i < Math.min(shadowHandsLevel, Math.min(striking.length, punchProgress.length)); i++) {
                                        if (striking[i] != 0) {
                                            if (punchProgress[i] < 10) {
                                                punchProgress[i] = punchProgress[i] + 1;
                                            } else {
                                                punching.hurt(punching.damageSources().mobAttack(pet), Mth.clamp(shadowHandsLevel, 2, 4));
                                                striking[i] = 0;
                                            }
                                        }
                                        if (striking[i] == 0 && punchProgress[i] > 0) {
                                            punchProgress[i] = punchProgress[i] - 1;
                                        }
                                    }
                                    TameableUtils.setShadowPunchStriking(pet, striking);
                                    TameableUtils.setShadowPunchTimes(pet, punchProgress);
                                }
                            } else {
                                if (punching != null) {
                                    boolean flag = true;
                                    for (int i = 0; i < Math.min(shadowHandsLevel, punchProgress.length); i++) {
                                        if (punchProgress[i] > 0) {
                                            punchProgress[i] = punchProgress[i] - 1;
                                            flag = false;
                                        }
                                    }
                                    TameableUtils.setShadowPunchStriking(pet, new int[shadowHandsLevel]);
                                    TameableUtils.setShadowPunchTimes(pet, punchProgress);
                                    if (flag) {
                                        TameableUtils.setPetAttackTarget(pet, -1);
                                    }
                                }
                                Entity punchingTarget = null;
                                if (pet.getTarget() != null) {
                                    punchingTarget = pet.getTarget();
                                } else if (TameableUtils.getOwnerOf(pet) instanceof LivingEntity owner) {
                                    if (owner.getLastHurtByMob() != null && owner.getLastHurtByMob().isAlive() && !TameableUtils.hasSameOwnerAs(pet, owner.getLastHurtByMob())) {
                                        punchingTarget = owner.getLastHurtByMob();
                                    }
                                    if (owner.getLastHurtMob() != null && owner.getLastHurtMob().isAlive() && !TameableUtils.hasSameOwnerAs(pet, owner.getLastHurtMob())) {
                                        punchingTarget = owner.getLastHurtMob();
                                    }
                                }
                                if (punchingTarget != null && punchingTarget.isAlive()) {
                                    TameableUtils.setPetAttackTarget(pet, punchingTarget.getId());
                                }
                            }
                        }
                    }
                    int oreLvl = TameableUtils.getEnchantLevel(pet, ModEnchantments.ORE_SCENTING);
                    if (oreLvl > 0 && !entity.level().isClientSide && entity.isAlive()) {
                        int interval = 100 + Math.max(150, 550 - oreLvl * 100);
                        TameableUtils.detectRandomOres(pet, interval, 5 + oreLvl * 2, oreLvl * 50, oreLvl * 3);
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.LINKED_INVENTORY)) {
                        if (!pet.canPickUpLoot()) {
                            pet.setCanPickUpLoot(true);
                        }
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.HEALING_AURA) && !pet.level().isClientSide()) {
                        int time = TameableUtils.getHealingAuraTime(pet);
                        if (time > 0) {
                            List<LivingEntity> hurtNearby = TameableUtils.getAuraHealables(pet);
                            for (LivingEntity needsHealing : hurtNearby) {
                                if (!needsHealing.hasEffect(MobEffects.REGENERATION)) {
                                    needsHealing.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, TameableUtils.getEnchantLevel(pet, ModEnchantments.HEALING_AURA) - 1));
                                }
                            }
                            time--;
                            if (time == 0) {
                                time = -600 - pet.getRandom().nextInt(600);
                            }
                        } else if (time < 0) {
                            time++;
                        } else if ((pet.tickCount + pet.getId()) % 200 == 0 || TameableUtils.getHealingAuraImpulse(pet)) {
                            List<LivingEntity> hurtNearby = TameableUtils.getAuraHealables(pet);
                            if (!hurtNearby.isEmpty()) {
                                time = 200;
                            }
                            TameableUtils.setHealingAuraImpulse(pet, false);
                        }
                        TameableUtils.setHealingAuraTime(pet, time);
                    }
                    int psychicWallLevel = TameableUtils.getEnchantLevel(pet, ModEnchantments.PSYCHIC_WALL);
                    if (psychicWallLevel > 0 && !entity.level().isClientSide()) {
                        int cooldown = TameableUtils.getPsychicWallCooldown(pet);
                        if (cooldown > 0) {
                            TameableUtils.setPsychicWallCooldown(pet, cooldown - 1);
                        } else {
                            Entity blocking = null;
                            Entity blockingFrom = null;
                            if (pet.getTarget() != null) {
                                blocking = pet.getTarget();
                                blockingFrom = pet;
                            } else if (TameableUtils.getOwnerOf(pet) instanceof LivingEntity owner) {
                                if (owner.getLastHurtByMob() != null && owner.getLastHurtByMob().isAlive() && !TameableUtils.hasSameOwnerAs(pet, owner.getLastHurtByMob())) {
                                    blocking = owner.getLastHurtByMob();
                                    blockingFrom = owner;
                                }
                                if (owner.getLastHurtMob() != null && owner.getLastHurtMob().isAlive() && !TameableUtils.hasSameOwnerAs(pet, owner.getLastHurtMob())) {
                                    blocking = owner.getLastHurtMob();
                                    blockingFrom = owner;
                                }
                            }
                            if (blocking != null) {
                                int width = psychicWallLevel + 1;
                                float yAdditional = blocking.getBbHeight() * 0.5F + width * 0.5F;
                                Vec3 vec3 = blockingFrom.position().add(0, yAdditional, 0);
                                Vec3 vec32 = blocking.position().add(0, yAdditional, 0);
                                Vec3 vec33 = vec3.add(vec32);
                                Vec3 avg = new Vec3(vec33.x / 2F, Math.floor(vec33.y / 2F), vec33.z / 2F);
                                Vec3 rotationFrom = avg.subtract(vec3);
                                Direction dir = Direction.getNearest(rotationFrom.x, rotationFrom.y, rotationFrom.z);
                                PsychicWallEntity wall = ModEntities.PSYCHIC_WALL.get().create(pet.level());
                                if (wall != null) {
                                    wall.setPos(avg.x, avg.y, avg.z);
                                    wall.setBlockWidth(width);
                                    wall.setCreatorId(pet.getUUID());
                                    wall.setLifespan(psychicWallLevel * 100);
                                    wall.setWallDirection(dir);
                                    pet.level().addFreshEntity(wall);
                                    TameableUtils.setPsychicWallCooldown(pet, psychicWallLevel * 200 + 40);
                                }
                            }
                        }
                    }

                    int shepherdLvl = TameableUtils.getEnchantLevel(pet, ModEnchantments.SHEPHERD);
                    if (shepherdLvl > 0) {
                        TameableUtils.attractAnimals(pet, shepherdLvl * 3);
                    }
                    if (TameableUtils.hasEnchant(pet, ModEnchantments.MAGNETIC)) {
                        Entity sucking = TameableUtils.getPetAttackTarget(pet);
                        if (!pet.level().isClientSide()) {
                            if (pet.getTarget() == null || !pet.getTarget().isAlive() || pet.distanceTo(pet.getTarget()) < 0.5F + pet.getBbWidth() || pet.getRootVehicle() instanceof GiantBubbleEntity) {
                                if (TameableUtils.getPetAttackTargetID(pet) != -1) {
                                    TameableUtils.setPetAttackTarget(pet, -1);
                                }
                            } else {
                                TameableUtils.setPetAttackTarget(pet, pet.getTarget().getId());
                            }
                        } else {
                            if (sucking != null) {
                                double dist = pet.distanceTo(sucking);
                                Vec3 start = pet.position().add(0, pet.getBbHeight() * 0.5F, 0);
                                Vec3 end = sucking.position().add(0, sucking.getBbHeight() * 0.5F, 0).subtract(start);
                                for (float distStep = pet.getBbWidth() + 0.8F; distStep < (int) Math.ceil(dist); distStep++) {
                                    Vec3 vec3 = start.add(end.scale(distStep / dist));
                                    float f1 = 0.5F * (pet.getRandom().nextFloat() - 0.5F);
                                    float f2 = 0.5F * (pet.getRandom().nextFloat() - 0.5F);
                                    float f3 = 0.5F * (pet.getRandom().nextFloat() - 0.5F);
                                    pet.level().addParticle(ModParticles.MAGNET.get(), vec3.x + f1, vec3.y + f2, vec3.z + f3, 0.0F, 0.0F, 0.0F);
                                }
                            }
                        }
                        if (sucking != null) {
                            if (pet.tickCount % 15 == 0) {
                                pet.playSound(ModSounds.MAGNET_LOOP.get(), 1F, 1F);
                            }
                            pet.setDeltaMovement(pet.getDeltaMovement().multiply(0.88D, 1.0D, 0.88D));
                            Vec3 move = new Vec3(pet.getX() - sucking.getX(), pet.getY() - (double) sucking.getEyeHeight() / 2.0D - sucking.getY(), pet.getZ() - sucking.getZ());
                            sucking.setDeltaMovement(sucking.getDeltaMovement().add(move.normalize().scale(pet.onGround() ? 0.15D : 0.05D)));
                        }
                    }
                }

                if (frozenTime > 0) {
                    TameableUtils.setFrozenTimeTag(entity, frozenTime - 1);
                    AttributeInstance instance = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (instance != null) {
                        float f = -0.1F * entity.getPercentFrozen();
                        if (frozenTime > 1) {
                            AttributeModifier fangModifier = new AttributeModifier(FROST_FANG_SLOW, f, AttributeModifier.Operation.ADD_VALUE);
                            if (!instance.hasModifier(FROST_FANG_SLOW)) {
                                instance.addTransientModifier(fangModifier);
                            }
                        } else {
                            instance.removeModifier(FROST_FANG_SLOW);
                        }
                    }
                    for (int i = 0; i < 1 + entity.getRandom().nextInt(2); i++) {
                        entity.level().addParticle(ParticleTypes.SNOWFLAKE, entity.getRandomX(0.7F), entity.getRandomY(), entity.getRandomZ(0.7F), 0.0F, 0.0F, 0.0F);
                    }
                }
            }
        }
    }

    public static boolean onSetAttackTarget(Mob mob, LivingEntity target) {
        return TameableUtils.isTamed(mob) && target instanceof Player player && TameableUtils.isPetOf(player, mob);
    }

    public static ItemStack onUpdateAnvil(ItemStack left, ItemStack right, int[] outCost) {
        if (left.is(ModItems.COLLAR_TAG.get()) && !left.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty() &&
                right.is(ModItems.COLLAR_TAG.get()) && !right.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()) {

            ItemEnchantments.Mutable merged = new ItemEnchantments.Mutable(left.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
            ItemEnchantments rightEnchants = right.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

            int cost = 0;

            for (var entry : rightEnchants.entrySet()) {
                var enchantHolder = entry.getKey();
                int rightLevel = entry.getIntValue();
                int leftLevel = merged.getLevel(enchantHolder);

                int finalLevel = leftLevel == rightLevel ? rightLevel + 1 : Math.max(rightLevel, leftLevel);

                int maxLvl = enchantHolder.value().getMaxLevel();
                if (finalLevel > maxLvl) finalLevel = maxLvl;

                merged.set(enchantHolder, finalLevel);

                int multiplier = enchantHolder.value().getAnvilCost();
                cost += multiplier * finalLevel;
            }

            outCost[0] = cost;
            ItemStack copy = left.copy();
            copy.set(DataComponents.ENCHANTMENTS, merged.toImmutable());
            return copy;
        }
        return ItemStack.EMPTY;
    }

    public static void onVillagerTrades(VillagerProfession profession, Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        if (profession == ModVillagers.ANIMAL_TAMER.get()) {
            List<VillagerTrades.ItemListing> level1 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level2 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level3 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level4 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level5 = new ArrayList<>();

            level1.add(new BuyingItemTrade(Items.TROPICAL_FISH, 10, 2, 10, 2));
            level1.add(new SellingItemTrade(Items.BONE, 3, 10, 6, 4));
            level1.add(new BuyingItemTrade(Items.HAY_BLOCK, 7, 1, 9, 1));
            level1.add(new SellingItemTrade(Items.COD, 2, 7, 6, 3));
            level1.add(new SellingItemTrade(Items.EGG, 4, 2, 9, 3));
            level1.add(new SellingItemTrade(ModItems.FEATHER_ON_A_STICK.get(), 3, 1, 2, 3));

            level2.add(new SellingItemTrade(Items.TROPICAL_FISH_BUCKET, 2, 1, 6, 7));
            level2.add(new BuyingItemTrade(ModItems.COLLAR_TAG.get(), 5, 1, 12, 7));
            level2.add(new SellingItemTrade(Items.APPLE, 4, 12, 3, 7));
            level2.add(new SellingItemTrade(ModBlocks.PET_BED_BLOCKS.get(DyeColor.WHITE).get().asItem(), 2, 1, 6, 7));
            level2.add(new SellingItemTrade(ModItems.DEED_OF_OWNERSHIP.get(), 3, 1, 2, 7));

            level3.add(new SellingItemTrade(ModItems.ROTTEN_APPLE.get(), 4, 1, 1, 10));
            level3.add(new SellingItemTrade(Items.CARROT_ON_A_STICK, 3, 1, 2, 10));
            level3.add(new SellingItemTrade(Items.LEAD, 3, 2, 5, 10));
            level3.add(new SellingItemTrade(Items.LEATHER_HORSE_ARMOR, 4, 1, 3, 11));
            level3.add(new SellingItemTrade(ModBlocks.COMMAND_DRUM.get().asItem(), 2, 3, 7, 11));
            level3.add(new SellingItemTrade(Items.TADPOLE_BUCKET, 6, 1, 4, 13));
            level3.add(new EnchantItemTrade(ModItems.COLLAR_TAG.get(), 20, 2, 8, 3, 10));

            level4.add(new SellingItemTrade(Items.IRON_HORSE_ARMOR, 8, 1, 2, 15));
            level4.add(new SellingItemTrade(Items.AXOLOTL_BUCKET, 11, 1, 2, 15));
            level4.add(new SellingItemTrade(Items.TURTLE_EGG, 26, 1, 2, 15));
            level4.add(new EnchantItemTrade(ModItems.COLLAR_TAG.get(), 40, 3, 18, 3, 15));

            level5.add(new SellingItemTrade(Items.GOLDEN_HORSE_ARMOR, 13, 1, 1, 18));
            level5.add(new SellingItemTrade(Items.TURTLE_SCUTE, 21, 1, 3, 18));
            level5.add(new EnchantItemTrade(ModItems.COLLAR_TAG.get(), 50, 4, 38, 3, 20));
            level5.add(new SellingRandomEnchantedBook(15));

            trades.put(1, level1);
            trades.put(2, level2);
            trades.put(3, level3);
            trades.put(4, level4);
            trades.put(5, level5);
        }
    }
}