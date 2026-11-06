package com.evandev.redomesticate.fabric.network;

//? if fabric {
import com.evandev.redomesticate.network.IMessageContext;
import com.evandev.redomesticate.network.Networking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FabricNetworking {

    public static void initMain() {
        Networking.register(new Networking.IPayloadRegistrar() {
            @Override
            public <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, BiConsumer<T, IMessageContext> clientHandler, BiConsumer<T, IMessageContext> serverHandler) {
                PayloadTypeRegistry.playC2S().register(type, streamCodec);
                PayloadTypeRegistry.playS2C().register(type, streamCodec);

                ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                    serverHandler.accept(payload, new IMessageContext() {
                        @Override
                        public CompletableFuture<Void> enqueueWork(Runnable runnable) {
                            context.server().execute(runnable);
                            return CompletableFuture.completedFuture(null);
                        }

                        @Override
                        public void disconnect(Component reason) {
                            context.player().connection.disconnect(reason);
                        }

                        @Override
                        public Player getPlayer() {
                            return context.player();
                        }
                    });
                });
            }
        });
    }

    public static void initClient() {
        Networking.register(new Networking.IPayloadRegistrar() {
            @Override
            public <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, BiConsumer<T, IMessageContext> clientHandler, BiConsumer<T, IMessageContext> serverHandler) {
                ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                    clientHandler.accept(payload, new IMessageContext() {
                        @Override
                        public CompletableFuture<Void> enqueueWork(Runnable runnable) {
                            context.client().execute(runnable);
                            return CompletableFuture.completedFuture(null);
                        }

                        @Override
                        public void disconnect(Component reason) {
                            context.client().player.connection.getConnection().disconnect(reason);
                        }

                        @Override
                        public Player getPlayer() {
                            return context.player();
                        }
                    });
                });
            }
        });
    }
}
//?}
