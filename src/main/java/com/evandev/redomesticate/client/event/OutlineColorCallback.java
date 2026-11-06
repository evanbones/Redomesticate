package com.evandev.redomesticate.client.event;

import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public interface OutlineColorCallback {

    List<OutlineColorCallback> LISTENERS = new ArrayList<>();

    static Integer invoke(Entity entity) {
        for (OutlineColorCallback listener : LISTENERS) {
            Integer color = listener.getOutlineColor(entity);
            if (color != null) {
                return color;
            }
        }
        return null;
    }

    Integer getOutlineColor(Entity entity);
}