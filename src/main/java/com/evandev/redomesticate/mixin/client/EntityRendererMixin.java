package com.evandev.redomesticate.mixin.client;

//? if >=26.1 {
/*import com.evandev.redomesticate.client.util.EntityRenderStateAccess;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void redomesticate$rememberEntity(T entity, S state, float partialTicks, CallbackInfo ci) {
        ((EntityRenderStateAccess) state).redomesticate$set(entity, partialTicks);
    }
}
*///?}
