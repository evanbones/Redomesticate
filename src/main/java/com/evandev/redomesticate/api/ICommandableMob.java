package com.evandev.redomesticate.api;

import com.evandev.redomesticate.util.VersionCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;

public interface ICommandableMob {

    int redomesticate$getCommand();

    void redomesticate$setCommand(int command);

    boolean redomesticate$isStayingStill();

    boolean redomesticate$isFollowingOwner();

    boolean redomesticate$isValidAttackTarget(LivingEntity target);

    default PetCommand redomesticate$getPetCommand() {
        return PetCommand.fromId(redomesticate$getCommand());
    }

    default void redomesticate$setPetCommand(PetCommand command) {
        redomesticate$setCommand(command.getId());
    }

    default void playerSetCommand(Player owner, Mob ourselves) {
        if (!owner.level().isClientSide()) {
            PetCommand nextCommand = redomesticate$getPetCommand().next();
            this.redomesticate$setPetCommand(nextCommand);
            this.redomesticate$sendCommandMessage(owner, nextCommand.getId(), ourselves.getName());

            if (ourselves instanceof TamableAnimal tamable) {
                tamable.setOrderedToSit(nextCommand == PetCommand.SIT);
                tamable.setInSittingPose(nextCommand == PetCommand.SIT && !tamable.isInWaterOrBubble());
            }

            if (ourselves instanceof Fox fox) {
                fox.setSitting(nextCommand == PetCommand.SIT);
            }
        }
    }

    default void redomesticate$sendCommandMessage(Player owner, int command, Component name) {
        VersionCompat.sendMessage(owner, Component.translatable("message.redomesticate.command_" + command, name), true);
    }
}