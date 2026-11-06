package com.evandev.redomesticate.content.block;

import com.evandev.redomesticate.util.VersionCompat;
import com.evandev.redomesticate.api.ICommandableMob;
import com.evandev.redomesticate.api.PetCommand;
import com.evandev.redomesticate.content.block.entity.DrumBlockEntity;
import com.evandev.redomesticate.registry.ModSounds;
import com.evandev.redomesticate.util.TameableUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
//? if >=26.1 {
/*import net.minecraft.world.level.redstone.Orientation;
*///?}

public class DrumBlock extends BaseEntityBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty COMMAND = IntegerProperty.create("command", 0, 2);
    //? if <26.3 {
    public static final MapCodec<DrumBlock> CODEC = simpleCodec(DrumBlock::new);
    //?}
    private static final Random random = new Random();

    public DrumBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COMMAND, 0).setValue(POWERED, Boolean.FALSE));
    }

    //? if <26.3 {
    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> context) {
        context.add(COMMAND, POWERED);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        } else {
            BlockState cycledState = state.cycle(COMMAND);
            int newCommand = cycledState.getValue(COMMAND);
            level.setBlockAndUpdate(pos, cycledState);

            int count = issueCommand(level, pos, newCommand, player.getUUID());
            if (count > 0) {
                VersionCompat.sendMessage(player, Component.translatable("message.redomesticate.drum_command_" + newCommand, count), true);
            }

            player.playSound(ModSounds.DRUM.get(), 3, 0.3F + 0.4F * random.nextFloat());
            level.gameEvent(player, GameEvent.NOTE_BLOCK_PLAY, pos);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void setPlacedBy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (livingEntity != null && blockentity instanceof DrumBlockEntity drum) {
            drum.setPlacerUUID(livingEntity.getUUID());
        }
    }

    public int issueCommand(Level level, BlockPos pos, int command, @Nullable UUID issuer) {
        int count = 0;
        if (issuer != null) {
            Predicate<Entity> tames = (animal) -> TameableUtils.isTamed(animal)
                    && TameableUtils.getOwnerUUIDOf(animal) != null
                    && TameableUtils.getOwnerUUIDOf(animal).equals(issuer);
            AABB area = new AABB(
                    pos.getX() - 32, pos.getY() - 32, pos.getZ() - 32,
                    pos.getX() + 32, pos.getY() + 32, pos.getZ() + 32
            );
            for (Mob mob : level.getEntitiesOfClass(Mob.class, area, EntitySelector.NO_SPECTATORS.and(tames))) {
                if (mob instanceof ICommandableMob commandable) {
                    commandable.redomesticate$setCommand(command);
                    mob.getNavigation().stop();
                    mob.setTarget(null);
                    count++;
                }
                if (mob instanceof TamableAnimal tamable) {
                    boolean shouldSit = (command == PetCommand.SIT.getId());
                    tamable.setOrderedToSit(shouldSit);
                    tamable.setInSittingPose(shouldSit);
                    if (!(mob instanceof ICommandableMob)) {
                        count++;
                    }
                }
                if (mob instanceof Fox fox) {
                    fox.setSitting(command == PetCommand.SIT.getId());
                }
                mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, true, false));
            }
        }
        return count;
    }

    //? if >=26.1 {
    /*@Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            if (flag) {
                UUID uuid = null;
                if (level.getBlockEntity(pos) instanceof DrumBlockEntity drum) {
                    uuid = drum.getPlacerUUID();
                }
                this.issueCommand(level, pos, state.getValue(COMMAND), uuid);
                level.playSound(null, pos, ModSounds.DRUM.get(), SoundSource.BLOCKS, 3, 0.3F + 0.4F * random.nextFloat());
                level.gameEvent(null, GameEvent.NOTE_BLOCK_PLAY, pos);
            }
            level.setBlock(pos, state.setValue(POWERED, flag), 3);
        }
    }
    *///?} else {
    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos pos2, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            if (flag) {
                UUID uuid = null;
                if (level.getBlockEntity(pos) instanceof DrumBlockEntity drum) {
                    uuid = drum.getPlacerUUID();
                }
                this.issueCommand(level, pos, state.getValue(COMMAND), uuid);
                level.playSound(null, pos, ModSounds.DRUM.get(), SoundSource.BLOCKS, 3, 0.3F + 0.4F * random.nextFloat());
                level.gameEvent(null, GameEvent.NOTE_BLOCK_PLAY, pos);
            }
            level.setBlock(pos, state.setValue(POWERED, flag), 3);
        }
    }
    //?}

    //? if >=26.1 {
    /*@Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double f) {
        entity.playSound(ModSounds.DRUM.get(), 3, 0.6F + 0.4F * random.nextFloat());
        super.fallOn(level, state, pos, entity, f);
    }
    *///?} else {
    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float f) {
        entity.playSound(ModSounds.DRUM.get(), 3, 0.6F + 0.4F * random.nextFloat());
        super.fallOn(level, state, pos, entity, f);
    }
    //?}

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DrumBlockEntity(pos, state);
    }
}