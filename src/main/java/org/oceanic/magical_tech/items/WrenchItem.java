package org.oceanic.magical_tech.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipe;

public class WrenchItem extends Item {
    public WrenchItem(Item.Properties settings) {
        super(settings);
    }
    private Direction getWrenchDirectionFromClick(Vec3 vec, Direction dir) {
        Direction clickedDir = dir;
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;
        if (dir == Direction.UP || dir == Direction.DOWN) {
            if (x > 0.65) clickedDir = Direction.EAST;
            if (x < 0.35) clickedDir = Direction.WEST;
            if (z > 0.65) clickedDir = Direction.SOUTH;
            if (z < 0.35) clickedDir = Direction.NORTH;
            if (x > 0.85) clickedDir = Direction.EAST;
            if (x < 0.15) clickedDir = Direction.WEST;
            if (z > 0.85) clickedDir = Direction.SOUTH;
            if (z < 0.15) clickedDir = Direction.NORTH;
            if (y < 0.15) clickedDir = Direction.DOWN;
            if (y > 0.85) clickedDir = Direction.UP;
        }
        if (dir == Direction.EAST || dir == Direction.WEST) {
            if (y > 0.65) clickedDir = Direction.UP;
            if (y < 0.35) clickedDir = Direction.DOWN;
            if (z > 0.65) clickedDir = Direction.SOUTH;
            if (z < 0.35) clickedDir = Direction.NORTH;
            if (y > 0.85) clickedDir = Direction.UP;
            if (y < 0.15) clickedDir = Direction.DOWN;
            if (z > 0.85) clickedDir = Direction.SOUTH;
            if (z < 0.15) clickedDir = Direction.NORTH;
            if (x < 0.15) clickedDir = Direction.WEST;
            if (x > 0.85) clickedDir = Direction.EAST;
        }
        if (dir == Direction.NORTH || dir == Direction.SOUTH) {
            if (x > 0.65) clickedDir = Direction.EAST;
            if (x < 0.35) clickedDir = Direction.WEST;
            if (y > 0.65) clickedDir = Direction.UP;
            if (y < 0.35) clickedDir = Direction.DOWN;
            if (x > 0.85) clickedDir = Direction.EAST;
            if (x < 0.15) clickedDir = Direction.WEST;
            if (y > 0.85) clickedDir = Direction.UP;
            if (y < 0.15) clickedDir = Direction.DOWN;
            if (z < 0.15) clickedDir = Direction.NORTH;
            if (z > 0.85) clickedDir = Direction.SOUTH;
        }
        return clickedDir;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Vec3 vec = useOnContext.getClickLocation().subtract(Vec3.atLowerCornerOf(useOnContext.getClickedPos()));
        Direction dir = getWrenchDirectionFromClick(vec, useOnContext.getClickedFace());
        Level world = useOnContext.getLevel();
        BlockPos position = useOnContext.getClickedPos();
        BlockState state = world.getBlockState(position);
        Block block = state.getBlock();
        if (block instanceof AbstractPipe pipe) {
            BlockState newState = pipe.getWrenchedState(state, dir, position, world);
            world.setBlockAndUpdate(position, newState);
            BlockState state2 = world.getBlockState(position.relative(dir));
            Block block2 = state2.getBlock();
            if (block2 instanceof AbstractPipe pipe2) {
                BlockState state3 = pipe2.getWrenchedState(state2, dir.getOpposite(), position.relative(dir), world);
                world.setBlockAndUpdate(position.relative(dir), state3);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(useOnContext);
    }
}
