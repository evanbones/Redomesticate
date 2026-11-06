package com.evandev.redomesticate.content.block.entity;

import com.evandev.redomesticate.util.NbtCompat;
import com.evandev.redomesticate.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
//? if >=26.1 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.UUIDUtil;
*///?}

public class DrumBlockEntity extends BlockEntity {

    private UUID placerUUID;

    public DrumBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRUM.get(), pos, state);
    }

    public UUID getPlacerUUID() {
        return placerUUID;
    }

    public void setPlacerUUID(UUID placerUUID) {
        this.placerUUID = placerUUID;
    }

    //? if >=26.1 {
    /*@Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("PlacerUUID", UUIDUtil.CODEC).ifPresent(uuid -> this.placerUUID = uuid);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("PlacerUUID", UUIDUtil.CODEC, this.placerUUID);
    }
    *///?} else {
    @Override
    protected void loadAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(compound, registries);
        if (compound.contains("PlacerUUID")) {
            this.placerUUID = NbtCompat.getUUID(compound, "PlacerUUID");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(compound, registries);
        if (this.placerUUID != null) {
            NbtCompat.putUUID(compound, "PlacerUUID", this.placerUUID);
        }
    }
    //?}
}