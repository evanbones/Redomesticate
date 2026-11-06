package com.evandev.redomesticate.client.event;

import com.evandev.redomesticate.client.sound.DiscJockeySound;
import com.evandev.redomesticate.content.entity.FollowingJukeboxEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;

public class ClientJukeboxHandler {

    public static void updateEntityStatus(FollowingJukeboxEntity entity, byte updateKind) {
        SoundEvent record = entity.getRecordSound();
        if (entity.isAlive() && updateKind == 66) {
            DiscJockeySound sound;
            if (record != null && (DiscJockeySound.DISC_JOCKEY_SOUND_MAP.get(entity.getId()) == null || DiscJockeySound.DISC_JOCKEY_SOUND_MAP.get(entity.getId()).getRecordSound() != record)) {
                sound = new DiscJockeySound(record, entity);
                DiscJockeySound.DISC_JOCKEY_SOUND_MAP.put(entity.getId(), sound);
            } else {
                sound = DiscJockeySound.DISC_JOCKEY_SOUND_MAP.get(entity.getId());
            }
            if (sound != null && !Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound() && sound.isNearest()) {
                Minecraft.getInstance().getSoundManager().play(sound);
            }
        }
        if (updateKind == 67 || record == null) {
            if (DiscJockeySound.DISC_JOCKEY_SOUND_MAP.containsKey(entity.getId())) {
                DiscJockeySound sound = DiscJockeySound.DISC_JOCKEY_SOUND_MAP.get(entity.getId());
                DiscJockeySound.DISC_JOCKEY_SOUND_MAP.remove(entity.getId());
                Minecraft.getInstance().getSoundManager().stop(sound);
            }
        }
    }
}