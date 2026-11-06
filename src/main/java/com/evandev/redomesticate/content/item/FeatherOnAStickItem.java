package com.evandev.redomesticate.content.item;

import com.evandev.redomesticate.content.entity.FeatherEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
//? if >=26.1 {
/*import net.minecraft.world.InteractionResult;
*///?} else {
import net.minecraft.world.InteractionResultHolder;
//?}

public class FeatherOnAStickItem extends Item {

    public FeatherOnAStickItem(Properties properties) {
        super(properties.durability(160));
    }

    @Override
    //? if >=26.1 {
    /*public @NotNull InteractionResult use(
    *///?} else {
    public @NotNull InteractionResultHolder<ItemStack> use(
    //?}
            @NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.fishing != null && player.fishing.isAlive()) {
            if (!level.isClientSide()) {
                int i = player.fishing.retrieve(itemstack);
                itemstack.hurtAndBreak(i, player, EquipmentSlot.MAINHAND);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            FeatherEntity featherEntity = new FeatherEntity(player, level);
            featherEntity.setOwner(player);
            if (!level.isClientSide()) {
                level.addFreshEntity(featherEntity);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        //? if >=26.1 {
        /*return InteractionResult.SUCCESS;
        *///?} else {
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        //?}
    }
}
