package com.evandev.redomesticate.content.item;

import com.evandev.redomesticate.util.NbtCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
//? if >=26.1 {
/*import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
*///?}

public class DeedOfOwnershipItem extends Item {

    public DeedOfOwnershipItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static boolean isBound(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().contains("HasBoundEntity") && NbtCompat.getBoolean(customData.copyTag(), "HasBoundEntity");
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return super.isFoil(stack) || isBound(stack);
    }

    //? if >=26.1 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (isBound(stack) && customData != null && customData.copyTag().contains("BoundEntityName")) {
            tooltip.accept(Component.translatable("item.redomesticate.deed_of_ownership.desc", NbtCompat.getString(customData.copyTag(), "BoundEntityName")).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.accept(Component.translatable("item.redomesticate.deed_of_ownership.desc_unbound").withStyle(ChatFormatting.GRAY));
        }
    }
    *///?} else {
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (isBound(stack) && customData != null && customData.copyTag().contains("BoundEntityName")) {
            tooltipComponents.add(Component.translatable("item.redomesticate.deed_of_ownership.desc", NbtCompat.getString(customData.copyTag(), "BoundEntityName")).withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable("item.redomesticate.deed_of_ownership.desc_unbound").withStyle(ChatFormatting.GRAY));
        }
    }
    //?}
}