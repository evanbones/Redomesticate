package com.evandev.redomesticate.neoforge.network;

//? if neoforge {
/*import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.network.IMessageContext;
import com.evandev.redomesticate.network.Networking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
//? if <26.1 {
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
//?}
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NeoForgeNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar neoRegistrar = event.registrar(Constants.MOD_ID);

        Networking.register(new Networking.IPayloadRegistrar() {
            @Override
            public <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, BiConsumer<T, IMessageContext> clientHandler, BiConsumer<T, IMessageContext> serverHandler) {
                //? if >=26.1 {
                /^neoRegistrar.playBidirectional(type, streamCodec,
                        (data, ctx) -> serverHandler.accept(data, wrapContext(ctx)),
                        (data, ctx) -> clientHandler.accept(data, wrapContext(ctx)));
                ^///?} else {
                neoRegistrar.playBidirectional(type, streamCodec, new DirectionalPayloadHandler<>(
                        (data, ctx) -> clientHandler.accept(data, wrapContext(ctx)),
                        (data, ctx) -> serverHandler.accept(data, wrapContext(ctx))
                ));
                //?}
            }
        });
    }

    private static IMessageContext wrapContext(IPayloadContext ctx) {
        return new IMessageContext() {
            @Override
            public CompletableFuture<Void> enqueueWork(Runnable runnable) {
                return ctx.enqueueWork(runnable);
            }

            @Override
            public void disconnect(Component reason) {
                ctx.disconnect(reason);
            }

            @Override
            public Player getPlayer() {
                return ctx.player();
            }
        };
    }
}
*///?}
