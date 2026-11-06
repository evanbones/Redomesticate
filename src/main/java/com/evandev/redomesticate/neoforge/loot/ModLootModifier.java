package com.evandev.redomesticate.neoforge.loot;

import com.evandev.redomesticate.util.VersionCompat;
//? if neoforge {
/*import com.evandev.redomesticate.config.ModConfig;
import com.evandev.redomesticate.registry.ModEnchantments;
import com.evandev.redomesticate.registry.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import javax.annotation.Nonnull;

public class ModLootModifier extends LootModifier {
    //? if >=26.3 {
    /^public static final MapCodec<ModLootModifier> CODEC =
            RecordCodecBuilder.mapCodec(inst ->
                    inst.group(
                                    LootItemCondition.CODEC.optionalFieldOf("condition").forGetter(lm -> lm.condition),
                                    Codec.INT.fieldOf("loot_type").orElse(0).forGetter((configuration) -> configuration.lootType)
                            )
                            .apply(inst, ModLootModifier::new));

    private final int lootType;

    protected ModLootModifier(Optional<Holder<LootItemCondition>> condition, int lootType) {
        super(condition, IGlobalLootModifier.DEFAULT_PRIORITY);
        this.lootType = lootType;
    }
    ^///?} else {
    public static final MapCodec<ModLootModifier> CODEC =
            RecordCodecBuilder.mapCodec(inst ->
                    inst.group(
                                    LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions),
                                    Codec.INT.fieldOf("loot_type").orElse(0).forGetter((configuration) -> configuration.lootType)
                            )
                            .apply(inst, ModLootModifier::new));

    private final int lootType;

    protected ModLootModifier(LootItemCondition[] conditionsIn, int lootType) {
        //? if >=26.1 {
        /^super(conditionsIn, IGlobalLootModifier.DEFAULT_PRIORITY);
        ^///?} else {
        super(conditionsIn);
        //?}
        this.lootType = lootType;
    }
    //?}

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        switch (lootType) {
            case 0 -> {
                if (context.getRandom().nextFloat() < ModConfig.get().sinisterCarrotLootChance) {
                    generatedLoot.add(new ItemStack(ModItems.SINISTER_CARROT.get(), context.getRandom().nextInt(1, 2)));
                }
            }
            case 1 -> {
                if (context.getRandom().nextFloat() < ModConfig.get().bubblingLootChance) {
                    generatedLoot.add(enchantedBook(ModEnchantments.BUBBLING, context.getRandom(), context));
                }
            }
            case 2 -> {
                if (context.getRandom().nextFloat() < ModConfig.get().vampirismLootChance) {
                    generatedLoot.add(enchantedBook(ModEnchantments.VAMPIRE, context.getRandom(), context));
                }
            }
            case 3 -> {
                if (context.getRandom().nextFloat() < ModConfig.get().voidCloudLootChance) {
                    generatedLoot.add(enchantedBook(ModEnchantments.VOID_CLOUD, context.getRandom(), context));
                }
            }
            case 4 -> {
                if (context.getRandom().nextFloat() < ModConfig.get().oreScentingLootChance) {
                    generatedLoot.add(enchantedBook(ModEnchantments.ORE_SCENTING, context.getRandom(), context));
                }
            }
            case 5 -> {
                if (context.getRandom().nextFloat() < ModConfig.get().muffledLootChance) {
                    generatedLoot.add(enchantedBook(ModEnchantments.MUFFLED, context.getRandom(), context));
                }
            }
            case 6 -> {
                if (context.getRandom().nextFloat() < ModConfig.get().blazingProtectionLootChance) {
                    generatedLoot.add(enchantedBook(ModEnchantments.BLAZING_PROTECTION, context.getRandom(), context));
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + lootType);
        }
        return generatedLoot;
    }

    private ItemStack enchantedBook(ResourceKey<Enchantment> enchantmentKey, RandomSource randomSource, LootContext context) {
        HolderLookup.Provider registries = context.getLevel().registryAccess();
        Holder<Enchantment> enchant = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantmentKey);

        int maxLevels = enchant.value().getMaxLevel();

        int level = maxLevels > 1 ? 1 + randomSource.nextInt(maxLevels) : 1;

        return VersionCompat.createEnchantedBook(new EnchantmentInstance(enchant, level));
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
*///?}
