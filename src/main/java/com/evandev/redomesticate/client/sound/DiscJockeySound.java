package com.evandev.redomesticate.client.sound;

import com.evandev.redomesticate.content.entity.FollowingJukeboxEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.util.HashMap;
import java.util.Map;

public class DiscJockeySound extends AbstractTickableSoundInstance {
    public static final Map<Integer, DiscJockeySound> DISC_JOCKEY_SOUND_MAP = new HashMap<>();

    private final FollowingJukeboxEntity box;
    private final SoundEvent recordSound;
    private int ticksExisted = 0;

    public DiscJockeySound(SoundEvent record, FollowingJukeboxEntity box) {
        super(record, SoundSource.RECORDS, box.level().getRandom());
        this.box = box;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.x = this.box.getX();
        this.y = this.box.getY();
        this.z = this.box.getZ();
        this.recordSound = record;
    }

    public boolean canPlaySound() {
        return !this.box.isSilent() && DISC_JOCKEY_SOUND_MAP.get(this.box.getId()) == this;
    }

    public boolean isNearest() {
        return true;
    }

    public void tick() {
        if (!this.box.isRemoved() && this.box.isAlive()) {
            this.volume = 1;
            this.pitch = 1;
            this.x = this.box.getX();
            this.y = this.box.getY();
            this.z = this.box.getZ();
        } else {
            this.stop();
            DISC_JOCKEY_SOUND_MAP.remove(box.getId());
        }
        ticksExisted++;
    }

    public SoundEvent getRecordSound() {
        return recordSound;
    }
}