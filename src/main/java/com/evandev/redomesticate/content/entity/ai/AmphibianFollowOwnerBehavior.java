package com.evandev.redomesticate.content.entity.ai;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.ITameableEntity;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.util.TameableUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.NotNull;

public class AmphibianFollowOwnerBehavior<T extends Animal> extends Behavior<T> {

    private static final float START_DISTANCE = 10F;
    private static final float STOP_DISTANCE = 2F;
    private final float baseSpeedLand;
    private final float baseSpeedWater;
    private LivingEntity owner;

    public AmphibianFollowOwnerBehavior(float baseSpeedLand, float baseSpeedWater) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), 20);
        this.baseSpeedLand = baseSpeedLand;
        this.baseSpeedWater = baseSpeedWater;
    }

    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T axolotl) {
        if (axolotl instanceof ITameableEntity tamed && axolotl instanceof ICommandableMob commandable) {
            owner = tamed.redomesticate$getTameOwner();
            return owner != null && owner.isAlive() && !owner.isSpectator() && commandable.redomesticate$isFollowingOwner();
        }
        return false;
    }

    protected boolean canStillUse(@NotNull ServerLevel level, T axolotl, long gameTime) {
        if (!axolotl.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET) && owner != null && owner.isAlive()) {
            return axolotl instanceof ICommandableMob cmd && cmd.redomesticate$isFollowingOwner() && axolotl.distanceTo(owner) > STOP_DISTANCE;
        }
        return false;
    }

    protected void stop(@NotNull ServerLevel level, T axolotl, long gameTime) {
        axolotl.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    protected void tick(@NotNull ServerLevel level, T axolotl, long gameTime) {
        if (axolotl.distanceToSqr(this.owner) >= 144.0D && !ModConfig.get().disablePetTeleportation) {
            this.teleportToOwner(axolotl);
        } else {
            int speedsterLevel = TameableUtils.getEnchantLevel(axolotl, ModEnchantments.SPEEDSTER);
            float speed = axolotl.isInWaterOrBubble() ? baseSpeedWater + speedsterLevel * 0.05F : baseSpeedLand + speedsterLevel * 0.1F;
            BehaviorUtils.lookAtEntity(axolotl, owner);
            BehaviorUtils.setWalkAndLookTargetMemories(axolotl, owner, speed, (int) STOP_DISTANCE);
        }
    }

    private int randomIntInclusive(int low, int high) {
        return this.owner.getRandom().nextInt(high - low + 1) + low;
    }

    private void teleportToOwner(T axolotl) {
        BlockPos blockpos = this.owner.blockPosition();

        for (int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(axolotl, blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private boolean maybeTeleportTo(T axolotl, int p_25304_, int p_25305_, int p_25306_) {
        if (Math.abs((double) p_25304_ - this.owner.getX()) < 2.0D && Math.abs((double) p_25306_ - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(axolotl, new BlockPos(p_25304_, p_25305_, p_25306_))) {
            return false;
        } else {
            axolotl.moveTo((double) p_25304_ + 0.5D, p_25305_, (double) p_25306_ + 0.5D, axolotl.getYRot(), axolotl.getXRot());
            axolotl.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            return true;
        }
    }

    private boolean canTeleportTo(T axolotl, BlockPos pos) {
        PathType pathtype = WalkNodeEvaluator.getPathTypeStatic(axolotl, pos.mutable());
        if (axolotl.level().getFluidState(pos).is(Fluids.WATER)) {
            return true;
        } else if (pathtype != PathType.WALKABLE) {
            return false;
        } else {
            BlockPos blockpos = pos.subtract(axolotl.blockPosition());
            return axolotl.level().noCollision(axolotl, axolotl.getBoundingBox().move(blockpos));
        }
    }
}