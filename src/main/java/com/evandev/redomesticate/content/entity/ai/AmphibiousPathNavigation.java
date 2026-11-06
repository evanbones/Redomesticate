package com.evandev.redomesticate.content.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AmphibiousPathNavigation extends WaterBoundPathNavigation {

    public AmphibiousPathNavigation(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected @NotNull PathFinder createPathFinder(int i) {
        this.nodeEvaluator = new AmphibiousNodeEvaluator(true);
        return new PathFinder(this.nodeEvaluator, i);
    }

    protected boolean canUpdatePath() {
        return true;
    }

    protected @NotNull Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
    }

    protected boolean canMoveDirectly(@NotNull Vec3 posVec31, Vec3 posVec32) {
        Vec3 vector3d = new Vec3(posVec32.x, posVec32.y + (double) this.mob.getBbHeight() * 0.5D, posVec32.z);
        return this.level.clip(new ClipContext(posVec31, vector3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob)).getType() == HitResult.Type.MISS;
    }

    public boolean isStableDestination(BlockPos pos) {
        return !this.level.getBlockState(pos.below()).isAir() || this.level.isWaterAt(pos.below());
    }

    public void setCanFloat(boolean canSwim) {
    }
}
