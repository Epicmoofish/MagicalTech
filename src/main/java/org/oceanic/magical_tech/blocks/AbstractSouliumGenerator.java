package org.oceanic.magical_tech.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.oceanic.magical_tech.blocks.abstractions.SouliumBlock;
import org.oceanic.magical_tech.blocks.abstractions.AbstractSouliumGeneratorTE;
import java.lang.reflect.InvocationTargetException;

import static net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper;

public class AbstractSouliumGenerator<Q extends AbstractSouliumGeneratorTE> extends SouliumBlock implements EntityBlock {
    private final Class<Q> clazz;
    public AbstractSouliumGenerator(FabricBlockSettings settings, Class<Q> clazz) {
        super(settings);
        this.clazz = clazz;
    }
    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof AbstractSouliumGeneratorTE te) {
            return te;
        }
        return super.getMenuProvider(blockState, level, blockPos);
    }
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            MenuProvider screenHandlerFactory = blockState.getMenuProvider(level, blockPos);
            if (screenHandlerFactory != null) {
                player.openMenu(screenHandlerFactory);
            }
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        try {
            return clazz.getConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return RenderShape.MODEL;
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        try {
            Object j = clazz.getMethod("getTypeOf").invoke(null);
            BlockEntityType<Q> typeOf = (BlockEntityType<Q>) j;
            return createTickerHelper(type, typeOf, AbstractSouliumGeneratorTE::tick);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
