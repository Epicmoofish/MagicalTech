package org.oceanic.magical_tech.blocks.abstractions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
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
import java.util.List;
import java.util.Set;

public abstract class AbstractPipe extends Block {
    public static final BooleanProperty CONNECTION_UP = BooleanProperty.create("connection_up");
    public static final BooleanProperty CONNECTION_DOWN = BooleanProperty.create("connection_down");
    public static final BooleanProperty CONNECTION_NORTH = BooleanProperty.create("connection_north");
    public static final BooleanProperty CONNECTION_SOUTH = BooleanProperty.create("connection_south");
    public static final BooleanProperty CONNECTION_WEST = BooleanProperty.create("connection_west");
    public static final BooleanProperty CONNECTION_EAST = BooleanProperty.create("connection_east");
    public AbstractPipe(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(CONNECTION_UP, false)
                .setValue(CONNECTION_DOWN, false)
                .setValue(CONNECTION_NORTH, false)
                .setValue(CONNECTION_SOUTH, false)
                .setValue(CONNECTION_WEST, false)
                .setValue(CONNECTION_EAST, false)
        );
    }
    public AbstractPipe(Properties properties, boolean bool) {
        super(properties);
        if (bool) {
            registerDefaultState(defaultBlockState()
                    .setValue(CONNECTION_UP, false)
                    .setValue(CONNECTION_DOWN, false)
                    .setValue(CONNECTION_NORTH, false)
                    .setValue(CONNECTION_SOUTH, false)
                    .setValue(CONNECTION_WEST, false)
                    .setValue(CONNECTION_EAST, false)
            );
        }
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos getPlacedPosition = blockPlaceContext.getClickedPos();
        Level world = blockPlaceContext.getLevel();
        BlockState stateForPlacement = this.defaultBlockState();
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.UP)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_UP, true);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.DOWN)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_DOWN, true);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.NORTH)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_NORTH, true);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.SOUTH)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_SOUTH, true);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.EAST)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_EAST, true);
        }
        if (this.canConnect(world.getBlockState(getPlacedPosition.relative(Direction.WEST)))) {
            stateForPlacement = stateForPlacement.setValue(CONNECTION_WEST, true);
        }
        return stateForPlacement;
    }
    public boolean getConnection(Direction dir, BlockState state) {
        boolean a = false;
        if (dir == Direction.UP) a = state.getValue(CONNECTION_UP);
        if (dir == Direction.DOWN) a = state.getValue(CONNECTION_DOWN);
        if (dir == Direction.NORTH) a = state.getValue(CONNECTION_NORTH);
        if (dir == Direction.SOUTH) a = state.getValue(CONNECTION_SOUTH);
        if (dir == Direction.EAST) a = state.getValue(CONNECTION_EAST);
        if (dir == Direction.WEST) a = state.getValue(CONNECTION_WEST);
        return a;
    }
    public List<Integer> getAllowedConnections(Direction dir, BlockPos pos, Level world) {
        Block connectable = world.getBlockState(pos.relative(dir)).getBlock();
        List<Integer> a = new ArrayList<>();
        a.add(0);
        if (this.canConnect(connectable)) a.add(1);
        if (this.canConnectTile(connectable)) a.add(2);
        return a;
    }
    public boolean canConnectTile(BlockState state) {
        return canConnectTile(state.getBlock());
    }
    public abstract boolean canConnectTile(Block block);
    protected Set<BlockPos> getConnectedPipes(BlockPos pos, Level world, Set<BlockPos> found) {
        BlockState state = world.getBlockState(pos);
        for (Direction dir : this.directions(state)) {
            BlockPos possibleConnection = pos.relative(dir);
            Block block = world.getBlockState(possibleConnection).getBlock();
            if (this.canConnect(block) && block instanceof AbstractPipe pipe) {
                if (!found.contains(possibleConnection)) {
                    found.add(possibleConnection);
                    found.addAll(pipe.getConnectedPipes(possibleConnection, world, found));
                }
            }
        }
        return found;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTION_UP);
        builder.add(CONNECTION_DOWN);
        builder.add(CONNECTION_NORTH);
        builder.add(CONNECTION_SOUTH);
        builder.add(CONNECTION_WEST);
        builder.add(CONNECTION_EAST);
    }
    protected boolean canConnect(Block block) {
        for (Class<? extends AbstractPipe> clazz : getConnectable()) {
            if (clazz.isInstance(block)) {
                return true;
            }
        }
        return false;
    }
    protected boolean canConnect(BlockState state) {
        Block block = state.getBlock();
        return canConnect(block);
    }

    public abstract BlockState getWrenchedState(BlockState oldState, Direction direction, BlockPos pos, Level world);

    protected abstract List<Class<? extends AbstractPipe>> getConnectable();
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    public void neighborChangedSuperer(BlockState blockState, Level level, BlockPos blockPos, Block blockOld, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, blockOld, blockPos2, bl);
    }
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block blockOld, BlockPos blockPos2, boolean bl) {
        Direction dir = MagicalTech.relativeDirection(blockPos, blockPos2);
        Block block = level.getBlockState(blockPos2).getBlock();
        if (!(block instanceof AbstractPipe && blockOld instanceof AbstractPipe)) {
            if (dir != null) {
                if (dir == Direction.UP) {
                    boolean current = blockState.getValue(CONNECTION_UP);
                    boolean updated = this.canConnect(block);
                    if (current != updated) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CONNECTION_UP, updated));
                    }
                }
                if (dir == Direction.DOWN) {
                    boolean current = blockState.getValue(CONNECTION_DOWN);
                    boolean updated = this.canConnect(block);
                    if (current != updated) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CONNECTION_DOWN, updated));
                    }
                }
                if (dir == Direction.NORTH) {
                    boolean current = blockState.getValue(CONNECTION_NORTH);
                    boolean updated = this.canConnect(block);
                    if (current != updated) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CONNECTION_NORTH, updated));
                    }
                }
                if (dir == Direction.SOUTH) {
                    boolean current = blockState.getValue(CONNECTION_SOUTH);
                    boolean updated = this.canConnect(block);
                    if (current != updated) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CONNECTION_SOUTH, updated));
                    }
                }
                if (dir == Direction.EAST) {
                    boolean current = blockState.getValue(CONNECTION_EAST);
                    boolean updated = this.canConnect(block);
                    if (current != updated) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CONNECTION_EAST, updated));
                    }
                }
                if (dir == Direction.WEST) {
                    boolean current = blockState.getValue(CONNECTION_WEST);
                    boolean updated = this.canConnect(block);
                    if (current != updated) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CONNECTION_WEST, updated));
                    }
                }
            }
        }
        super.neighborChanged(blockState, level, blockPos, blockOld, blockPos2, bl);
    }


    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Vec3 start = new Vec3(0.35, 0.35, 0.35);
        Vec3 end = new Vec3(0.65, 0.65, 0.65);
        VoxelShape shape = Shapes.create(new AABB(start, end));
        if (blockState.getValue(CONNECTION_UP)) {
            Vec3 start2 = new Vec3(0.35, 0.65, 0.35);
            Vec3 end2 = new Vec3(0.65, 1, 0.65);
            VoxelShape addition = Shapes.create(new AABB(start2, end2));
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_DOWN)) {
            Vec3 start2 = new Vec3(0.35, 0, 0.35);
            Vec3 end2 = new Vec3(0.65, 0.35, 0.65);
            VoxelShape addition = Shapes.create(new AABB(start2, end2));
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_NORTH)) {
            Vec3 start2 = new Vec3(0.35, 0.35, 0);
            Vec3 end2 = new Vec3(0.65, 0.65, 0.35);
            VoxelShape addition = Shapes.create(new AABB(start2, end2));
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_SOUTH)) {
            Vec3 start2 = new Vec3(0.35, 0.35, 0.65);
            Vec3 end2 = new Vec3(0.65, 0.65, 1);
            VoxelShape addition = Shapes.create(new AABB(start2, end2));
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_EAST)) {
            Vec3 start2 = new Vec3(0.65, 0.35, 0.35);
            Vec3 end2 = new Vec3(1, 0.65, 0.65);
            VoxelShape addition = Shapes.create(new AABB(start2, end2));
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        if (blockState.getValue(CONNECTION_WEST)) {
            Vec3 start2 = new Vec3(0, 0.35, 0.35);
            Vec3 end2 = new Vec3(0.35, 0.65, 0.65);
            VoxelShape addition = Shapes.create(new AABB(start2, end2));
            shape = Shapes.join(shape, addition, BooleanOp.OR);
        }
        return shape;
    }
    protected List<Direction> directions(BlockState state) {
        List<Direction> arr = new ArrayList<>();
        if (state.getValue(CONNECTION_UP)) {
            arr.add(Direction.UP);
        }
        if (state.getValue(CONNECTION_DOWN)) {
            arr.add(Direction.DOWN);
        }
        if (state.getValue(CONNECTION_NORTH)) {
            arr.add(Direction.NORTH);
        }
        if (state.getValue(CONNECTION_SOUTH)) {
            arr.add(Direction.SOUTH);
        }
        if (state.getValue(CONNECTION_EAST)) {
            arr.add(Direction.EAST);
        }
        if (state.getValue(CONNECTION_WEST)) {
            arr.add(Direction.WEST);
        }
        return arr;
    }
}
