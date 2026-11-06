package com.evandev.redomesticate.fabric.platform;

//? if fabric {
//? if >=26.1 {
/*import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.data.PetState;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

public class FabricAttachments {
    public static final AttachmentType<PetState> PET_STATE = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "pet_state"),
            builder -> builder.initializer(() -> PetState.DEFAULT).syncWith(PetState.STREAM_CODEC, AttachmentSyncPredicate.all()));

    public static void init() {
    }
}
*///?}
//?}
