package org.oceanic.magical_tech.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.blocks.abstractions.SouliumBlock;
import org.oceanic.magical_tech.blocks.abstractions.SouliumHolder;
import org.oceanic.magical_tech.blocks.tileentities.CreativeSouliumGeneratorTE;

import net.minecraft.world.level.Level;

public class CreativeSouliumGenerator extends SouliumBlock implements EntityBlock {
    public CreativeSouliumGenerator(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CreativeSouliumGeneratorTE(pos, state);
    }

    @Override
    public SouliumHolder getSouliumHolder(BlockPos pos, Level world) {
        BlockEntity ent = world.getBlockEntity(pos);
        if (ent instanceof SouliumHolder) {
            return (SouliumHolder) ent;
        }
        return null;
    }
}
