package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.IPetbedDataEntity;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Axolotl.class)
public abstract class AxolotlMixin extends Animal {

    protected AxolotlMixin(EntityType<? extends Animal> type, Level lvl) {
        super(type, lvl);
    }

    @Inject(at = @At("TAIL"), method = "saveToBucketTag(Lnet/minecraft/world/item/ItemStack;)V")
    private void writeAdditionalBucket(ItemStack stack, CallbackInfo ci) {
        ITameableEntity tameable = (ITameableEntity) this;
        ICommandableMob commandable = (ICommandableMob) this;

        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, compoundNBT -> {
            compoundNBT.putInt("RedomesticateCommand", commandable.redomesticate$getCommand());
            compoundNBT.putBoolean("RedomesticateTamed", tameable.redomesticate$isTame());
            if (tameable.redomesticate$getTameOwnerUUID() != null) {
                compoundNBT.putUUID("Owner", tameable.redomesticate$getTameOwnerUUID());
            }
            if ((Object) this instanceof IPetbedDataEntity petbedData) {
                CompoundTag syncData = petbedData.redomesticate$getEntityData();
                if (syncData != null && !syncData.isEmpty()) {
                    compoundNBT.put(Constants.ENTITY_SYNC_DATA, syncData);
                }
            }
        });
    }

    @Inject(at = @At("TAIL"), method = "loadFromBucketTag(Lnet/minecraft/nbt/CompoundTag;)V")
    private void readAdditionalBucket(CompoundTag compoundNBT, CallbackInfo ci) {
        ITameableEntity tameable = (ITameableEntity) this;
        ICommandableMob commandable = (ICommandableMob) this;

        commandable.redomesticate$setCommand(compoundNBT.getInt("RedomesticateCommand"));
        tameable.redomesticate$setTame(compoundNBT.getBoolean("RedomesticateTamed"));

        UUID uuid = null;
        if (compoundNBT.hasUUID("Owner")) {
            uuid = compoundNBT.getUUID("Owner");
        } else if (compoundNBT.contains("Owner", 8)) {
            String s = compoundNBT.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                tameable.redomesticate$setTameOwnerUUID(uuid);
                tameable.redomesticate$setTame(true);
            } catch (Throwable throwable) {
                tameable.redomesticate$setTame(false);
            }
        }

        if (compoundNBT.contains(Constants.ENTITY_SYNC_DATA)) {
            if ((Object) this instanceof IPetbedDataEntity petbedData) {
                petbedData.redomesticate$setEntityData(compoundNBT.getCompound(Constants.ENTITY_SYNC_DATA));
                petbedData.redomesticate$setCachedEnchants(TameableUtils.getEnchants(this));
                TameableUtils.onUpdateEnchants(null, this);
            }
        }
    }
}