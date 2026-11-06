package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public class VillagerMixin {

    @Inject(
            at = @At("TAIL"),
            method = "getPlayerReputation(Lnet/minecraft/world/entity/player/Player;)I",
            cancellable = true
    )
    private void getPlayerReputation(Player player, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() + TameableUtils.getCharismaBonusForOwner(player));
    }
}
