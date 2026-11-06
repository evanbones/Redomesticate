package com.evandev.redomesticate.content.entity;

import com.evandev.redomesticate.util.VersionCompat;
import com.evandev.redomesticate.util.NbtCompat;
import com.evandev.redomesticate.client.event.ClientJukeboxHandler;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
//? if >=26.1 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.UUIDUtil;
*///?}

public class FollowingJukeboxEntity extends Entity {

    private static final EntityDataAccessor<Object> FOLLOWING_UUID = SynchedEntityData.defineId(FollowingJukeboxEntity.class, VersionCompat.syncedUUIDSerializer());
    private static final EntityDataAccessor<ItemStack> JUKEBOX_ITEM = SynchedEntityData.defineId(FollowingJukeboxEntity.class, EntityDataSerializers.ITEM_STACK);

    public FollowingJukeboxEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void tick() {
        super.tick();
        Entity following = getFollowing();
        this.setPos(this.position().add(this.getDeltaMovement()));
        this.setYRot(this.getYRot() + 2F);
        if (!level().isClientSide()) {
            if (following != null) {
                float width = following.getBbWidth() + 0.6F;
                this.setRecordItem(this.getDiscFromOwner());
                float speed = 0.05F;
                if (this.distanceTo(following) > 2 + width) {
                    this.copyPosition(following);
                } else {
                    double targetX = following.getX() + Math.sin(tickCount * speed) * width;
                    double targetY = following.getY();
                    double targetZ = following.getZ() - Math.cos(tickCount * speed) * width;
                    Vec3 vec3 = new Vec3(targetX - this.getX(), targetY - this.getY() + Math.sin(tickCount * 0.3F + 2F) * 0.01F, targetZ - this.getZ());
                    this.setDeltaMovement(vec3);
                }
                if (!this.isSilent() && !this.getRecordItem().isEmpty()) {
                    this.level().broadcastEntityEvent(this, (byte) 66);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 67);
                }

                if (following instanceof LivingEntity livingFollowing && !TameableUtils.hasEnchant(livingFollowing, ModEnchantments.DISC_JOCKEY)) {
                    this.setFollowingUUID(null);
                }
            } else {
                this.setRecordItem(ItemStack.EMPTY);
                this.discard();
            }
        }
    }

    public boolean isNoGravity() {
        return true;
    }

    //? if >=26.1 {
    /*@Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }
    *///?}

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        VersionCompat.defineSyncedUUID(builder, FOLLOWING_UUID);
        builder.define(JUKEBOX_ITEM, ItemStack.EMPTY);
    }

    //? if >=26.1 {
    /*@Override
    protected void readAdditionalSaveData(ValueInput input) {
        input.read("FollowerUUID", UUIDUtil.CODEC).ifPresent(this::setFollowingUUID);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.storeNullable("FollowerUUID", UUIDUtil.CODEC, this.getFollowerUUID());
    }
    *///?} else {
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (NbtCompat.hasUUID(tag, "FollowerUUID")) {
            this.setFollowingUUID(NbtCompat.getUUID(tag, "FollowerUUID"));
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        if (this.getFollowerUUID() != null) {
            NbtCompat.putUUID(tag, "FollowerUUID", this.getFollowerUUID());
        }
    }
    //?}

    @Nullable
    public UUID getFollowerUUID() {
        return VersionCompat.getSyncedUUID(this.entityData, FOLLOWING_UUID);
    }

    public void setFollowingUUID(@Nullable UUID uniqueId) {
        VersionCompat.setSyncedUUID(this.entityData, FOLLOWING_UUID, uniqueId);
    }

    public Entity getFollowing() {
        UUID id = getFollowerUUID();
        if (id != null && !level().isClientSide()) {
            return ((ServerLevel) level()).getEntity(id);
        }
        return null;
    }

    public ItemStack getRecordItem() {
        return this.entityData.get(JUKEBOX_ITEM);
    }

    public void setRecordItem(ItemStack item) {
        this.entityData.set(JUKEBOX_ITEM, item);
    }

    public void addDiscToOwner(ItemStack stack) {
        if (getFollowing() instanceof LivingEntity living) {
            TameableUtils.setPetJukeboxDisc(living, stack);
            if (stack.isEmpty()) {
                this.level().broadcastEntityEvent(this, (byte) 67);
            }
        }
    }

    public ItemStack getDiscFromOwner() {
        if (getFollowing() instanceof LivingEntity living) {
            return TameableUtils.getPetJukeboxDisc(living);
        }
        return ItemStack.EMPTY;
    }

    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec3, @NotNull InteractionHand hand) {
        return interact(player, hand);
    }

    public boolean isPickable() {
        return true;
    }

    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        boolean flag = false;
        ItemStack held = player.getItemInHand(hand);
        if (!this.getRecordItem().isEmpty()) {
            ItemStack copy = this.getRecordItem().copy();
            addDiscToOwner(ItemStack.EMPTY);
            VersionCompat.spawnAtLocation(this, copy);
            flag = true;
        }

        if (held.has(DataComponents.JUKEBOX_PLAYABLE)) {
            addDiscToOwner(held);
            if (!player.isCreative()) {
                held.shrink(1);
            }
            flag = true;
        }
        return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    public void handleEntityEvent(byte id) {
        if (id == 66 || id == 67) {
            if (this.hasRecordSound() && this.random.nextFloat() < 0.1F) {
                float f = this.random.nextFloat();
                float f1 = this.random.nextFloat();
                float f2 = this.random.nextFloat();
                this.level().addParticle(ParticleTypes.NOTE, this.getX(), this.getY(1), this.getZ(), f, f1, f2);
            }
            if (this.level().isClientSide()) {
                ClientJukeboxHandler.updateEntityStatus(this, id);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    public SoundEvent getRecordSound() {
        var playable = this.getRecordItem().get(DataComponents.JUKEBOX_PLAYABLE);
        if (playable != null) {
            //? if >=26.1 {
            /*if (playable.song().isBound()) {
                return playable.song().value().soundEvent().value();
            }
            *///?} else {
            var songHolder = playable.song().unwrap(this.level().registryAccess());

            if (songHolder.isPresent() && songHolder.get().isBound()) {
                return songHolder.get().value().soundEvent().value();
            }
            //?}
        }
        return null;
    }

    public boolean hasRecordSound() {
        return this.getRecordItem().has(DataComponents.JUKEBOX_PLAYABLE);
    }
}