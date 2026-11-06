package com.evandev.redomesticate.content.entity.ai;

import com.evandev.redomesticate.api.ICommandableMob;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;

public class AmphibianStayBehavior<T extends Animal> extends Behavior<T> {

    public AmphibianStayBehavior() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), 400);
    }

    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T axolotl) {
        return axolotl instanceof ICommandableMob cmd && cmd.redomesticate$isStayingStill();
    }

    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull T axolotl, long gameTime) {
        return axolotl instanceof ICommandableMob cmd && cmd.redomesticate$isStayingStill();
    }

    protected void stop(@NotNull ServerLevel p_23492_, @NotNull T axolotl, long gameTime) {
    }

    protected void tick(@NotNull ServerLevel p_23503_, T axolotl, long gameTime) {
        axolotl.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        axolotl.getNavigation().stop();
    }
}
