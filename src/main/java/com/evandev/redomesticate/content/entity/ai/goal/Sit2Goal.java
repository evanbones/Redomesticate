package com.evandev.redomesticate.content.entity.ai.goal;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.ITameableEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Fox;

import java.util.EnumSet;

public class Sit2Goal extends Goal {
    private final Mob mob;

    public Sit2Goal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canContinueToUse() {
        return ((ITameableEntity) this.mob).redomesticate$isTame() && ((ICommandableMob) this.mob).redomesticate$isStayingStill();
    }

    public boolean canUse() {
        if (!((ITameableEntity) this.mob).redomesticate$isTame()) {
            return false;
        } else if (this.mob.isInWaterOrBubble()) {
            return false;
        } else if (!this.mob.onGround()) {
            return false;
        } else {
            return ((ICommandableMob) this.mob).redomesticate$isStayingStill();
        }
    }

    public void start() {
        this.mob.getNavigation().stop();
        if (this.mob instanceof Fox fox) {
            fox.setSitting(true);
        }
    }

    public void stop() {
        if (this.mob instanceof Fox fox) {
            fox.setSitting(false);
        }
    }
}