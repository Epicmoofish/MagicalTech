package org.oceanic.magical_tech.blocks.abstractions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.oceanic.magical_tech.MagicalTech;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractPipeConnection extends AbstractPipe implements EntityBlock {
    public static final IntegerProperty CONNECTION_UP = IntegerProperty.create("connection_up", 0, 2);
    public static final IntegerProperty CONNECTION_DOWN = IntegerProperty.create("connection_down", 0, 2);
    public static final IntegerProperty CONNECTION_NORTH = IntegerProperty.create("connection_north", 0, 2);
    public static final IntegerProperty CONNECTION_SOUTH = IntegerProperty.create("connection_south", 0, 2);
    public static final IntegerProperty CONNECTION_WEST = IntegerProperty.create("connection_west", 0, 2);
    public static final IntegerProperty CONNECTION_EAST = IntegerProperty.create("connection_east", 0, 2);
    public AbstractPipeConnection(Properties properties) {
        super(properties, false);
        registerDefaultState(defaultBlockState()
                .setValue(CONNECTION_UP, 0)
                .setValue(CONNECTION_DOWN, 0)
                .setValue(CONNECTION_NORTH, 0)
                .setValue(CONNECTION_SOUTH, 0)
                .setValue(CONNECTION_WEST, 0)
                .setValue(CONNECTION_EAST, 0)
        );
    }
    public int getConnectionVal(Direction dir, BlockState state) {
        int a = 0;
        if (dir == Direction.UP) a = state.getValue(CONNECTION_UP);
        if (dir == Direction.DOWN) a = state.getValue(CONNECTION_DOWN);
        if (dir == Direction.NORTH) a = state.getValue(CONNECTION_NORTH);
        if (dir == Direction.SOUTH) a = state.getValue(CONNECTION_SOUTH);
        if (dir == Direction.EAST) a = state.getValue(CONNECTION_EAST);
        if (dir == Direction.WEST) a = state.getValue(CONNECTION_WEST);
        return a;
    }
    public abstract BlockState decayState(BlockState state);
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block blockOld, BlockPos blockPos2, boolean bl) {
        Direction dir = MagicalTech.relativeDirection(blockPos, blockPos2);
        Block block = level.getBlockState(blockPos2).getBlock();
        if (!(block instanceof AbstractPipe && blockOld instanceof AbstractPipe)) {
            if (dir != null) {
                if (dir == Direction.UP) {
                    int current = blockState.getValue(CONNECTION_UP);
                    boolean pipeConnection = this.canConnect(block);
                    boolean teConnection = this.canConnectTile(block);
                    int value = 0;
                    if (pipeConnection) value = 1;
                    if (teConnection) value = current;
                    if (current != value) {
                        level.setBlockAndUpdate(blockPos, decayState(blockState.setValue(CONNECTION_UP, value)));
                    }
                }
                if (dir == Direction.DOWN) {
                    int current = blockState.getValue(CONNECTION_DOWN);
                    boolean pipeConnection = this.canConnect(block);
                    boolean teConnection = this.canConnectTile(block);
                    int value = 0;
                    if (pipeConnection) value = 1;
                    if (teConnection) value = current;
                    if (current != value) {
                        level.setBlockAndUpdate(blockPos, decayState(blockState.setValue(CONNECTION_DOWN, value)));
                    }
                }
                if (dir == Direction.NORTH) {
                    int current = blockState.getValue(CONNECTION_NORTH);
                    boolean pipeConnection = this.canConnect(block);
                    boolean teConnection = this.canConnectTile(block);
                    int value = 0;
                    if (pipeConnection) value = 1;
                    if (teConnection) value = current;
                    if (current != value) {
                        level.setBlockAndUpdate(blockPos, decayState(blockState.setValue(CONNECTION_NORTH, value)));
                    }
                }
                if (dir == Direction.SOUTH) {
                    int current = blockState.getValue(CONNECTION_SOUTH);
                    boolean pipeConnection = this.canConnect(block);
                    boolean teConnection = this.canConnectTile(block);
                    int value = 0;
                    if (pipeConnection) value = 1;
                    if (teConnection) value = current;
                    if (current != value) {
                        level.setBlockAndUpdate(blockPos, decayState(blockState.setValue(CONNECTION_SOUTH, value)));
                    }
                }
                if (dir == Direction.EAST) {
                    int current = blockState.getValue(CONNECTION_EAST);
                    boolean pipeConnection = this.canConnect(block);
                    boolean teConnection = this.canConnectTile(block);
                    int value = 0;
                    if (pipeConnection) value = 1;
                    if (teConnection) value = current;
                    if (current != value) {
                        level.setBlockAndUpdate(blockPos, decayState(blockState.setValue(CONNECTION_EAST, value)));
                    }
                }
                if (dir == Direction.WEST) {
                    int current = blockState.getValue(CONNECTION_WEST);
                    boolean pipeConnection = this.canConnect(block);
                    boolean teConnection = this.canConnectTile(block);
                    int value = 0;
                    if (pipeConnection) value = 1;
                    if (teConnection) value = current;
                    if (current != value) {
                        level.setBlockAndUpdate(blockPos, decayState(blockState.setValue(CONNECTION_WEST, value)));
                    }
                }
            }
        }
        super.neighborChangedSuperer(blockState, level, blockPos, block, blockPos2, bl);
    }
    public VoxelShape getConnectionShape(int connectionType, boolean increasing, int xyorz) {
        double defStart = 0.35;
        double defEnd = 0.65;
        double updatedStart = increasing ? 0.65 : 0;
        double updatedEnd = increasing ? 1.0 : 0.35;
        Vec3 start2 = new Vec3(updatedStart, defStart, defStart);
        Vec3 end2 = new Vec3(updatedEnd, defEnd, defEnd);
        if (xyorz == 1) {
            start2 = new Vec3(defStart, updatedStart, defStart);
            end2 = new Vec3(defEnd, updatedEnd, defEnd);
        }
        if (xyorz == 2) {
            start2 = new Vec3(defStart, defStart, updatedStart);
            end2 = new Vec3(defEnd, defEnd, updatedEnd);
        }
        VoxelShape addition = Shapes.create(new AABB(start2, end2));
        if (connectionType == 2) {
            double defStart2 = 0.25;
            double defEnd2 = 0.75;
            double updatedStart2 = increasing ? 0.90 : 0;
            double updatedEnd2 = increasing ? 1.0 : 0.1;
            Vec3 start = new Vec3(updatedStart2, defStart2, defStart2);
            Vec3 end = new Vec3(updatedEnd2, defEnd2, defEnd2);
            if (xyorz == 1) {
                start = new Vec3(defStart2, updatedStart2, defStart2);
                end = new Vec3(defEnd2, updatedEnd2, defEnd2);
            }
            if (xyorz == 2) {
                start = new Vec3(defStart2, defStart2, updatedStart2);
                end = new Vec3(defEnd2, defEnd2, updatedEnd2);
            }
            addition = Shapes.join(addition, Shapes.create(new AABB(start, end)), BooleanOp.OR);
        }
        return addition;
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Vec3 start = new Vec3(0.35, 0.35, 0.35);
        Vec3 end = new Vec3(0.65, 0.65, 0.65);
        VoxelShape shape = Shapes.create(new AABB(start, end));
        if (blockState.getValue(CONNECTION_UP) != 0) {
            VoxelShape addition = getConnectionShape(blockState.getValue(CONNECTION_UP), true, 1);
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_DOWN) != 0) {
            VoxelShape addition = getConnectionShape(blockState.getValue(CONNECTION_DOWN), false, 1);
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_NORTH) != 0) {
            VoxelShape addition = getConnectionShape(blockState.getValue(CONNECTION_NORTH), false, 2);
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_SOUTH) != 0) {
            VoxelShape addition = getConnectionShape(blockState.getValue(CONNECTION_SOUTH), true, 2);
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_EAST) != 0) {
            VoxelShape addition = getConnectionShape(blockState.getValue(CONNECTION_EAST), true, 0);
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_WEST) != 0) {
            VoxelShape addition = getConnectionShape(blockState.getValue(CONNECTION_WEST), false, 0);
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        return shape;
    }
    protected List<Direction> directions(BlockState state) {
        List<Direction> arr = new ArrayList<>();
        if (state.getValue(CONNECTION_UP) != 0) {
            arr.add(Direction.UP);
        }
        if (state.getValue(CONNECTION_DOWN) != 0) {
            arr.add(Direction.DOWN);
        }
        if (state.getValue(CONNECTION_NORTH) != 0) {
            arr.add(Direction.NORTH);
        }
        if (state.getValue(CONNECTION_SOUTH) != 0) {
            arr.add(Direction.SOUTH);
        }
        if (state.getValue(CONNECTION_EAST) != 0) {
            arr.add(Direction.EAST);
        }
        if (state.getValue(CONNECTION_WEST) != 0) {
            arr.add(Direction.WEST);
        }
        return arr;
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos getPlacedPosition = blockPlaceContext.getClickedPos();
        Level world = blockPlaceContext.getLevel();
        BlockState stateForPlacement = this.defaultBlockState();
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.UP)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_UP, 1);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.DOWN)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_DOWN, 1);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.NORTH)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_NORTH, 1);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.SOUTH)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_SOUTH, 1);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.EAST)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_EAST, 1);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.WEST)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_WEST, 1);
        }

        if (this.canConnectTile(world.getBlockState(getPlacedPosition.relative(Direction.UP)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_UP, 2);
        }
        if (this.canConnectTile(world.getBlockState(getPlacedPosition.relative(Direction.DOWN)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_DOWN, 2);
        }
        if (this.canConnectTile(world.getBlockState(getPlacedPosition.relative(Direction.NORTH)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_NORTH, 2);
        }
        if (this.canConnectTile(world.getBlockState(getPlacedPosition.relative(Direction.SOUTH)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_SOUTH, 2);
        }
        if (this.canConnectTile(world.getBlockState(getPlacedPosition.relative(Direction.EAST)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_EAST, 2);
        }
        if (this.canConnectTile(world.getBlockState(getPlacedPosition.relative(Direction.WEST)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_WEST, 2);
        }
        return stateForPlacement;
    }
    @Nullable
    @Override
    public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTION_UP);
        builder.add(CONNECTION_DOWN);
        builder.add(CONNECTION_NORTH);
        builder.add(CONNECTION_SOUTH);
        builder.add(CONNECTION_WEST);
        builder.add(CONNECTION_EAST);
    }

    public List<BlockPos> getConnectedConnections(BlockPos pos, Level world) {
        List<BlockPos> connections = new ArrayList<>();
        Set<BlockPos> positionsFound = new HashSet<>();
        positionsFound.add(pos);
        positionsFound = this.getConnectedPipes(pos, world, positionsFound);
        for (BlockPos position : positionsFound) {
            if (world.getBlockState(position).getBlock() instanceof AbstractPipeConnection) {
                connections.add(position);
            }
        }
        return connections;
    }
    public abstract void doExports(BlockPos pos, Level world);
}
