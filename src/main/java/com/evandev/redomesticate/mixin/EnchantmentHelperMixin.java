package com.evandev.redomesticate.mixin;

import com.evandev.redomesticate.config.ModConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"))
    private static void redomesticate$filterDisabledEnchantments(int level, ItemStack stack, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        cir.getReturnValue().removeIf(instance -> {
            //? if >=26.1 {
            /*Holder<Enchantment> enchantment = instance.enchantment();
            *///?} else {
            Holder<Enchantment> enchantment = instance.enchantment;
            //?}
            return enchantment.unwrapKey().map(key -> !ModConfig.get().isEnchantmentEnabled(key)).orElse(false);
        });
    }
}
