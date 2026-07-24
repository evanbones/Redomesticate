package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.api.ICommandableMob;
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

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements ICommandableMob, ITameableEntity {

    @Unique
    private static final EntityDataAccessor<Integer> redomesticate$COMMAND = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Optional<UUID>> redomesticate$OWNER_UUID = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.OPTIONAL_UUID);
    @Unique
    private static final EntityDataAccessor<Boolean> redomesticate$IS_TAMED = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BOOLEAN);

    protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
    private void redomesticate$preventDespawn(CallbackInfo ci) {
        if (this.redomesticate$isTame()) {
            ci.cancel();
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void redomesticate$defineUniversalData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(redomesticate$COMMAND, 0);
        builder.define(redomesticate$OWNER_UUID, Optional.empty());
        builder.define(redomesticate$IS_TAMED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void redomesticate$saveUniversalData(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("RedomesticateCommand", this.redomesticate$getCommand());
        tag.putBoolean("RedomesticateTamed", this.redomesticate$isTame());
        if (this.redomesticate$getTameOwnerUUID() != null) {
            tag.putUUID("RedomesticateOwner", this.redomesticate$getTameOwnerUUID());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void redomesticate$readUniversalData(CompoundTag tag, CallbackInfo ci) {
        this.redomesticate$setCommand(tag.getInt("RedomesticateCommand"));
        this.redomesticate$setTame(tag.getBoolean("RedomesticateTamed"));
        if (tag.hasUUID("RedomesticateOwner")) {
            this.redomesticate$setTameOwnerUUID(tag.getUUID("RedomesticateOwner"));
        }
    }

    @Override
    public int redomesticate$getCommand() {
        return this.entityData.get(redomesticate$COMMAND);
    }

    @Override
    public void redomesticate$setCommand(int command) {
        this.entityData.set(redomesticate$COMMAND, command);
    }

    @Override
    public boolean redomesticate$isTame() {
        return this.entityData.get(redomesticate$IS_TAMED);
    }

    @Override
    public void redomesticate$setTame(boolean value) {
        this.entityData.set(redomesticate$IS_TAMED, value);
    }

    @Nullable
    @Override
    public UUID redomesticate$getTameOwnerUUID() {
        return this.entityData.get(redomesticate$OWNER_UUID).orElse(null);
    }

    @Override
    public void redomesticate$setTameOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(redomesticate$OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    @Override
    public LivingEntity redomesticate$getTameOwner() {
        UUID uuid = this.redomesticate$getTameOwnerUUID();
        return uuid == null ? null : this.level().getPlayerByUUID(uuid);
    }

    @Inject(
            method = "pickUpItem(Lnet/minecraft/world/entity/item/ItemEntity;)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void pickUpItem(ItemEntity itemEntity, CallbackInfo ci) {
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
    private void globalInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemInHand = player.getItemInHand(hand);
        Mob mob = (Mob) (Object) this;

        if (TameableUtils.isTamed(mob) && TameableUtils.isPetOf(player, mob)) {
            if (ModConfig.get().trinaryCommandSystem && !player.isShiftKeyDown() && mob.getType().is(ModTags.COMMAND_WHITELIST) && !(mob instanceof AbstractHorse) && itemInHand.isEmpty()) {
                if (!player.level().isClientSide()) {
                    mob.setTarget(null);
                    mob.getNavigation().stop();

                    if (mob instanceof TamableAnimal tamable) {
                        tamable.setOrderedToSit(false);
                    }

                    this.playerSetCommand(player, mob);
                }

                player.swing(hand, true);
                cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide()));
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