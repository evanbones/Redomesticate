package com.evandev.redomesticate.mixin.accessor;

import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ExperienceOrb.class)
public interface ExperienceOrbAccessor {
    //? if >=26.1 {
    /*@Invoker("setValue")
    *///?} else {
    @Accessor("value")
    //?}
    void redomesticate$setValue(int value);
}