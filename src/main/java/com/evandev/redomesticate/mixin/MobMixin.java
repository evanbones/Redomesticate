package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.util.VersionCompat;
import com.evandev.redomesticate.util.NbtCompat;
import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.compat.alexsmobs.AlexsMobsCompat;
import com.evandev.redomesticate.platform.Services;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.api.PetCommand;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.registry.ModTags;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;
//? if >=26.1 {
/*import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
*///?}

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements ICommandableMob, ITameableEntity {

    //? if <26.1 {
    @Unique
    private static final EntityDataAccessor<Integer> redomesticate$COMMAND = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Object> redomesticate$OWNER_UUID = SynchedEntityData.defineId(Mob.class, VersionCompat.syncedUUIDSerializer());
    @Unique
    private static final EntityDataAccessor<Boolean> redomesticate$IS_TAMED = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BOOLEAN);
    //?}

    protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
    private void redomesticate$preventDespawn(CallbackInfo ci) {
        if (this.redomesticate$isTame()) {
            ci.cancel();
        }
    }

    //? if <26.1 {
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void redomesticate$defineUniversalData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(redomesticate$COMMAND, 0);
        VersionCompat.defineSyncedUUID(builder, redomesticate$OWNER_UUID);
        builder.define(redomesticate$IS_TAMED, false);
    }
    //?}

    //? if >=26.1 {
    /*@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void redomesticate$saveUniversalData(ValueOutput output, CallbackInfo ci) {
        output.putInt("RedomesticateCommand", this.redomesticate$getCommand());
        output.putBoolean("RedomesticateTamed", this.redomesticate$isTame());
        output.storeNullable("RedomesticateOwner", UUIDUtil.CODEC, this.redomesticate$getTameOwnerUUID());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void redomesticate$readUniversalData(ValueInput input, CallbackInfo ci) {
        if (!AlexsMobsCompat.hasNativeCommands((Mob) (Object) this)) {
            this.redomesticate$setCommand(input.getIntOr("RedomesticateCommand", 0));
        }
        this.redomesticate$setTame(input.getBooleanOr("RedomesticateTamed", false));
        input.read("RedomesticateOwner", UUIDUtil.CODEC).ifPresent(this::redomesticate$setTameOwnerUUID);
    }
    *///?} else {
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void redomesticate$saveUniversalData(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("RedomesticateCommand", this.redomesticate$getCommand());
        tag.putBoolean("RedomesticateTamed", this.redomesticate$isTame());
        if (this.redomesticate$getTameOwnerUUID() != null) {
            NbtCompat.putUUID(tag, "RedomesticateOwner", this.redomesticate$getTameOwnerUUID());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void redomesticate$readUniversalData(CompoundTag tag, CallbackInfo ci) {
        if (!AlexsMobsCompat.hasNativeCommands((Mob) (Object) this)) {
            this.redomesticate$setCommand(NbtCompat.getInt(tag, "RedomesticateCommand"));
        }
        this.redomesticate$setTame(NbtCompat.getBoolean(tag, "RedomesticateTamed"));
        if (NbtCompat.hasUUID(tag, "RedomesticateOwner")) {
            this.redomesticate$setTameOwnerUUID(NbtCompat.getUUID(tag, "RedomesticateOwner"));
        }
    }
    //?}

    @Override
    public int redomesticate$getCommand() {
        if (AlexsMobsCompat.hasNativeCommands((Mob) (Object) this)) {
            return AlexsMobsCompat.getCommand((Mob) (Object) this).getId();
        }
        //? if >=26.1 {
        /*return Services.PLATFORM.getPetState((Mob) (Object) this).command();
        *///?} else {
        return this.entityData.get(redomesticate$COMMAND);
        //?}
    }

    @Override
    public void redomesticate$setCommand(int command) {
        if (AlexsMobsCompat.hasNativeCommands((Mob) (Object) this)) {
            AlexsMobsCompat.setCommand((Mob) (Object) this, PetCommand.fromId(command));
            return;
        }
        //? if >=26.1 {
        /*Mob mob = (Mob) (Object) this;
        Services.PLATFORM.setPetState(mob, Services.PLATFORM.getPetState(mob).withCommand(command));
        *///?} else {
        this.entityData.set(redomesticate$COMMAND, command);
        //?}
    }

    @Override
    public boolean redomesticate$isTame() {
        //? if >=26.1 {
        /*return Services.PLATFORM.getPetState((Mob) (Object) this).tamed();
        *///?} else {
        return this.entityData.get(redomesticate$IS_TAMED);
        //?}
    }

    @Override
    public void redomesticate$setTame(boolean value) {
        //? if >=26.1 {
        /*Mob mob = (Mob) (Object) this;
        Services.PLATFORM.setPetState(mob, Services.PLATFORM.getPetState(mob).withTamed(value));
        *///?} else {
        this.entityData.set(redomesticate$IS_TAMED, value);
        //?}
    }

    @Nullable
    @Override
    public UUID redomesticate$getTameOwnerUUID() {
        //? if >=26.1 {
        /*return Services.PLATFORM.getPetState((Mob) (Object) this).owner().orElse(null);
        *///?} else {
        return VersionCompat.getSyncedUUID(this.entityData, redomesticate$OWNER_UUID);
        //?}
    }

    @Override
    public void redomesticate$setTameOwnerUUID(@Nullable UUID uuid) {
        //? if >=26.1 {
        /*Mob mob = (Mob) (Object) this;
        Services.PLATFORM.setPetState(mob, Services.PLATFORM.getPetState(mob).withOwner(uuid));
        *///?} else {
        VersionCompat.setSyncedUUID(this.entityData, redomesticate$OWNER_UUID, uuid);
        //?}
    }

    @Nullable
    @Override
    public LivingEntity redomesticate$getTameOwner() {
        UUID uuid = this.redomesticate$getTameOwnerUUID();
        return uuid == null ? null : this.level().getPlayerByUUID(uuid);
    }

    @Inject(
            //? if >=26.1 {
            /*method = "pickUpItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/item/ItemEntity;)V",
            *///?} else {
            method = "pickUpItem(Lnet/minecraft/world/entity/item/ItemEntity;)V",
            //?}
            at = @At(value = "HEAD"),
            cancellable = true
    )
    //? if >=26.1 {
    /*private void pickUpItem(ServerLevel level, ItemEntity itemEntity, CallbackInfo ci) {
    *///?} else {
    private void pickUpItem(ItemEntity itemEntity, CallbackInfo ci) {
    //?}
        if (TameableUtils.isTamed(this) && TameableUtils.hasEnchant(this, ModEnchantments.LINKED_INVENTORY)) {
            Entity owner = TameableUtils.getOwnerOf(this);
            if (owner instanceof Player player) {
                ci.cancel();
                if (player.addItem(itemEntity.getItem())) {
                    itemEntity.discard();
                } else {
                    itemEntity.copyPosition(player);
                }
            }

        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    //? if >=26.1 {
    /*private void globalInteract(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
    *///?} else {
    private void globalInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    //?}
        ItemStack itemInHand = player.getItemInHand(hand);
        Mob mob = (Mob) (Object) this;

        if (TameableUtils.isTamed(mob) && TameableUtils.isPetOf(player, mob)) {
            if (ModConfig.get().trinaryCommandSystem && !player.isShiftKeyDown() && mob.getType().builtInRegistryHolder().is(ModTags.COMMAND_WHITELIST) && !(mob instanceof AbstractHorse) && itemInHand.isEmpty()) {
                if (!player.level().isClientSide()) {
                    mob.setTarget(null);
                    mob.getNavigation().stop();

                    if (mob instanceof TamableAnimal tamable) {
                        tamable.setOrderedToSit(false);
                    }

                    this.playerSetCommand(player, mob);
                }

                VersionCompat.swing(player, hand, true);
                cir.setReturnValue(VersionCompat.sidedSuccess(player.level().isClientSide()));
            }
        }
    }

    @Override
    public boolean redomesticate$isStayingStill() {
        return this.redomesticate$getPetCommand() == PetCommand.SIT;
    }

    @Override
    public boolean redomesticate$isFollowingOwner() {
        return this.redomesticate$getPetCommand() == PetCommand.FOLLOW;
    }

    @Override
    public boolean redomesticate$isValidAttackTarget(LivingEntity target) {
        return true;
    }
}