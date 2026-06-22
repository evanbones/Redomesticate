package com.evandev.redomesticate.content.block.entity;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.content.block.PetBedBlock;
import com.evandev.redomesticate.data.ModWorldData;
import com.evandev.redomesticate.data.request.RespawnRequest;
import com.evandev.redomesticate.registry.ModBlockEntities;
import com.evandev.redomesticate.registry.ModTags;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class PetBedBlockEntity extends BlockEntity {

    private UUID ownerUUID;

    public PetBedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PET_BED.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PetBedBlockEntity blockEntity) {
        if (!ModConfig.get().petBedRespawns) return;
        long time = level.dayTime() % 24000L;
        if (time == 1) {
            ModWorldData data = ModWorldData.get(level);
            if (data != null) {
                List<RespawnRequest> requestList = data.getRespawnRequestsFor(level, pos);
                for (RespawnRequest request : requestList) {
                    if (addAndRemoveEntity(level, pos, state.getValue(PetBedBlock.FACING), request)) {
                        data.removeRespawnRequest(request);
                    }
                }
            }
        }

        if (!level.isClientSide && level.getGameTime() % 40 == 0) {
            if (blockEntity.ownerUUID != null) {
                LivingEntity owner = null;
                if (level instanceof ServerLevel serverLevel) {
                    for (ServerLevel sl : serverLevel.getServer().getAllLevels()) {
                        Entity entity = sl.getEntity(blockEntity.ownerUUID);
                        if (entity instanceof LivingEntity living) {
                            owner = living;
                            break;
                        }
                    }
                }
                if (owner != null) {
                    BlockPos ownerBedPos = TameableUtils.getPetBedPos(owner);
                    if (ownerBedPos == null || !ownerBedPos.equals(pos)) {
                        blockEntity.setOwnerUUID(null);
                    }
                }
            } else {
                Predicate<Entity> petPred = (animal) -> {
                    if (!TameableUtils.isTamed(animal)) return false;
                    if (animal.getType().is(ModTags.REFUSES_PET_BEDS)) return false;
                    return switch (animal) {
                        case ICommandableMob cmd when cmd.redomesticate$isStayingStill() -> false;
                        case TamableAnimal tamable when tamable.isOrderedToSit() -> false;
                        case LivingEntity living when TameableUtils.getPetBedPos(living) != null -> false;
                        default -> true;
                    };
                };
                List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(10.0D), EntitySelector.NO_SPECTATORS.and(petPred));
                if (!list.isEmpty()) {
                    LivingEntity pet = list.getFirst();
                    blockEntity.setOwnerUUID(pet.getUUID());
                    TameableUtils.setPetBedPos(pet, pos);
                    TameableUtils.setPetBedDimension(pet, level.dimension().toString());

                    if (level instanceof ServerLevel serverLevel) {
                        if (pet instanceof Mob mobPet) {
                            mobPet.getNavigation().moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1.2D);
                        }

                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                                pet.getX(), pet.getY() + pet.getBbHeight() / 2.0D, pet.getZ(),
                                5, 0.3, 0.3, 0.3, 0.0);
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                5, 0.3, 0.3, 0.3, 0.0);
                    }
                }
            }
        }
    }

    private static boolean addAndRemoveEntity(Level level, BlockPos pos, Direction dir, RespawnRequest request) {
        EntityType type = request.getEntityType();
        if (type != null) {
            Entity entity = type.create(level);
            if (entity instanceof LivingEntity living) {
                living.load(request.getEntityData());
                living.setPos(Vec3.upFromBottomCenterOf(pos, 0.8F));
                living.setHealth(living.getMaxHealth());
                if (!request.getNametag().isEmpty()) {
                    living.setCustomName(Component.translatable(request.getNametag()));
                }
                switch (dir) {
                    case NORTH:
                        living.setYRot(180);
                        break;
                    case EAST:
                        living.setYRot(-90);
                        break;
                    case SOUTH:
                        living.setYRot(0);
                        break;
                    case WEST:
                        living.setYRot(90);
                        break;
                }
                if (living instanceof ICommandableMob) {
                    ((ICommandableMob) living).redomesticate$setCommand(1);
                }
                if (living instanceof TamableAnimal) {
                    ((TamableAnimal) living).setOrderedToSit(true);
                }
                if (living instanceof Fox fox) {
                    fox.setSitting(true);
                }
                level.addFreshEntity(living);
                Entity owner = TameableUtils.getOwnerOf(entity);
                if (owner instanceof Player) {
                    ((Player) owner).displayClientMessage(Component.translatable("message.redomesticate.respawn", entity.getName()), false);
                }
                return true;
            }
        }
        return false;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(compound, registries);
        if (compound.hasUUID("OwnerUUID")) {
            this.ownerUUID = compound.getUUID("OwnerUUID");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(compound, registries);
        if (this.ownerUUID != null) {
            compound.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    public void removeAllRequestsFor(@Nullable Player message) {
        ModWorldData data = ModWorldData.get(level);
        if (data != null) {
            List<RespawnRequest> requestList = data.getRespawnRequestsFor(level, this.getBlockPos());
            for (RespawnRequest request : requestList) {
                data.removeRespawnRequest(request);
                if (message != null) {
                    message.displayClientMessage(Component.translatable("message.redomesticate.goodbye", request.getNametag()), false);
                }
            }
        }
    }

    public void resetBedsForNearbyPets() {
        this.ownerUUID = null;
        this.setChanged();
        Predicate<Entity> pet = (animal) -> TameableUtils.isTamed(animal) && TameableUtils.getPetBedPos((LivingEntity) animal) != null && TameableUtils.getPetBedPos((LivingEntity) animal).equals(this.getBlockPos());
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(this.getBlockPos().offset(-10, -5, -10).getCenter(), this.getBlockPos().offset(10, 5, 10).getCenter()), EntitySelector.NO_SPECTATORS.and(pet));
        for (LivingEntity entity : list) {
            Entity owner = TameableUtils.getOwnerOf(entity);
            if (owner instanceof Player) {
                ((Player) owner).displayClientMessage(Component.translatable("message.redomesticate.remove_respawn", entity.getName()), false);
                TameableUtils.removePetBedPos(entity);
            }
        }
    }
}