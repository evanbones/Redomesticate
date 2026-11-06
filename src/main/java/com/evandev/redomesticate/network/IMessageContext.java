package com.evandev.redomesticate.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;

public interface IMessageContext {

    CompletableFuture<Void> enqueueWork(Runnable runnable);

    void disconnect(Component reason);

    Player getPlayer();
}