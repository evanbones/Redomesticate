package com.evandev.redomesticate.content.item;

import com.evandev.redomesticate.data.CustomTabBehavior;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class InventoryOnlyItem extends Item implements CustomTabBehavior {
    public InventoryOnlyItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab.Output contents) {
    }
}
