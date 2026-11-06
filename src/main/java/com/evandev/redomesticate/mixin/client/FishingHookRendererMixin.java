package com.evandev.redomesticate.mixin.client;

//? if >=26.1 {
/*import com.evandev.redomesticate.content.item.FeatherOnAStickItem;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/^*
 * Lets the vanilla "fishing rod is cast" item model property recognise the feather on a stick.
 ^/
@Mixin(FishingHookRenderer.class)
public class FishingHookRendererMixin {

    @Inject(method = "getHoldingArm", at = @At("HEAD"), cancellable = true)
    private static void redomesticate$featherOnAStickArm(Player owner, CallbackInfoReturnable<HumanoidArm> cir) {
        if (owner.getMainHandItem().getItem() instanceof FeatherOnAStickItem) {
            cir.setReturnValue(owner.getMainArm());
        }
    }
}
*///?}
