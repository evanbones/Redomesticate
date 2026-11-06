package com.evandev.redomesticate.registry;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.platform.registry.RegistrationProvider;
import com.evandev.redomesticate.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final RegistrationProvider<SoundEvent> SOUND_REGISTRRY = RegistrationProvider.get(Registries.SOUND_EVENT, Constants.MOD_ID);

    public static final RegistryObject<SoundEvent> COLLAR_TAG = createSoundEvent("collar_tag");
    public static final RegistryObject<SoundEvent> MAGNET_LOOP = createSoundEvent("magnet_loop");
    public static final RegistryObject<SoundEvent> CHAIN_LIGHTNING = createSoundEvent("chain_lightning");
    public static final RegistryObject<SoundEvent> GIANT_BUBBLE_INFLATE = createSoundEvent("giant_bubble_inflate");
    public static final RegistryObject<SoundEvent> GIANT_BUBBLE_POP = createSoundEvent("giant_bubble_pop");
    public static final RegistryObject<SoundEvent> PET_BED_USE = createSoundEvent("pet_bed_use");
    public static final RegistryObject<SoundEvent> DRUM = createSoundEvent("drum");
    public static final RegistryObject<SoundEvent> PSYCHIC_WALL = createSoundEvent("psychic_wall");
    public static final RegistryObject<SoundEvent> PSYCHIC_WALL_DEFLECT = createSoundEvent("psychic_wall_deflect");
    public static final RegistryObject<SoundEvent> BLAZING_PROTECTION = createSoundEvent("blazing_protection");

    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
        return SOUND_REGISTRRY.register(soundName, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, soundName)));
    }

    public static void init() {
    }
}
