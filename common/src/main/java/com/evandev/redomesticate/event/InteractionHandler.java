package com.evandev.redomesticate.event;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.api.PetCommand;
import com.evandev.redomesticate.api.taming.TamingDefinition;
import com.evandev.redomesticate.api.taming.TransformationDefinition;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.content.entity.ai.UniversalAIManager;
import com.evandev.redomesticate.platform.Services;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.registry.ModItems;
import com.evandev.redomesticate.registry.ModSounds;
import com.evandev.redomesticate.registry.ModTags;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InteractionHandler {

    public static InteractionResult handleEntityInteraction(Player player, InteractionHand hand, Entity target) {
        if (!(target instanceof Mob mob) || !(target instanceof ITameableEntity tameable)) {
            return InteractionResult.PASS;
        }

        ItemStack itemInHand = player.getItemInHand(hand);
        ICommandableMob commandable = (ICommandableMob) mob;
        boolean isClient = player.level().isClientSide();

        if (!TameableUtils.isTamed(mob)) {
            Registry<TamingDefinition> tamingRegistry = player.level().registryAccess().registryOrThrow(TamingDefinition.REGISTRY_KEY);
            Optional<TamingDefinition> tamingDef = tamingRegistry.stream()
                    .filter(def -> def.entities().contains(mob.getType().builtInRegistryHolder()) && def.items().test(itemInHand))
                    .filter(def -> {
                        if (def.requiredData().isEmpty()) return true;
                        CompoundTag mobData = new CompoundTag();
                        mob.saveWithoutId(mobData);
                        return NbtUtils.compareNbt(def.requiredData().get(), mobData, true);
                    })
                    .findFirst();

            if (tamingDef.isPresent()) {
                if (isClient) return InteractionResult.SUCCESS;

                if (!player.getAbilities().instabuild) itemInHand.shrink(1);

                if (player.getRandom().nextFloat() < tamingDef.get().chance()) {
                    tameable.redomesticate$setTame(true);
                    tameable.redomesticate$setTameOwnerUUID(player.getUUID());

                    if (mob instanceof TamableAnimal tamableAnimal) {
                        tamableAnimal.setTame(true, false);
                        tamableAnimal.setOwnerUUID(player.getUUID());
                    }

                    commandable.redomesticate$setCommand(0);

                    if (mob instanceof Fox fox) {
                        fox.setSitting(false);
                    }

                    mob.setTarget(null);
                    mob.setLastHurtByMob(null);
                    if (mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
                        mob.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                    }
                    mob.getNavigation().stop();
                    mob.setPersistenceRequired();

                    UniversalAIManager.applyPetAI(mob);
                    spawnParticles((ServerLevel) player.level(), mob, ParticleTypes.HEART);
                } else {
                    spawnParticles((ServerLevel) player.level(), mob, ParticleTypes.SMOKE);
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (itemInHand.is(ModItems.DEED_OF_OWNERSHIP.get())) {
            CustomData data = itemInHand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            boolean isBound = data.contains("HasBoundEntity") && data.copyTag().getBoolean("HasBoundEntity");

            if (!isBound && TameableUtils.isTamed(mob) && TameableUtils.isPetOf(player, mob)) {
                if (isClient) return InteractionResult.SUCCESS;

                CompoundTag tag = data.copyTag();
                tag.putBoolean("HasBoundEntity", true);
                tag.putString("BoundEntityName", mob.getName().getString());
                tag.putUUID("BoundEntityUUID", mob.getUUID());
                itemInHand.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                player.swing(hand);
                return InteractionResult.CONSUME;
            } else if (isBound) {
                UUID boundUUID = data.copyTag().contains("BoundEntityUUID") ? data.copyTag().getUUID("BoundEntityUUID") : null;
                if (boundUUID != null && boundUUID.equals(mob.getUUID()) && !TameableUtils.isPetOf(player, mob)) {
                    if (isClient) return InteractionResult.SUCCESS;

                    if (mob instanceof TamableAnimal tamableAnimal) {
                        tamableAnimal.setTame(true, false);
                        tamableAnimal.setOwnerUUID(player.getUUID());
                    }

                    tameable.redomesticate$setTame(true);
                    tameable.redomesticate$setTameOwnerUUID(player.getUUID());
                    commandable.redomesticate$setCommand(1);
                    if (mob instanceof Fox fox) {
                        fox.setSitting(true);
                    }
                    player.swing(hand);
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
        }

        if (TameableUtils.isTamed(mob) && TameableUtils.isPetOf(player, mob)) {
            if (ModConfig.get().trinaryCommandSystem && !player.isShiftKeyDown() && itemInHand.isEmpty() && mob.getType().is(ModTags.COMMAND_WHITELIST) && !(mob instanceof AbstractHorse)) {
                if (isClient) return InteractionResult.SUCCESS;

                mob.setTarget(null);
                mob.getNavigation().stop();

                commandable.playerSetCommand(player, mob);
                return InteractionResult.SUCCESS;
            }

            if (TameableUtils.hasEnchant(mob, ModEnchantments.GLUTTONOUS)) {
                var foodProperty = itemInHand.get(DataComponents.FOOD);
                if (foodProperty != null && mob.getHealth() < mob.getMaxHealth()) {
                    if (isClient) return InteractionResult.SUCCESS;

                    mob.heal((float) Math.floor(foodProperty.nutrition() * 1.5F));
                    if (!player.isCreative()) itemInHand.shrink(1);
                    mob.playSound(mob.getRandom().nextBoolean() ? SoundEvents.PLAYER_BURP : SoundEvents.GENERIC_EAT, 1F, mob.getVoicePitch());
                    return InteractionResult.SUCCESS;
                }
            }

            if (itemInHand.is(ModItems.COLLAR_TAG.get())) {
                if (isClient) return InteractionResult.SUCCESS;

                var itemEnchantments = itemInHand.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                Map<ResourceLocation, Integer> entityEnchantments = TameableUtils.getEnchants(mob);

                if (itemInHand.has(DataComponents.CUSTOM_NAME)) mob.setCustomName(itemInHand.getHoverName());
                if (!player.isCreative()) itemInHand.shrink(1);

                EventProxy.blockCollarTick(mob);

                if (TameableUtils.hasCollar(mob)) {
                    ItemStack collarFrom = new ItemStack(ModItems.COLLAR_TAG.get());
                    if (entityEnchantments != null) {
                        var reg = mob.level().registryAccess().registry(Registries.ENCHANTMENT);
                        if (reg.isPresent()) {
                            for (Map.Entry<ResourceLocation, Integer> entry : entityEnchantments.entrySet()) {
                                var oneEnchant = reg.get().get(entry.getKey());
                                if (oneEnchant != null)
                                    collarFrom.enchant(reg.get().wrapAsHolder(oneEnchant), entry.getValue());
                            }
                        }
                    }
                    mob.spawnAtLocation(collarFrom);
                }

                mob.playSound(ModSounds.COLLAR_TAG.get(), 1, 1);
                if (!itemEnchantments.isEmpty()) {
                    TameableUtils.clearEnchants(mob);
                    TameableUtils.addEnchant(mob, itemEnchantments);
                } else {
                    TameableUtils.clearEnchants(mob);
                }
                return InteractionResult.SUCCESS;
            }

            if (!ModConfig.get().trinaryCommandSystem && !player.isShiftKeyDown() && !(mob instanceof TamableAnimal) && mob.getType().is(ModTags.COMMAND_WHITELIST) && !(mob instanceof AbstractHorse)) {
                boolean isFood = mob instanceof Animal animal && animal.isFood(itemInHand);

                if (!isFood) {
                    if (isClient) return InteractionResult.SUCCESS;

                    boolean isSitting = commandable.redomesticate$getPetCommand() == PetCommand.SIT;
                    commandable.redomesticate$setPetCommand(isSitting ? PetCommand.FOLLOW : PetCommand.SIT);
                    mob.setTarget(null);
                    mob.getNavigation().stop();
                    mob.setDeltaMovement(0, mob.getDeltaMovement().y, 0);

                    return InteractionResult.SUCCESS;
                }
            }
        }

        Registry<TransformationDefinition> transformRegistry = player.level().registryAccess().registryOrThrow(TransformationDefinition.REGISTRY_KEY);
        Optional<TransformationDefinition> transformDef = transformRegistry.stream()
                .filter(def -> def.targetEntity().contains(mob.getType().builtInRegistryHolder()) && def.triggerItem().test(itemInHand))
                .filter(def -> {
                    if (def.requiredData().isEmpty()) return true;
                    CompoundTag mobData = new CompoundTag();
                    mob.saveWithoutId(mobData);
                    return NbtUtils.compareNbt(def.requiredData().get(), mobData, true);
                })
                .findFirst();

        if (transformDef.isPresent()) {
            TransformationDefinition def = transformDef.get();
            if (Services.PLATFORM.canLivingConvert(mob, def.resultEntity())) {
                if (isClient) return InteractionResult.CONSUME;

                player.swing(hand);

                if (def.soundEvent() != null) {
                    mob.playSound(def.soundEvent().value(), 0.8F, mob.getVoicePitch());
                } else {
                    mob.playSound(SoundEvents.ZOMBIE_INFECT, 0.8F, mob.getVoicePitch());
                }

                Mob newMob = (Mob) def.resultEntity().create(mob.level());
                if (newMob != null) {
                    CompoundTag extras = new CompoundTag();
                    mob.addAdditionalSaveData(extras);

                    if (mob.isLeashed()) newMob.setLeashedTo(mob.getLeashHolder(), true);
                    newMob.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                    newMob.setNoAi(mob.isNoAi());
                    newMob.setBaby(mob.isBaby());

                    if (mob.hasCustomName()) {
                        newMob.setCustomName(mob.getCustomName());
                        newMob.setCustomNameVisible(mob.isCustomNameVisible());
                    }

                    newMob.readAdditionalSaveData(extras);
                    newMob.setPersistenceRequired();

                    for (int i = 0; i < 6 + mob.getRandom().nextInt(5); i++) {
                        mob.level().addParticle(ParticleTypes.SNEEZE, mob.getRandomX(1.0F), mob.getRandomY(), mob.getRandomZ(1.0F), 0F, 0F, 0F);
                    }

                    Services.PLATFORM.onLivingConvert(mob, newMob);
                    player.level().addFreshEntity(newMob);
                    mob.discard();

                    if (!player.isCreative()) itemInHand.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private static void spawnParticles(ServerLevel level, Mob mob, SimpleParticleType particleType) {
        for (int i = 0; i < 7; ++i) {
            double d0 = mob.getRandom().nextGaussian() * 0.02D;
            double d1 = mob.getRandom().nextGaussian() * 0.02D;
            double d2 = mob.getRandom().nextGaussian() * 0.02D;
            level.sendParticles(particleType, mob.getRandomX(1.0D), mob.getRandomY() + 0.5D, mob.getRandomZ(1.0D), 1, d0, d1, d2, 0.0D);
        }
    }
}