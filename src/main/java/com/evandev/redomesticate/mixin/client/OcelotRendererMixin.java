package com.evandev.redomesticate.mixin.client;

//? if >=26.1 {
/*import com.evandev.redomesticate.api.ICommandableMob;
import net.minecraft.client.renderer.entity.OcelotRenderer;
import net.minecraft.client.renderer.entity.state.FelineRenderState;
import net.minecraft.world.entity.animal.feline.Ocelot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/^*
 * Ocelots have no vanilla sitting state, so tamed ones told to stay borrow the cat's sitting pose.
 ^/
@Mixin(OcelotRenderer.class)
public class OcelotRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/feline/Ocelot;Lnet/minecraft/client/renderer/entity/state/FelineRenderState;F)V", at = @At("TAIL"))
    private void redomesticate$applySittingPose(Ocelot entity, FelineRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity instanceof ICommandableMob commandable && commandable.redomesticate$isStayingStill()) {
            state.isSitting = true;
        }
    }
}
*///?}
