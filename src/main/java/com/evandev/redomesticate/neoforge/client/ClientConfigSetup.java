package com.evandev.redomesticate.neoforge.client;

//? if neoforge {
/*import com.evandev.redomesticate.compat.clothconfig.ClothConfigIntegration;
import com.evandev.redomesticate.platform.Services;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClientConfigSetup {
    public static void register(ModContainer container) {
        if (Services.PLATFORM.isModLoaded("cloth_config")) {
            container.registerExtensionPoint(IConfigScreenFactory.class, (c, parent) -> ClothConfigIntegration.createScreen(parent));
        }
    }
}
*///?}
