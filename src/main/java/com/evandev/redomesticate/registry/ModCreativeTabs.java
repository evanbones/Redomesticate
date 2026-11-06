package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.util.VersionCompat;
import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import com.evandev.redomesticate.data.CustomTabBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class ModCreativeTabs {
    public static final RegistrationProvider<CreativeModeTab> TAB_REGISTRY = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> TAB = TAB_REGISTRY.register(Constants.MOD_ID, () ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + Constants.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.COLLAR_TAG.get()))
                    .displayItems((parameters, output) -> {
                        for (var item : ModItems.ITEM_REGISTRY.getEntries()) {
                            if (item.get() instanceof CustomTabBehavior customTabBehavior) {
                                customTabBehavior.fillItemCategory(output);
                            } else {
                                output.accept(item.get());
                            }
                        }
                        parameters.holders().lookup(Registries.ENCHANTMENT).ifPresent(enchantmentRegistry -> {
                            enchantmentRegistry.listElements()
                                    .filter(holder -> holder.key().location().getNamespace().equals(Constants.MOD_ID))
                                    .filter(holder -> ModConfig.get().isEnchantmentEnabled(holder.key()))
                                    .forEach(holder -> {
                                        output.accept(VersionCompat.createEnchantedBook(
                                                new EnchantmentInstance(holder, holder.value().getMaxLevel())
                                        ));
                                    });
                        });
                    })
                    .build());

    public static void init() {
    }
}