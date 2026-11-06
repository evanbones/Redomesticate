package com.evandev.redomesticate.mixin.client;

//? if >=26.1 {
/*import com.evandev.redomesticate.client.util.EntityRenderStateAccess;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAccess {
    @Unique
    private @Nullable Entity redomesticate$entity;
    @Unique
    private float redomesticate$partialTicks;

    @Override
    public @Nullable Entity redomesticate$getEntity() {
        return this.redomesticate$entity;
    }

    @Override
    public float redomesticate$getPartialTicks() {
        return this.redomesticate$partialTicks;
    }

    @Override
    public void redomesticate$set(Entity entity, float partialTicks) {
        this.redomesticate$entity = entity;
        this.redomesticate$partialTicks = partialTicks;
    }
}
*///?}
