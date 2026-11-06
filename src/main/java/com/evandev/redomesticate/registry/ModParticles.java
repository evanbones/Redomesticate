package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ModParticles {
    public static final RegistrationProvider<ParticleType<?>> PARTICLE_REGISTRY = RegistrationProvider.get(Registries.PARTICLE_TYPE, Constants.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DEFLECTION_SHIELD = PARTICLE_REGISTRY.register("deflection_shield", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> MAGNET = PARTICLE_REGISTRY.register("magnet", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ZZZ = PARTICLE_REGISTRY.register("zzz", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GIANT_POP = PARTICLE_REGISTRY.register("giant_pop", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SIMPLE_BUBBLE = PARTICLE_REGISTRY.register("simple_bubble", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> VAMPIRE = PARTICLE_REGISTRY.register("vampire", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SNIFF = PARTICLE_REGISTRY.register("sniff", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> PSYCHIC_WALL = PARTICLE_REGISTRY.register("psychic_wall", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> INTIMIDATION = PARTICLE_REGISTRY.register("intimidation", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLIGHT = PARTICLE_REGISTRY.register("blight", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> QUESTION_MARK_PARTICLE_TYPE = PARTICLE_REGISTRY.register("question_mark_particle", () -> new SimpleParticleType(false));

    public static void init() {
    }
}
