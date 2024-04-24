package org.oceanic.magical_tech.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.AbstractSouliumGeneratorTE;
import org.oceanic.magical_tech.menus.AbstractSouliumGeneratorScreenHandler;
import org.oceanic.magical_tech.menus.AdvancedSouliumGeneratorScreenHandler;

public class AdvancedSouliumTE extends AbstractSouliumGeneratorTE {
    public AdvancedSouliumTE(BlockPos pos, BlockState state) {
        super(MagicalTech.ADVANCED_GENERATOR_TILE_ENTITY, pos, state, 50, 500000);
    }
    @SuppressWarnings("unused")
    public static BlockEntityType<? extends AbstractSouliumGeneratorTE> getTypeOf() {
        return MagicalTech.ADVANCED_GENERATOR_TILE_ENTITY;
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable(MagicalTech.MOD_ID + ".advanced_soulium_generator.title");
    }

    @Override
    public Class<? extends AbstractSouliumGeneratorScreenHandler> getMenuClass() {
        return AdvancedSouliumGeneratorScreenHandler.class;
    }
}
