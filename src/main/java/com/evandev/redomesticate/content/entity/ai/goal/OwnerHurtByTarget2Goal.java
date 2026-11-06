package com.evandev.redomesticate.content.entity.ai.goal;

import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.ITameableEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class OwnerHurtByTarget2Goal extends TargetGoal {
    private final Mob tameAnimal;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public OwnerHurtByTarget2Goal(Mob p_26114_) {
        super(p_26114_, false);
        this.tameAnimal = p_26114_;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (((ITameableEntity) this.tameAnimal).redomesticate$isTame() && !((ICommandableMob) this.tameAnimal).redomesticate$isStayingStill()) {
            LivingEntity livingentity = ((ITameableEntity) this.tameAnimal).redomesticate$getTameOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.ownerLastHurt = livingentity.getLastHurtByMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && ((ICommandableMob) this.tameAnimal).redomesticate$isValidAttackTarget(this.ownerLastHurt);
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity livingentity = ((ITameableEntity) this.tameAnimal).redomesticate$getTameOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtMobTimestamp();
        }
        super.start();
    }
}