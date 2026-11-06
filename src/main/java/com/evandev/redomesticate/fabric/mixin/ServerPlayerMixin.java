package com.evandev.redomesticate.fabric.mixin;

//? if fabric {
import com.evandev.redomesticate.event.EventProxy;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    //? if >=26.1 {
    /*@Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z", at = @At("HEAD"))
    private void redomesticate$onPlayerTeleport(ServerLevel level, double x, double y, double z, Set<?> relativeMovements, float yRot, float xRot, boolean resetCamera, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FF)Z", at = @At("HEAD"))
    private void redomesticate$onPlayerTeleport(ServerLevel level, double x, double y, double z, Set<?> relativeMovements, float yRot, float xRot, CallbackInfoReturnable<Boolean> cir) {
    //?}
        ServerPlayer player = (ServerPlayer) (Object) this;
        EventProxy.onEntityTeleport(player, player.position(), new Vec3(x, y, z));
    }
}
//?}
