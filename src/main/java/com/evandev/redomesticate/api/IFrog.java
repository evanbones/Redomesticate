package com.evandev.redomesticate.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public interface IFrog {

    boolean redomesticate$onFrogInteract(Player player, InteractionHand hand);
}
