package com.evandev.redomesticate.network;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.LangDefinition;
import com.evandev.redomesticate.util.ModEntityData;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();
    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public static void handleData(final PropertiesMessage data, final IMessageContext context) {
        context.enqueueWork(() -> {
                    var level = context.getPlayer().level();
                    Entity e = level.getEntity(data.entityID());
                    if (e instanceof LivingEntity && (data.propertyID().equals(Constants.ENTITY_DATA_TAG_UPDATE))) {
                        ModEntityData.setEntityTag((LivingEntity) e, data.compound());
                    }
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable(LangDefinition.network_failed, e.getMessage()));
                    return null;
                });
    }
}