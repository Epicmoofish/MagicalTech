package org.oceanic.magical_tech.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.blocks.abstractions.SouliumBlock;
import org.oceanic.magical_tech.blocks.tileentities.SouliumBatteryTE;

public class SouliumBattery extends SouliumBlock implements EntityBlock {
    public SouliumBattery(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SouliumBatteryTE(pos, state);
    }
    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return RenderShape.MODEL;
    }
}
