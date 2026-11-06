package com.evandev.redomesticate.content.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
//? if >=26.1 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///?}

public class HighlightedBlockEntity extends Entity {

    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(HighlightedBlockEntity.class, EntityDataSerializers.INT);

    public HighlightedBlockEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getBlockState().isAir()) {
            this.discard();
        }
        if (getLifespan() <= 0) {
            this.discard();
        } else {
            this.setLifespan(this.getLifespan() - 1);
        }
    }

    @Override
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIFESPAN, 20);
    }

    //? if >=26.1 {
    /*@Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }
    *///?} else {
    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
    }
    //?}

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int i) {
        this.entityData.set(LIFESPAN, i);
    }

    public BlockState getBlockState() {
        return this.level().getBlockState(this.blockPosition());
    }

    public boolean isCurrentlyGlowing() {
        return true;
    }
}
