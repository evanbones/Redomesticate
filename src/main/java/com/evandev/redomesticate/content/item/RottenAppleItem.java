package com.evandev.redomesticate.content.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
//? if >=26.1 {
/*import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
*///?}

public class RottenAppleItem extends Item {

    public RottenAppleItem(Properties properties) {
        //? if >=26.1 {
        /*super(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build(),
                Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 1), 1.0F)).build()));
        *///?} else {
        super(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).effect(new MobEffectInstance(MobEffects.POISON, 100, 1), 1.0F).build()));
        //?}
    }
}