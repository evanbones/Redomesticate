package com.evandev.redomesticate.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.BiConsumer;

public class Networking {

    public static void register(IPayloadRegistrar registrar) {
        registrar.registerBidirectional(
                PropertiesMessage.TYPE,
                PropertiesMessage.STREAM_CODEC,
                ClientPayloadHandler::handleData,
                ServerPayloadHandler::handleData
        );
    }

    public interface IPayloadRegistrar {
        <T extends CustomPacketPayload> void registerBidirectional(
                CustomPacketPayload.Type<T> type,
                StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec,
                BiConsumer<T, IMessageContext> clientHandler,
                BiConsumer<T, IMessageContext> serverHandler
        );
    }
}