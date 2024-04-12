package org.oceanic.magical_tech.blocks.pipes;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipe;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipeConnection;
import org.oceanic.magical_tech.blocks.abstractions.SouliumBlock;

import java.util.List;

public class EnergyPipe extends AbstractPipe {
    public EnergyPipe(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canConnectTile(Block block) {
        return block instanceof SouliumBlock;
    }

    @Override
    public BlockState getWrenchedState(BlockState oldState, Direction direction, BlockPos pos, Level world) {
        int current = this.getConnection(direction, oldState) ? 1 : 0;
        List<Integer> possibles = this.getAllowedConnections(direction, pos, world);
        int new_state = -1;
        for (int i : possibles) {
            if (i > current) {
                new_state = i;
                break;
            }
        }
        if (new_state == -1) {
            new_state = 0;
        }
        if (new_state == 2) {
            BlockState replaceState = MagicalTech.ENERGY_PIPE_CONNECTION.defaultBlockState();
            replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_UP, oldState.getValue(CONNECTION_UP) ? 1 : 0);
            replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_DOWN, oldState.getValue(CONNECTION_DOWN) ? 1 : 0);
            replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_NORTH, oldState.getValue(CONNECTION_NORTH) ? 1 : 0);
            replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_SOUTH, oldState.getValue(CONNECTION_SOUTH) ? 1 : 0);
            replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_EAST, oldState.getValue(CONNECTION_EAST) ? 1 : 0);
            replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_WEST, oldState.getValue(CONNECTION_WEST) ? 1 : 0);

            if (direction == Direction.UP) replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_UP, new_state);
            if (direction == Direction.DOWN) replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_DOWN, new_state);
            if (direction == Direction.NORTH) replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_NORTH, new_state);
            if (direction == Direction.SOUTH) replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_SOUTH, new_state);
            if (direction == Direction.EAST) replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_EAST, new_state);
            if (direction == Direction.WEST) replaceState = replaceState.setValue(EnergyPipeConnection.CONNECTION_WEST, new_state);
            return replaceState;

        } else {
            BlockState replaceState = oldState;
            if (direction == Direction.UP) replaceState = replaceState.setValue(CONNECTION_UP, new_state == 1);
            if (direction == Direction.DOWN) replaceState = replaceState.setValue(CONNECTION_DOWN, new_state == 1);
            if (direction == Direction.NORTH) replaceState = replaceState.setValue(CONNECTION_NORTH, new_state == 1);
            if (direction == Direction.SOUTH) replaceState = replaceState.setValue(CONNECTION_SOUTH, new_state == 1);
            if (direction == Direction.EAST) replaceState = replaceState.setValue(CONNECTION_EAST, new_state == 1);
            if (direction == Direction.WEST) replaceState = replaceState.setValue(CONNECTION_WEST, new_state == 1);
            return replaceState;
        }
    }

    @Override
    protected List<Class<? extends AbstractPipe>> getConnectable() {
        return List.of(EnergyPipe.class, EnergyPipeConnection.class);
    }
}
