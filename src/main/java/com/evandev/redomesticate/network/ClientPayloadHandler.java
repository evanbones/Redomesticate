package com.evandev.redomesticate.network;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.util.ModEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ClientPayloadHandler {
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public static void handleData(final PropertiesMessage data, final IMessageContext context) {
        context.enqueueWork(() -> {
                    var compound = data.compound();
                    var entityID = data.entityID();
                    var propertyID = data.propertyID();
                    if (compound != null && Minecraft.getInstance().level != null) {
                        Entity entity = Minecraft.getInstance().level.getEntity(entityID);
                        if ((propertyID.equals(Constants.ENTITY_DATA_TAG_UPDATE)) && entity instanceof LivingEntity) {
                            ModEntityData.setEntityTag((LivingEntity) entity, compound);
                        }

                    }
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("text.redomesticate.network_failed", e.getMessage()));
                    return null;
                });
    }
}