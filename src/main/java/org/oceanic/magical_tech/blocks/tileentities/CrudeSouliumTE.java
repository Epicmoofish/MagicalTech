package org.oceanic.magical_tech.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.AbstractSouliumGeneratorTE;

public class CrudeSouliumTE extends AbstractSouliumGeneratorTE {
    public CrudeSouliumTE(BlockPos pos, BlockState state) {
        super(MagicalTech.CRUDE_GENERATOR_TILE_ENTITY, pos, state, 10);
    }
    @SuppressWarnings("unused")
    public static BlockEntityType<? extends AbstractSouliumGeneratorTE> getTypeOf() {
        return MagicalTech.CRUDE_GENERATOR_TILE_ENTITY;
    }
}
