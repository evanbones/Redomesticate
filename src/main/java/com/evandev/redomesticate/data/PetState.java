package com.evandev.redomesticate.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public record PetState(int command, boolean tamed, Optional<UUID> owner) {
    public static final PetState DEFAULT = new PetState(0, false, Optional.empty());

    public static final StreamCodec<ByteBuf, PetState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PetState::command,
            ByteBufCodecs.BOOL, PetState::tamed,
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), PetState::owner,
            PetState::new
    );

    public PetState withCommand(int command) {
        return new PetState(command, this.tamed, this.owner);
    }

    public PetState withTamed(boolean tamed) {
        return new PetState(this.command, tamed, this.owner);
    }

    public PetState withOwner(@Nullable UUID owner) {
        return new PetState(this.command, this.tamed, Optional.ofNullable(owner));
    }
}
