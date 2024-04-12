package org.oceanic.magical_tech.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.SouliumBlock;
import org.oceanic.magical_tech.blocks.abstractions.SouliumHolder;
import org.oceanic.magical_tech.blocks.tileentities.CrudeSouliumGeneratorTE;

import static net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper;

public class CrudeSouliumGenerator extends SouliumBlock implements EntityBlock {
    public CrudeSouliumGenerator(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrudeSouliumGeneratorTE(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return RenderShape.MODEL;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MagicalTech.CRUDE_GENERATOR_TILE_ENTITY, (world1, pos, state1, be) -> CrudeSouliumGeneratorTE.tick(world1, pos, state1, be));
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
