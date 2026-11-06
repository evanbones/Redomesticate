package com.evandev.redomesticate.content.block;

import com.evandev.redomesticate.registry.ModBlockEntities;
import com.evandev.redomesticate.content.block.entity.WaywardLanternBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;
//? if >=26.1 {
/*import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.util.RandomSource;
*///?}

public class WaywardLanternBlock extends BaseEntityBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Stream.of(
            Block.box(7, 12, 7, 9, 16, 9),
            Block.box(6, 11, 6, 10, 12, 10),
            Block.box(2, 9, 2, 14, 11, 14),
            Block.box(4, 2, 4, 12, 9, 12),
            Block.box(6, 0, 6, 10, 2, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public WaywardLanternBlock(Properties properties) {
        super(properties.mapColor(MapColor.COLOR_BLACK).pushReaction(PushReaction.BLOCK).sound(SoundType.LANTERN).strength(0.9F).lightLevel((i) -> 13).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    public @NotNull VoxelShape getShape(@NotNull BlockState p_49038_, @NotNull BlockGetter p_49039_, @NotNull BlockPos p_49040_, @NotNull CollisionContext p_49041_) {
        return SHAPE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }

    //? if >=26.1 {
    /*@Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random);
    }
    *///?} else {
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction direction, @NotNull BlockState state2, @NotNull LevelAccessor accessor, @NotNull BlockPos pos, @NotNull BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }

        return super.updateShape(state, direction, state2, accessor, pos, pos2);
    }
    //?}

    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> context) {
        context.add(WATERLOGGED);
    }

    //? if <26.3 {
    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
    //?}

    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WaywardLanternBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152180_, @NotNull BlockState p_152181_, @NotNull BlockEntityType<T> p_152182_) {
        return p_152180_.isClientSide() ? null : createTickerHelper(p_152182_, ModBlockEntities.WAYWARD_LANTERN.get(), WaywardLanternBlockEntity::tick);
    }

}
