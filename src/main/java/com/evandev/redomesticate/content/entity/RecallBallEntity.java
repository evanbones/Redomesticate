package com.evandev.redomesticate.content.entity;

import com.evandev.redomesticate.util.VersionCompat;
import com.evandev.redomesticate.util.NbtCompat;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if >=26.1 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///?}

import java.util.Optional;
import java.util.UUID;

public class RecallBallEntity extends Entity {

    private static final EntityDataAccessor<Object> OWNER_UUID = SynchedEntityData.defineId(RecallBallEntity.class, VersionCompat.syncedUUIDSerializer());
    private static final EntityDataAccessor<String> CONTAINED_ENTITY_TYPE = SynchedEntityData.defineId(RecallBallEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> OPENED = SynchedEntityData.defineId(RecallBallEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FINISHED = SynchedEntityData.defineId(RecallBallEntity.class, EntityDataSerializers.BOOLEAN);
    private CompoundTag containedData = new CompoundTag();
    private float prevOpenProgress;
    private float openProgress;

    public RecallBallEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    //? if >=26.1 {
    /*@Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }
    *///?}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        VersionCompat.defineSyncedUUID(builder, OWNER_UUID);
        builder.define(CONTAINED_ENTITY_TYPE, "minecraft:pig");
        builder.define(OPENED, false);
        builder.define(FINISHED, false);
    }

    public boolean isPickable() {
        return !this.isFinished();
    }

    @Override
    //? if >=26.1 {
    /*public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand, @NotNull Vec3 location) {
    *///?} else {
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
    //?}
        if (!this.isFinished()) {
            if (this.getOwnerUUID() == null) {
                this.discard();
            } else if (player.getUUID().equals(this.getOwnerUUID()) && !this.entityData.get(OPENED)) {
                this.playSound(SoundEvents.ENDER_CHEST_OPEN, 1.0F, 1.5F);
                this.entityData.set(OPENED, true);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick() {
        super.tick();
        this.setYRot(this.getYRot() + 1);
        this.setXRot(0);
        prevOpenProgress = openProgress;

        if (this.isInWaterOrBubble() || this.isInLava()) {
            this.setPos(this.position().add(0, 0.08, 0));
        }

        if (this.entityData.get(OPENED) && openProgress < 1F) {
            openProgress += 0.1F;
        }
        if (!this.entityData.get(OPENED) && openProgress > 0F) {
            openProgress -= 0.1F;
        }

        if (random.nextFloat() < 0.4F) {
            this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }

        if (this.getY() < level().getMinBuildHeight()) {
            this.setPos(this.getX(), level().getMinBuildHeight() + 1.2F, this.getZ());
        }

        if (this.entityData.get(OPENED) && openProgress >= 1F && !this.isFinished()) {
            if (!level().isClientSide()) {
                EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(this.getContainedEntityType()));
                Entity entity = VersionCompat.create(type, level());
                if (entity instanceof LivingEntity alive) {
                    NbtCompat.loadEntity(alive, this.getContainedData());
                    alive.setHealth(Math.max(2, alive.getMaxHealth() * 0.25F));
                    alive.setYRot(random.nextFloat() * 360 - 180);
                    alive.setPos(this.position());
                    level().addFreshEntity(alive);
                }
                this.entityData.set(FINISHED, true);
                this.entityData.set(OPENED, false);
            }
        }

        if (this.isFinished() && openProgress <= 0.01F) {
            this.discard();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean shouldBeSaved() {
        return !this.isFinished() && super.shouldBeSaved();
    }

    public boolean isFinished() {
        return this.entityData.get(FINISHED);
    }

    //? if >=26.1 {
    /*@Override
    protected void readAdditionalSaveData(ValueInput input) {
        input.read("Owner", UUIDUtil.CODEC).ifPresent(this::setOwnerUUID);
        this.setContainedEntityType(input.getStringOr("ContainedEntityType", "minecraft:pig"));
        input.read("ContainedData", CompoundTag.CODEC).filter(tag -> !tag.isEmpty()).ifPresent(this::setContainedData);
        this.entityData.set(FINISHED, input.getBooleanOr("Finished", false));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.storeNullable("Owner", UUIDUtil.CODEC, this.getOwnerUUID());
        output.putString("ContainedEntityType", this.getContainedEntityType());
        output.store("ContainedData", CompoundTag.CODEC, this.getContainedData());
        output.putBoolean("Finished", this.isFinished());
    }
    *///?} else {
    @Override
    protected void readAdditionalSaveData(CompoundTag compoundNBT) {
        UUID uuid;
        if (NbtCompat.hasUUID(compoundNBT, "Owner")) {
            uuid = NbtCompat.getUUID(compoundNBT, "Owner");
        } else {
            String s = NbtCompat.getString(compoundNBT, "Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(VersionCompat.getServer(this), s);
        }

        if (uuid != null) {
            this.setOwnerUUID(uuid);
        }

        this.setContainedEntityType(NbtCompat.getString(compoundNBT, "ContainedEntityType"));
        if (!NbtCompat.getCompound(compoundNBT, "ContainedData").isEmpty()) {
            this.setContainedData(NbtCompat.getCompound(compoundNBT, "ContainedData"));
        }
        this.entityData.set(FINISHED, NbtCompat.getBoolean(compoundNBT, "Finished"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundNBT) {
        if (this.getOwnerUUID() != null) {
            NbtCompat.putUUID(compoundNBT, "Owner", this.getOwnerUUID());
        }
        compoundNBT.putString("ContainedEntityType", this.getContainedEntityType());
        compoundNBT.put("ContainedData", this.getContainedData());
        compoundNBT.putBoolean("Finished", this.isFinished());
    }
    //?}

    @Nullable
    public UUID getOwnerUUID() {
        return VersionCompat.getSyncedUUID(this.entityData, OWNER_UUID);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        VersionCompat.setSyncedUUID(this.entityData, OWNER_UUID, uuid);
    }

    public String getContainedEntityType() {
        return this.entityData.get(CONTAINED_ENTITY_TYPE);
    }

    public void setContainedEntityType(String containedEntityType) {
        this.entityData.set(CONTAINED_ENTITY_TYPE, containedEntityType);
    }

    public CompoundTag getContainedData() {
        return this.containedData;
    }

    public void setContainedData(CompoundTag containedData) {
        this.containedData = containedData;
    }

    //? if <26.1 {
    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return damageSource.getEntity() == null || !(damageSource.getEntity() instanceof Player player) || !player.isCreative();
    }
    //?}

    public float getOpenProgress(float f) {
        return Mth.lerp(f, prevOpenProgress, openProgress);
    }
}