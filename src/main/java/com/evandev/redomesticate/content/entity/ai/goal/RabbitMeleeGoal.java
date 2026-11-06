package com.evandev.redomesticate.content.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Animal;

public class RabbitMeleeGoal extends MeleeAttackGoal {
    public RabbitMeleeGoal(Animal animal) {
        super(animal, 1.4D, true);
    }
}