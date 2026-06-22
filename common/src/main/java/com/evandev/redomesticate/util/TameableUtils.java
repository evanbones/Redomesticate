package com.evandev.redomesticate.util;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.IPetbedDataEntity;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.api.PetCommand;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.content.entity.HighlightedBlockEntity;
import com.evandev.redomesticate.mixin.accessor.ExperienceOrbAccessor;
import com.evandev.redomesticate.network.PropertiesMessage;
import com.evandev.redomesticate.platform.Services;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.registry.ModEntities;
import com.evandev.redomesticate.registry.ModParticles;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class TameableUtils {

    private static final String ENCHANTMENT_TAG = "StoredPetEnchantments";
    private static final String COLLAR_TAG = "HasPetCollar";
    private static final String IMMUNITY_TIME_TAG = "PetImmunityTimer";
    private static final String FROZEN_TIME_TAG = "PetFrozenTime";
    private static final String ATTACK_TARGET_ENTITY = "PetAttackTarget";
    private static final String SHADOW_PUNCH_TIMES = "PetShadowPunchTimes";
    private static final String SHADOW_PUNCH_COOLDOWN = "PetShadowPunchCooldown";
    private static final String PSYCHIC_WALL_COOLDOWN = "PetPsychicWallCooldown";
    private static final String INTIMIDATION_COOLDOWN = "PetIntimidationCooldown";
    private static final String SHADOW_PUNCH_STRIKING = "PetShadowPunchStriking";
    private static final String JUKEBOX_FOLLOWER_DISC = "PetJukeboxFollowerDisc";
    private static final String BLAZING_PROTECTION_BARS = "PetBlazingProtectionBars";
    private static final String BLAZING_PROTECTION_COOLDOWN = "PetBlazingProtectionCooldown";
    private static final String HEALING_AURA_TIME = "PetHealingAuraTime";
    private static final String HEALING_AURA_IMPULSE = "PetHealingAuraImpulse";
    private static final String HAS_PET_BED = "HasPetBed";
    private static final String PET_BED_X = "PetBedX";
    private static final String PET_BED_Y = "PetBedY";
    private static final String PET_BED_Z = "PetBedZ";
    private static final String PET_BED_DIMENSION = "PetBedDimension";
    private static final String FALL_DISTANCE_SYNC = "SyncedFallDistance";
    private static final String SAFE_PET_HEALTH = "SafePetHealth";
    private static final String COLLAR_SWAP_COOLDOWN = "CollarSwapCooldown";
    private static final ResourceLocation HEALTH_BOOST_UUID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "health_boost");
    private static final ResourceLocation SPEED_BOOST_UUID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "speed_boost");
    private static final ResourceLocation SPEED_BOOST_AQUATIC_LAND_UUID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "speed_boost_aqua");

    public static UUID getOwnerUUIDOf(Entity entity) {
        if (entity instanceof TamableAnimal tamable && tamable.getOwnerUUID() != null) {
            return tamable.getOwnerUUID();
        }
        if (entity instanceof ITameableEntity tameable) {
            return tameable.redomesticate$getTameOwnerUUID();
        }
        return null;
    }

    public static boolean hasCollar(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.contains(COLLAR_TAG) && tag.getBoolean(COLLAR_TAG);
    }

    public static boolean shouldUnloadToLantern(LivingEntity tameable) {
        if (tameable instanceof ICommandableMob commandableMob) {
            return commandableMob.redomesticate$getPetCommand() == PetCommand.FOLLOW;
        } else {
            CompoundTag tag = new CompoundTag();
            tameable.addAdditionalSaveData(tag);
            int command = -1;
            // compat with alexs mobs
            for (String s : tag.getAllKeys()) {
                if (s.endsWith("Command") && tag.contains(s, 1)) {
                    command = tag.getInt(s);
                }
            }
            if (command != -1) {
                return command == 1;
            } else if (tameable instanceof TamableAnimal animal) {
                return !animal.isOrderedToSit();
            }
        }
        return false;
    }

    public static double getSafePetHealth(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getDouble(SAFE_PET_HEALTH);
    }

    public static void setSafePetHealth(LivingEntity enchanted, double health) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putDouble(SAFE_PET_HEALTH, health);
        sync(enchanted, tag);
    }

    public static boolean isTamed(Entity entity) {
        if (entity instanceof TamableAnimal tamable && tamable.isTame()) {
            return true;
        }
        if (entity instanceof ITameableEntity tameable) {
            return tameable.redomesticate$isTame();
        }
        return false;
    }

    public static boolean isPetOf(Player player, Entity entity) {
        return entity != null && (entity.isAlliedTo(player) || hasSameOwnerAsOneWay(entity, player));
    }

    private static boolean hasSameOwnerAsOneWay(Entity tameable, Entity target) {
        Entity owner1 = getOwnerOf(tameable);
        if (owner1 == null) return false;

        Entity owner2 = getOwnerOf(target);
        if (owner1.equals(owner2)) {
            return true;
        }

        return owner1.equals(target);
    }

    @Nullable
    public static BlockPos getPetBedPos(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        if (tag.getBoolean(HAS_PET_BED) && tag.contains(PET_BED_X) && tag.contains(PET_BED_Y) && tag.contains(PET_BED_Z)) {
            return new BlockPos(tag.getInt(PET_BED_X), tag.getInt(PET_BED_Y), tag.getInt(PET_BED_Z));
        }
        return null;
    }

    public static void setPetBedPos(LivingEntity enchanted, BlockPos petBed) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putBoolean(HAS_PET_BED, true);
        tag.putInt(PET_BED_X, petBed.getX());
        tag.putInt(PET_BED_Y, petBed.getY());
        tag.putInt(PET_BED_Z, petBed.getZ());
        sync(enchanted, tag);
    }

    public static void removePetBedPos(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putBoolean(HAS_PET_BED, false);
        sync(enchanted, tag);
    }

    public static String getPetBedDimension(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return !tag.contains(PET_BED_DIMENSION) ? "minecraft:overworld" : tag.getString(PET_BED_DIMENSION);
    }

    public static void setPetBedDimension(LivingEntity enchanted, String dimension) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putString(PET_BED_DIMENSION, dimension);
        sync(enchanted, tag);
    }

    public static Entity getOwnerOf(Entity entity) {
        if (entity instanceof TamableAnimal tamable && tamable.getOwner() != null) {
            return tamable.getOwner();
        }
        if (entity instanceof ITameableEntity tameable) {
            return tameable.redomesticate$getTameOwner();
        }
        return null;
    }

    public static int getBlazingProtectionBars(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getInt(BLAZING_PROTECTION_BARS);
    }

    public static int getBlazingProtectionCooldown(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getInt(BLAZING_PROTECTION_COOLDOWN);
    }

    public static void setBlazingProtectionCooldown(LivingEntity enchanted, int time) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(BLAZING_PROTECTION_COOLDOWN, time);
        sync(enchanted, tag);
    }

    public static void setBlazingProtectionBars(LivingEntity enchanted, int time) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(BLAZING_PROTECTION_BARS, time);
        sync(enchanted, tag);
    }

    private static void sync(LivingEntity entity, CompoundTag tag) {
        ModEntityData.setEntityTag(entity, tag);
        PropertiesMessage msg = new PropertiesMessage(Constants.ENTITY_DATA_TAG_UPDATE, tag.copy(), entity.getId());
        if (!entity.level().isClientSide()) {
            Services.PLATFORM.sendToAllPlayers(msg, ResourceLocation.parse(Constants.ENTITY_DATA_TAG_UPDATE));
        } else {
            Services.PLATFORM.sendToServer(msg, ResourceLocation.parse(Constants.ENTITY_DATA_TAG_UPDATE));
        }
    }

    public static void syncToPlayer(LivingEntity entity, ServerPlayer player) {
        CompoundTag tag = ModEntityData.getEntityTag(entity);
        if (tag != null && !tag.isEmpty()) {
            PropertiesMessage msg = new PropertiesMessage(Constants.ENTITY_DATA_TAG_UPDATE, tag.copy(), entity.getId());
            Services.PLATFORM.sendToPlayer(player, msg, ResourceLocation.parse(Constants.ENTITY_DATA_TAG_UPDATE));
        }
    }

    public static void clearEnchants(LivingEntity entity) {
        setEnchantmentTag(entity, new ListTag());
    }

    private static void setEnchantmentTag(LivingEntity enchanted, ListTag enchants) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        Map<ResourceLocation, Integer> prevEnchants = getEnchants(enchanted);
        tag.put(ENCHANTMENT_TAG, enchants);
        tag.putInt(COLLAR_SWAP_COOLDOWN, 20);
        tag.putBoolean(COLLAR_TAG, true);

        if (enchanted instanceof IPetbedDataEntity dataEntity) {
            dataEntity.redomesticate$setCachedEnchants(getEnchants(enchanted));
        }

        sync(enchanted, tag);
        onUpdateEnchants(prevEnchants, enchanted);
    }

    private static boolean isWaterCreature(LivingEntity enchanted) {
        return enchanted.getType().getCategory() == MobCategory.WATER_CREATURE || enchanted.getType().getCategory() == MobCategory.UNDERGROUND_WATER_CREATURE || enchanted.getType().getCategory() == MobCategory.WATER_AMBIENT;
    }

    public static void onUpdateEnchants(@Nullable Map<ResourceLocation, Integer> prevEnchants, LivingEntity enchanted) {
        int healthExtra = getEnchantLevel(enchanted, ModEnchantments.HEALTH_BOOST);
        int speedExtra = getEnchantLevel(enchanted, ModEnchantments.SPEEDSTER);
        boolean amphib = hasEnchant(enchanted, ModEnchantments.AMPHIBIOUS) && !enchanted.isInWaterOrBubble() && isWaterCreature(enchanted);
        AttributeInstance health = enchanted.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance speed = enchanted.getAttribute(Attributes.MOVEMENT_SPEED);
        if (hasEnchant(enchanted, ModEnchantments.IMMATURITY_CURSE) || prevEnchants != null && prevEnchants.containsKey(ModEnchantments.IMMATURITY_CURSE.registry())) {
            //change pose to update client
            enchanted.setPose(Pose.FALL_FLYING);
            AgeableMob ageable = (AgeableMob) enchanted;
            ageable.setBaby(true);
            enchanted.refreshDimensions();
        }

        if (health != null) {
            AttributeModifier healthBoostPetUpgrade = new AttributeModifier(HEALTH_BOOST_UUID, healthExtra * 10, AttributeModifier.Operation.ADD_VALUE);

            if (healthExtra > 0) {
                if (health.hasModifier(healthBoostPetUpgrade.id())) {
                    health.removeModifier(healthBoostPetUpgrade);
                    health.addTransientModifier(healthBoostPetUpgrade);
                } else {
                    health.addTransientModifier(healthBoostPetUpgrade);
                }
            } else {
                health.removeModifier(healthBoostPetUpgrade);
            }
        }
        if (speed != null) {
            AttributeModifier speedsterPetUpgrade = new AttributeModifier(SPEED_BOOST_UUID, speedExtra * 0.075F, AttributeModifier.Operation.ADD_VALUE);

            if (speedExtra > 0) {
                if (speed.hasModifier(speedsterPetUpgrade.id())) {
                    speed.removeModifier(speedsterPetUpgrade);
                    speed.addTransientModifier(speedsterPetUpgrade);
                } else {
                    speed.addTransientModifier(speedsterPetUpgrade);
                }
            } else {
                speed.removeModifier(speedsterPetUpgrade);
            }
            AttributeModifier speed_aqua = new AttributeModifier(SPEED_BOOST_AQUATIC_LAND_UUID, 0.13F, AttributeModifier.Operation.ADD_VALUE);

            if (amphib) {
                if (speed.hasModifier(speed_aqua.id())) {
                    speed.removeModifier(speed_aqua);
                    speed.addTransientModifier(speed_aqua);
                } else {
                    speed.addTransientModifier(speed_aqua);
                }
            } else {
                speed.removeModifier(speed_aqua);
            }
        }
    }

    public static boolean hasEnchant(LivingEntity entity, ResourceKey<Enchantment> enchantment) {
        return getEnchantLevel(entity, enchantment) > 0;
    }

    public static int getEnchantLevel(LivingEntity entity, ResourceKey<Enchantment> enchantment) {
        if (!ModConfig.get().isEnchantmentEnabled(enchantment)) return 0;
        if (entity instanceof IPetbedDataEntity dataEntity) {
            Map<ResourceLocation, Integer> cache = dataEntity.redomesticate$getCachedEnchants();
            if (cache != null && cache.containsKey(enchantment.location())) {
                return cache.get(enchantment.location());
            }
        }
        return 0;
    }

    @Nullable
    private static ListTag getEnchantmentList(LivingEntity entity) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(entity);
        if (tag.contains(ENCHANTMENT_TAG)) {
            return tag.getList(ENCHANTMENT_TAG, 10);
        }
        return null;
    }

    public static List<Component> getEnchantDescriptions(LivingEntity entity) {
        List<Component> list = new ArrayList<>();
        list.add(Component.literal("   ").append(Component.translatable("message.redomesticate.enchantments").withStyle(ChatFormatting.GOLD)));
        Map<ResourceLocation, Integer> map = getEnchants(entity);
        if (map != null) {
            for (Map.Entry<ResourceLocation, Integer> entry : map.entrySet()) {

                boolean isCurse = entry.getKey().getPath().contains("curse");
                list.add(Component.translatable("enchantment." + entry.getKey().getNamespace() + "." + entry.getKey().getPath()).append(Component.literal(" ")).append(Component.translatable("enchantment.level." + entry.getValue())).withStyle(isCurse ? ChatFormatting.RED : ChatFormatting.AQUA));

            }
        }
        return list;
    }

    public static Map<ResourceLocation, Integer> getEnchants(LivingEntity entity) {
        ListTag listtag = getEnchantmentList(entity);
        if (listtag == null) {
            return null;
        }
        Map<ResourceLocation, Integer> enchants = new HashMap<>();
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            ResourceLocation res = ResourceLocation.parse(compoundtag.getString("id"));

            enchants.put(res, compoundtag.getInt("lvl"));

        }
        return enchants;
    }

    public static CompoundTag storeEnchantment(@Nullable ResourceLocation pId, int pLevel) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString("id", String.valueOf(pId));
        compoundtag.putShort("lvl", (short) pLevel);
        return compoundtag;
    }

    public static void addEnchant(LivingEntity entity, ItemEnchantments itemEnchantments) {
        ListTag listTag = new ListTag();


        for (var entry : itemEnchantments.entrySet()) {
            var en = entry.getKey().unwrapKey().orElseThrow().location();
            var compound = storeEnchantment(en, entry.getIntValue());
            listTag.add(compound);
        }
        setEnchantmentTag(entity, listTag);

    }

    public static boolean hasSameOwnerAs(LivingEntity tameable, Entity target) {
        return hasSameOwnerAsOneWay(tameable, target) || hasSameOwnerAsOneWay(target, tameable);
    }

    public static void absorbExpOrbs(LivingEntity living) {
        if (living.getHealth() < living.getMaxHealth() && !living.level().isClientSide()) {
            for (ExperienceOrb experienceorb : living.level().getEntitiesOfClass(ExperienceOrb.class, living.getBoundingBox().inflate(3D))) {
                if (living.getHealth() >= living.getMaxHealth()) {
                    break;
                }
                Vec3 vec3 = new Vec3(living.getX() - experienceorb.getX(), living.getY() + (double) living.getEyeHeight() / 2.0D - experienceorb.getY(), living.getZ() - experienceorb.getZ());
                double d0 = vec3.lengthSqr();
                if (d0 < 2.0D) {
                    if (!living.isDeadOrDying()) {
                        int orbValue = ((ExperienceOrbAccessor) experienceorb).redomesticate$getValue();
                        float h = living.getHealth() + orbValue;
                        living.setHealth(h);

                        if (h - living.getMaxHealth() > 0) {
                            ((ExperienceOrbAccessor) experienceorb).redomesticate$setValue((int) Math.floor(h - living.getMaxHealth()));
                            break;
                        } else {
                            experienceorb.discard();
                        }
                    }
                }
                if (d0 < 64.0D) {
                    double d1 = 1.0D - Math.sqrt(d0) / 8.0D;
                    experienceorb.setDeltaMovement(experienceorb.getDeltaMovement().add(vec3.normalize().scale(d1 * d1 * 0.5D)));
                }
            }
        }
    }

    public static int getHealingAuraTime(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getInt(HEALING_AURA_TIME);
    }

    public static void setHealingAuraTime(LivingEntity enchanted, int time) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(HEALING_AURA_TIME, time);
        sync(enchanted, tag);
    }

    public static boolean getHealingAuraImpulse(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getBoolean(HEALING_AURA_IMPULSE);
    }

    public static void setHealingAuraImpulse(LivingEntity enchanted, boolean impulse) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putBoolean(HEALING_AURA_IMPULSE, impulse);
        sync(enchanted, tag);
    }

    public static List<LivingEntity> getAuraHealables(LivingEntity pet) {
        return pet.level().getEntitiesOfClass(LivingEntity.class, pet.getBoundingBox().inflate(4, 4, 4),
                e -> !e.isSpectator() && hasSameOwnerAs(e, pet) && e.distanceTo(pet) < 4 && e.getHealth() < e.getMaxHealth()
        );
    }

    public static int getPsychicWallCooldown(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getInt(PSYCHIC_WALL_COOLDOWN);
    }

    public static void setPsychicWallCooldown(LivingEntity enchanted, int time) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(PSYCHIC_WALL_COOLDOWN, time);
        sync(enchanted, tag);
    }

    public static void attractAnimals(LivingEntity attractor, int max) {
        if ((attractor.tickCount + attractor.getId()) % 8 == 0) {
            Predicate<Entity> notOnTeam = (animal) -> !hasSameOwnerAs((LivingEntity) animal, attractor) && animal.distanceTo(attractor) > 3 + attractor.getBbWidth() * 1.6F;
            List<Animal> list = attractor.level().getEntitiesOfClass(Animal.class, attractor.getBoundingBox().inflate(16, 8, 16), EntitySelector.NO_SPECTATORS.and(notOnTeam));
            list.sort(Comparator.comparingDouble(attractor::distanceToSqr));
            for (int i = 0; i < Math.min(max, list.size()); i++) {
                Animal e = list.get(i);
                e.setTarget(null);
                e.setLastHurtByMob(null);
                e.getNavigation().moveTo(attractor, 1.1D);
            }

        }
    }

    public static int getPetAttackTargetID(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return !tag.contains(ATTACK_TARGET_ENTITY) ? -1 : tag.getInt(ATTACK_TARGET_ENTITY);
    }

    @Nullable
    public static Entity getPetAttackTarget(LivingEntity enchanted) {
        int i = getPetAttackTargetID(enchanted);
        return i == -1 ? null : enchanted.level().getEntity(i);
    }

    public static void setPetAttackTarget(LivingEntity enchanted, int id) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(ATTACK_TARGET_ENTITY, id);
        sync(enchanted, tag);
    }

    public static void aggroRandomMonsters(LivingEntity attractor) {
        if ((attractor.tickCount + attractor.getId()) % 400 == 0) {
            Predicate<Entity> notOnTeamAndMonster = (animal) -> animal instanceof Enemy && !hasSameOwnerAs((LivingEntity) animal, attractor) && animal.distanceTo(attractor) > 3 + attractor.getBbWidth() * 1.6F;
            List<Mob> list = attractor.level().getEntitiesOfClass(Mob.class, attractor.getBoundingBox().inflate(20, 8, 20), EntitySelector.NO_SPECTATORS.and(notOnTeamAndMonster));
            list.sort(Comparator.comparingDouble(attractor::distanceToSqr));
            if (!list.isEmpty()) {
                list.getFirst().setTarget(attractor);
            }
        }
    }

    public static int getIntimidationCooldown(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getInt(INTIMIDATION_COOLDOWN);
    }

    public static void setIntimidationCooldown(LivingEntity enchanted, int time) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(INTIMIDATION_COOLDOWN, time);
        sync(enchanted, tag);
    }

    public static void scareRandomMonsters(LivingEntity scary, int level) {
        boolean interval = (scary.tickCount + scary.getId()) % Math.max(140, 600 - level * 200) == 0;
        if (interval || scary.hurtTime == 4 || getIntimidationCooldown(scary) > 0) {
            Predicate<Entity> notOnTeamAndMonster = (animal) -> animal instanceof Monster && !hasSameOwnerAs((LivingEntity) animal, scary) && animal.distanceTo(scary) > 3 + scary.getBbWidth() * 1.6F;
            List<PathfinderMob> list = scary.level().getEntitiesOfClass(PathfinderMob.class, scary.getBoundingBox().inflate(10 * level, 8 * level, 10 * level), EntitySelector.NO_SPECTATORS.and(notOnTeamAndMonster));
            list.sort(Comparator.comparingDouble(scary::distanceToSqr));
            if (!list.isEmpty()) {
                if (getIntimidationCooldown(scary) > 0 && !interval) {
                    setIntimidationCooldown(scary, getIntimidationCooldown(scary) - 1);
                } else {
                    Vec3 rots = list.getFirst().getEyePosition().subtract(scary.getEyePosition()).normalize();
                    float f = Mth.sqrt((float) (rots.x * rots.x + rots.z * rots.z));
                    double yRot = Math.atan2(-rots.z, -rots.x) * (double) (180F / (float) Math.PI) + 90F;
                    double xRot = Math.atan2(-rots.y, f) * (double) (180F / (float) Math.PI);
                    scary.level().addParticle(ModParticles.INTIMIDATION.get(), scary.getX(), scary.getY(), scary.getZ(), scary.getId(), xRot, yRot);
                    setIntimidationCooldown(scary, 70 * level);
                    if (scary instanceof Mob) {
                        ((Mob) scary).playAmbientSound();
                    }
                }
                for (PathfinderMob monster : list) {
                    Vec3 vec = LandRandomPos.getPosAway(monster, 11 * level, 7, scary.position());
                    if (vec != null) {
                        monster.getNavigation().moveTo(vec.x, vec.y, vec.z, 1.5D);
                    }
                }
            }
        }
    }

    public static boolean couldBeTamed(Entity entity) {
        return entity instanceof ITameableEntity || entity instanceof TamableAnimal;
    }

    public static void destroyRandomPlants(LivingEntity living) {
        if ((living.tickCount + living.getId()) % 200 == 0) {
            int range = 2;
            List<BlockPos> plants = new ArrayList<>();
            List<BlockPos> grasses = new ArrayList<>();
            BlockPos blockpos = living.blockPosition();
            int half = range / 2;
            RandomSource r = living.getRandom();
            for (int i = 0; i <= half && i >= -half; i = (i <= 0 ? 1 : 0) - i) {
                for (int j = 0; j <= range && j >= -range; j = (j <= 0 ? 1 : 0) - j) {
                    for (int k = 0; k <= range && k >= -range; k = (k <= 0 ? 1 : 0) - k) {
                        BlockPos offset = blockpos.offset(j, i, k);
                        BlockState state = living.level().getBlockState(offset);
                        if (!state.isAir() && r.nextInt(4) == 0) {
                            if (state.is(BlockTags.FLOWERS) || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(BlockTags.CROPS)) {
                                plants.add(offset);
                            } else if (state.is(BlockTags.DIRT) && !state.is(Blocks.DIRT) && !state.is(Blocks.COARSE_DIRT) || state.is(Blocks.FARMLAND)) {
                                grasses.add(offset);
                            }
                        }
                    }
                }
            }
            for (BlockPos plant : plants) {
                living.level().setBlockAndUpdate(plant, Blocks.AIR.defaultBlockState());
                for (int i = 0; i < 1 + r.nextInt(2); i++) {
                    living.level().addParticle(ModParticles.BLIGHT.get(), plant.getX() + r.nextFloat(), plant.getY() + r.nextFloat(), plant.getZ() + r.nextFloat(), 0, 0.08F, 0);
                }
            }
            for (BlockPos dirt : grasses) {
                living.level().setBlockAndUpdate(dirt, r.nextBoolean() ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState());
                for (int i = 0; i < 1 + r.nextInt(2); i++) {
                    living.level().addParticle(ModParticles.BLIGHT.get(), dirt.getX() + r.nextFloat(), dirt.getY() + 1, dirt.getZ() + r.nextFloat(), 0, 0.08F, 0);
                }
            }
        }
    }

    public static void detectRandomOres(LivingEntity attractor, int interval, int range, int effectLength, int maxOres) {
        int tick = (attractor.tickCount + attractor.getId()) % interval;
        if (tick <= 30) {
            attractor.xRotO = attractor.getXRot();
            attractor.setXRot((float) Math.sin(tick * 0.6F) * 30F);
            Vec3 look = attractor.getEyePosition().add(attractor.getViewVector(1.0F).scale(attractor.getBbWidth()));
            for (int i = 0; i < 3; i++) {
                double x = attractor.getRandomX(2.0F);
                double y = attractor.position().y;
                double z = attractor.getRandomZ(2.0F);
                attractor.level().addParticle(ModParticles.SNIFF.get(), x, y, z, look.x, look.y, look.z);
            }
        }
        if (tick == 30) {
            List<BlockPos> ores = new ArrayList<>();
            BlockPos blockpos = attractor.blockPosition();
            int half = range / 2;
            for (int i = 0; i <= half && i >= -half; i = (i <= 0 ? 1 : 0) - i) {
                for (int j = 0; j <= range && j >= -range; j = (j <= 0 ? 1 : 0) - j) {
                    for (int k = 0; k <= range && k >= -range; k = (k <= 0 ? 1 : 0) - k) {
                        BlockPos offset = blockpos.offset(j, i, k);
                        BlockState state = attractor.level().getBlockState(offset);
                        if (Services.PLATFORM.isOre(state)) {
                            if (ores.size() < maxOres) {
                                ores.add(offset);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            for (BlockPos ore : ores) {
                HighlightedBlockEntity highlight = ModEntities.HIGHLIGHTED_BLOCK.get().create(attractor.level());
                highlight.setPos(Vec3.atBottomCenterOf(ore));
                highlight.setLifespan(effectLength);
                highlight.setXRot(0);
                highlight.setYRot(0);
                attractor.level().addFreshEntity(highlight);
            }
        }
    }

    public static int getImmuneTime(LivingEntity enchanted) {
        if (hasEnchant(enchanted, ModEnchantments.IMMUNITY_FRAME)) {
            CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
            return tag.getInt(IMMUNITY_TIME_TAG);
        }
        return 0;
    }

    public static void setImmuneTime(LivingEntity enchanted, int time) {
        if (hasEnchant(enchanted, ModEnchantments.IMMUNITY_FRAME)) {
            CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
            tag.putInt(IMMUNITY_TIME_TAG, time);
            sync(enchanted, tag);
        }
    }

    public static int getFrozenTime(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getEntityTag(enchanted);
        if (tag == null) return 0;

        return tag.getInt(FROZEN_TIME_TAG);
    }

    public static void setFrozenTimeTag(LivingEntity enchanted, int time) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(FROZEN_TIME_TAG, time);
        sync(enchanted, tag);
    }

    public static List<LivingEntity> getNearbyHealers(LivingEntity hurtOwner) {
        Predicate<Entity> healer = (animal) -> hasSameOwnerAs((LivingEntity) animal, hurtOwner) && hasEnchant((LivingEntity) animal, ModEnchantments.HEALING_AURA) && getHealingAuraTime((LivingEntity) animal) == 0;
        return hurtOwner.level().getEntitiesOfClass(LivingEntity.class, hurtOwner.getBoundingBox().inflate(16, 4, 16), EntitySelector.NO_SPECTATORS.and(healer));
    }

    public static float getFallDistance(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getFloat(FALL_DISTANCE_SYNC);
    }

    public static void setFallDistance(LivingEntity enchanted, float dist) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putFloat(FALL_DISTANCE_SYNC, dist);
        sync(enchanted, tag);

    }

    public static int getShadowPunchCooldown(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getInt(SHADOW_PUNCH_COOLDOWN);
    }

    public static void setShadowPunchCooldown(LivingEntity enchanted, int time) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putInt(SHADOW_PUNCH_COOLDOWN, time);
        sync(enchanted, tag);
    }

    public static int[] getShadowPunchTimes(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getIntArray(SHADOW_PUNCH_TIMES);
    }

    public static void setShadowPunchTimes(LivingEntity enchanted, int[] times) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putIntArray(SHADOW_PUNCH_TIMES, times);
        sync(enchanted, tag);
    }

    public static void setShadowPunchStriking(LivingEntity enchanted, int[] times) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        tag.putIntArray(SHADOW_PUNCH_STRIKING, times);
        sync(enchanted, tag);
    }

    public static int[] getShadowPunchStriking(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        return tag.getIntArray(SHADOW_PUNCH_STRIKING);
    }

    public static int getCharismaBonusForOwner(Player player) {
        Predicate<Entity> pet = (animal) -> isTamed(animal) && isPetOf(player, animal);
        List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(25, 8, 25), EntitySelector.NO_SPECTATORS.and(pet));
        int charismas = 0;
        for (LivingEntity entity : list) {
            charismas += 10 * getEnchantLevel(entity, ModEnchantments.CHARISMA);
        }
        return Math.min(charismas, 50);
    }

    public static void setPetJukeboxDisc(LivingEntity enchanted, ItemStack stack) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        if (stack.isEmpty()) {
            tag.remove(JUKEBOX_FOLLOWER_DISC);
        } else {
            tag.put(JUKEBOX_FOLLOWER_DISC, stack.saveOptional(enchanted.registryAccess()));
        }
        sync(enchanted, tag);
    }

    public static ItemStack getPetJukeboxDisc(LivingEntity enchanted) {
        CompoundTag tag = ModEntityData.getOrCreateEntityTag(enchanted);
        if (tag.contains(JUKEBOX_FOLLOWER_DISC)) {
            return ItemStack.parseOptional(enchanted.registryAccess(), tag.getCompound(JUKEBOX_FOLLOWER_DISC));
        }
        return ItemStack.EMPTY;
    }

    public static boolean isValidTeleporter(LivingEntity owner, Mob animal) {
        if (hasEnchant(animal, ModEnchantments.TETHERED_TELEPORT)) {
            if (animal instanceof ICommandableMob commandableMob) {
                return commandableMob.redomesticate$getPetCommand() == PetCommand.FOLLOW;
            } else if (animal instanceof TamableAnimal tame) {
                return !tame.isOrderedToSit() && animal.distanceTo(owner) < 10;
            }
        }
        return false;
    }

    public static boolean isInjured(LivingEntity entity) {
        return entity.getHealth() < entity.getMaxHealth() &&
                (entity.getHealth() / entity.getMaxHealth()) <= ModConfig.get().petInjuredStatusHealthRatio;
    }

    public static boolean wantsToAttack(LivingEntity pet, @Nullable LivingEntity enemy) {
        if (!isTamed(pet)) {
            return true;
        }

        if (ModConfig.get().petWontAttackWhenInjured && isInjured(pet)) {
            return enemy != null && !(enemy instanceof Enemy || enemy instanceof IronGolem);
        }
        return true;
    }
}
