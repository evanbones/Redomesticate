package com.evandev.redomesticate.content.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CollarTagItem extends Item {

    public CollarTagItem(Properties properties) {
        //? if >=26.1 {
        /*super(properties.enchantable(10));
        *///?} else {
        super(properties);
        //?}
    }
    //? if <26.1 {

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }
    //?}
}
