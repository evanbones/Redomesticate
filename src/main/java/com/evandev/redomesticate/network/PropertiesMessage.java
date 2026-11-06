package com.evandev.redomesticate.network;

import com.evandev.redomesticate.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record PropertiesMessage(String propertyID, CompoundTag compound, int entityID) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, PropertiesMessage> STREAM_CODEC =
            CustomPacketPayload.codec(PropertiesMessage::write, PropertiesMessage::new);
    public static final Type<PropertiesMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "pet_entity_tag"));

    public PropertiesMessage(final FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readNbt(), buf.readInt());
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(propertyID());
        pBuffer.writeNbt(compound().copy());
        pBuffer.writeInt(entityID());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
