package com.evandev.redomesticate.neoforge.platform;

//? if neoforge {
/*//? if >=26.1 {
/^import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.data.PetState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NeoForgeAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PetState>> PET_STATE = ATTACHMENT_TYPES.register("pet_state",
            () -> AttachmentType.builder(() -> PetState.DEFAULT).sync(PetState.STREAM_CODEC).build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
^///?}
*///?}
