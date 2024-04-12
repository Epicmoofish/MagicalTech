package org.oceanic.magical_tech.blocks.abstractions;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class SouliumBlock extends Block {
    public SouliumBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    public abstract SouliumHolder getSouliumHolder(BlockPos pos, Level world);
}