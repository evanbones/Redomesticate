package com.evandev.redomesticate.client.util;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface EntityRenderStateAccess {
    @Nullable
    Entity redomesticate$getEntity();

    float redomesticate$getPartialTicks();

    void redomesticate$set(Entity entity, float partialTicks);
}
