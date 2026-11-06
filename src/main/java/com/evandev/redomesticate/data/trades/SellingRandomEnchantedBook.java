package com.evandev.redomesticate.data.trades;

import com.evandev.redomesticate.util.VersionCompat;
//? if <26.1 {
import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SellingRandomEnchantedBook implements VillagerTrades.ItemListing {
    private final int villagerXp;

    public SellingRandomEnchantedBook(int villagerXp) {
        this.villagerXp = villagerXp;
    }

    @Override
    public MerchantOffer getOffer(Entity trader, @NotNull RandomSource random) {
        List<Holder<Enchantment>> validEnchants = new ArrayList<>();
        var enchantRegistry = trader.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        for (Holder<Enchantment> holder : enchantRegistry.getTagOrEmpty(ModTags.TRADABLE_ENCHANTMENT_KEY)) {
            if (holder.unwrapKey().isPresent() && ModConfig.get().isEnchantmentEnabled(holder.unwrapKey().get().location())) {
                validEnchants.add(holder);
            }
        }

        Optional<Holder<Enchantment>> optional = validEnchants.isEmpty()
                ? Optional.empty()
                : Optional.of(validEnchants.get(random.nextInt(validEnchants.size())));

        int i;
        ItemStack itemstack;

        if (optional.isPresent()) {
            Holder<Enchantment> holder = optional.get();
            Enchantment enchantment = holder.value();
            int j = Math.max(enchantment.getMinLevel(), 0);
            int k = enchantment.getMaxLevel();
            int l = Mth.nextInt(random, j, k);

            itemstack = VersionCompat.createEnchantedBook(new EnchantmentInstance(holder, l));
            i = 2 + random.nextInt(5 + l * 10) + 3 * l;

            if (holder.is(EnchantmentTags.DOUBLE_TRADE_PRICE)) {
                i *= 2;
            }
            if (i > 64) {
                i = 64;
            }
        } else {
            i = 1;
            itemstack = new ItemStack(Items.BOOK);
        }

        return new MerchantOffer(new ItemCost(Items.EMERALD, i), Optional.of(new ItemCost(Items.BOOK)), itemstack, 12, villagerXp, 0.2F);
    }
}
//?}
