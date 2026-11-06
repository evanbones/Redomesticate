package com.evandev.redomesticate.fabric.compat;

//? if fabric {
import com.evandev.redomesticate.compat.clothconfig.ClothConfigIntegration;
import com.evandev.redomesticate.platform.Services;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (Services.PLATFORM.isModLoaded("cloth-config")) {
            return ClothConfigIntegration::createScreen;
        }
        return null;
    }
}
//?}
