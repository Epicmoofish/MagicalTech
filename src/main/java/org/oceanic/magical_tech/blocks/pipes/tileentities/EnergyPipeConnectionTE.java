package org.oceanic.magical_tech.blocks.pipes.tileentities;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipe;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipeConnection;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipeConnectionTE;
import org.oceanic.magical_tech.transferrable.DSTransfer;
import org.oceanic.magical_tech.transferrable.Transferable;

import java.util.List;

public class EnergyPipeConnectionTE extends AbstractPipeConnectionTE {
    public EnergyPipeConnectionTE(BlockPos pos, BlockState state) {
        super(MagicalTech.ENERGY_PIPE_TILE_ENTITY, pos, state);
    }
}
