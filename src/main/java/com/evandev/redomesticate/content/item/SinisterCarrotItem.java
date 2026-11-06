package com.evandev.redomesticate.content.item;

import com.evandev.redomesticate.util.VersionCompat;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;
//? if >=26.1 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
*///?}

public class SinisterCarrotItem extends Item {

    public SinisterCarrotItem(Properties properties) {
        //? if >=26.1 {
        /*super(properties.rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).build(),
                Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.WITHER, 100), 1.0F)).build()));
        *///?} else {
        super(properties.rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).effect(new MobEffectInstance(MobEffects.WITHER, 100), 1.0F).build()));
        //?}
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (entity instanceof Rabbit rabbit) {
            if (rabbit.getVariant() != Rabbit.Variant.EVIL) {
                VersionCompat.swing(player, hand, false);
                rabbit.playSound(SoundEvents.RABBIT_ATTACK, 0.8F, rabbit.getVoicePitch());
                rabbit.playSound(SoundEvents.ZOMBIE_INFECT, 0.8F, rabbit.getVoicePitch());

                if (!player.level().isClientSide()) {
                    //? if >=26.1 {
                    /*rabbit.setComponent(DataComponents.RABBIT_VARIANT, Rabbit.Variant.EVIL);
                    *///?} else {
                    rabbit.setVariant(Rabbit.Variant.EVIL);
                    //?}
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }

                return VersionCompat.sidedSuccess(player.level().isClientSide());
            }
        }

        return InteractionResult.PASS;
    }
}