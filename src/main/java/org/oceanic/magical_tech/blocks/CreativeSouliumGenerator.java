package org.oceanic.magical_tech.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.blocks.abstractions.SouliumBlock;
import org.oceanic.magical_tech.blocks.tileentities.CreativeSouliumGeneratorTE;

public class CreativeSouliumGenerator extends SouliumBlock implements EntityBlock {
    public CreativeSouliumGenerator(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CreativeSouliumGeneratorTE(pos, state);
    }
}
