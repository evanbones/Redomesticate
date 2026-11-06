package com.evandev.redomesticate.mixin;

//? if >=26.1 {
/*import com.evandev.redomesticate.config.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.TradeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/^*
 * Trades are data-driven on 26.x, so the per-enchantment config toggles can't be expressed in the trade JSON.
 * Strip disabled enchantments from freshly generated offers instead, dropping enchanted books left empty.
 ^/
@Mixin(AbstractVillager.class)
public class AbstractVillagerMixin {

    @Inject(method = "addOffersFromTradeSet", at = @At("TAIL"))
    private void redomesticate$filterDisabledEnchantments(ServerLevel level, MerchantOffers offers, ResourceKey<TradeSet> tradeSet, CallbackInfo ci) {
        offers.removeIf(offer -> {
            ItemStack result = offer.getResult();
            EnchantmentHelper.updateEnchantments(result, enchantments ->
                    enchantments.removeIf(holder -> holder.unwrapKey().map(key -> !ModConfig.get().isEnchantmentEnabled(key)).orElse(false)));
            ItemEnchantments stored = result.get(DataComponents.STORED_ENCHANTMENTS);
            return stored != null && stored.isEmpty();
        });
    }
}
*///?}
