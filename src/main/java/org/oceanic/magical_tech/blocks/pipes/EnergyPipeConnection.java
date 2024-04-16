package org.oceanic.magical_tech.blocks.pipes;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.*;
import org.oceanic.magical_tech.blocks.pipes.tileentities.EnergyPipeConnectionTE;
import org.oceanic.magical_tech.data_structures.Mutable;
import org.oceanic.magical_tech.data_structures.PriorityHolders;
import org.oceanic.magical_tech.items.WrenchItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper;

public class EnergyPipeConnection extends AbstractPipeConnection {
    public EnergyPipeConnection(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canConnectTile(Block block) {
        return block instanceof SouliumBlock;
    }

    @Override
    public BlockState getWrenchedState(BlockState oldState, Direction direction, BlockPos pos, Level world) {
        int current = this.getConnectionVal(direction, oldState);
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
        BlockState replaceState = oldState;
        if (direction == Direction.UP) replaceState = replaceState.setValue(CONNECTION_UP, new_state);
        if (direction == Direction.DOWN) replaceState = replaceState.setValue(CONNECTION_DOWN, new_state);
        if (direction == Direction.NORTH) replaceState = replaceState.setValue(CONNECTION_NORTH, new_state);
        if (direction == Direction.SOUTH) replaceState = replaceState.setValue(CONNECTION_SOUTH, new_state);
        if (direction == Direction.EAST) replaceState = replaceState.setValue(CONNECTION_EAST, new_state);
        if (direction == Direction.WEST) replaceState = replaceState.setValue(CONNECTION_WEST, new_state);
        return decayState(replaceState);
    }
    @Override
    public BlockState decayState(BlockState state) {
        int up = state.getValue(CONNECTION_UP);
        int down = state.getValue(CONNECTION_DOWN);
        int west = state.getValue(CONNECTION_WEST);
        int east = state.getValue(CONNECTION_EAST);
        int south = state.getValue(CONNECTION_SOUTH);
        int north = state.getValue(CONNECTION_NORTH);
        if (up == 2 || down == 2 || west == 2 || east == 2 || south == 2 || north == 2) return state;

        BlockState replaceState = MagicalTech.ENERGY_PIPE.defaultBlockState();
        replaceState = replaceState.setValue(EnergyPipe.CONNECTION_UP, state.getValue(CONNECTION_UP) == 1);
        replaceState = replaceState.setValue(EnergyPipe.CONNECTION_DOWN, state.getValue(CONNECTION_DOWN) == 1);
        replaceState = replaceState.setValue(EnergyPipe.CONNECTION_NORTH, state.getValue(CONNECTION_NORTH) == 1);
        replaceState = replaceState.setValue(EnergyPipe.CONNECTION_SOUTH, state.getValue(CONNECTION_SOUTH) == 1);
        replaceState = replaceState.setValue(EnergyPipe.CONNECTION_EAST, state.getValue(CONNECTION_EAST) == 1);
        replaceState = replaceState.setValue(EnergyPipe.CONNECTION_WEST, state.getValue(CONNECTION_WEST) == 1);
        return replaceState;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MagicalTech.ENERGY_PIPE_TILE_ENTITY, EnergyPipeConnectionTE::tick);
    }
    private void populateExportersAndImporters(Level world, List<BlockPos> positions, List<PriorityHolders<SouliumHolder>> exporters, List<PriorityHolders<SouliumHolder>> importers) {
        for (BlockPos posi : positions) {
            BlockState state = world.getBlockState(posi);
            BlockEntity be = world.getBlockEntity(posi);
            if (state.getBlock() instanceof AbstractPipeConnection connection) {
                if (be instanceof AbstractPipeConnectionTE connectionTE) {
                    for (Direction dir : Direction.values()) {
                        int val = connection.getConnectionVal(dir, state);
                        if (val == 2) {
                            BlockPos relative = posi.relative(dir);
                            BlockEntity entity = world.getBlockEntity(relative);
                            if (entity instanceof SouliumHolder holder) {
                                if (connectionTE.isExporting(dir)) {
                                    exporters.add(new PriorityHolders<>(holder, connectionTE.getPriority(dir)));
                                }
                                if (connectionTE.isImporting(dir)) {
                                    importers.add(new PriorityHolders<>(holder, connectionTE.getPriority(dir)));
                                }
                            }
                        }
                    }
                }
            }
        }
        exporters.sort(Comparator.comparingInt(x -> -x.priority));
        importers.sort(Comparator.comparingInt(x -> -x.priority));
    }
    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof EnergyPipeConnectionTE te) {
            return te;
        }
        return super.getMenuProvider(blockState, level, blockPos);
    }
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!(player.getItemInHand(interactionHand).getItem() instanceof WrenchItem)) {
            if (!level.isClientSide) {
                MenuProvider screenHandlerFactory = blockState.getMenuProvider(level, blockPos);
                if (screenHandlerFactory != null) {
                    player.openMenu(screenHandlerFactory);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void doExports(BlockPos pos, Level world) {
        List<BlockPos> positions = this.getConnectedConnections(pos, world);
        positions.sort((pos2, pos3) -> {
            if (pos2.getX() > pos3.getX()) return -1;
            if (pos2.getX() < pos3.getX()) return 1;
            if (pos2.getY() > pos3.getY()) return -1;
            if (pos2.getY() < pos3.getY()) return 1;
            return Integer.compare(pos3.getZ(), pos2.getZ());
        });
        if (positions.get(0) != pos) return;
        List<PriorityHolders<SouliumHolder>> exporters = new ArrayList<>();
        List<PriorityHolders<SouliumHolder>> importers = new ArrayList<>();

        populateExportersAndImporters(world, positions, exporters, importers);

        List<List<Long>> exporting = new ArrayList<>();
        List<List<Long>> importing = new ArrayList<>();

        splitExportingAndImporting(exporters, importers, exporting, importing);

        List<List<Long>> takeExporters = new ArrayList<>();
        List<List<Long>> takeImporters = new ArrayList<>();
        int roundRobinIndex = 0;
        int roundRobinValue = 0;
        if (world.getBlockEntity(pos) instanceof AbstractPipeConnectionTE be) {
            roundRobinValue = be.roundRobinNum;
            roundRobinIndex = be.roundRobinIndex;
        }
        Mutable<Integer> currentRoundRobin = new Mutable<>(0);

        getSplits(pos, currentRoundRobin, exporting, importing, takeExporters, takeImporters, roundRobinValue, roundRobinIndex, world);

        List<Long> actualExporters = new ArrayList<>();
        List<Long> actualImporters = new ArrayList<>();

        recombine(actualExporters, actualImporters, takeExporters, takeImporters);
        handleSouliumExchange(exporters, actualExporters, importers, actualImporters);
    }

    private void handleSouliumExchange(List<PriorityHolders<SouliumHolder>> exporters, List<Long> actualExporters, List<PriorityHolders<SouliumHolder>> importers, List<Long> actualImporters) {
        for (int i2 = 0; i2 < actualExporters.size(); i2++) {
            exporters.get(i2).holder.removeSoulium(actualExporters.get(i2));
        }
        for (int i2 = 0; i2 < actualImporters.size(); i2++) {
            importers.get(i2).holder.addSoulium(actualImporters.get(i2));
        }
    }

    private void recombine(List<Long> actualExporters, List<Long> actualImporters, List<List<Long>> takeExporters, List<List<Long>> takeImporters) {
        for (List<Long> lst : takeExporters) {
            actualExporters.addAll(lst);
        }
        for (List<Long> lst : takeImporters) {
            actualImporters.addAll(lst);
        }
    }

    private void getSplits(BlockPos pos, Mutable<Integer> currentRoundRobin, List<List<Long>> exporting, List<List<Long>> importing, List<List<Long>> takeExporters, List<List<Long>> takeImporters, int roundRobinValue, int roundRobinIndex, Level world) {
        int i = 0;
        int j = 0;
        if (roundRobinIndex == 0) {
            currentRoundRobin = new Mutable<>(roundRobinValue);
        }
        while (i < exporting.size() && j < importing.size()) {
            List<Long> exporter = exporting.get(i);
            List<Long> importer = importing.get(j);
            long exporterTotal = MagicalTech.getTotals(exporter);
            long importerTotal = MagicalTech.getTotals(importer);
            List<Long> takeExporting;
            List<Long> takeImporting;
            if (exporterTotal >= importerTotal) {
                takeExporting = MagicalTech.balanceEnergy(exporter, importerTotal, new Mutable<>(0));
                takeImporting = importer;
                combine(takeExporters, takeExporting, i);
                combine(takeImporters, takeImporting, j);
                currentRoundRobin.set(0);
                j++;
                if (j == roundRobinIndex) {
                    currentRoundRobin.set(roundRobinValue);
                }
            } else {
                takeExporting = exporter;
                takeImporting = MagicalTech.balanceEnergy(importer, exporterTotal, currentRoundRobin);
                combine(takeExporters, takeExporting, i);
                combine(takeImporters, takeImporting, j);
                i++;
            }
        }
        if (world.getBlockEntity(pos) instanceof AbstractPipeConnectionTE be) {
            be.roundRobinIndex = j;
            be.roundRobinNum = currentRoundRobin.get();
        }
    }
    private void splitExportingAndImporting(List<PriorityHolders<SouliumHolder>> exporters, List<PriorityHolders<SouliumHolder>> importers, List<List<Long>> exporting, List<List<Long>> importing) {
        boolean first = true;
        int last = 0;
        for (PriorityHolders<SouliumHolder> holder : exporters) {
            if (first || last != holder.priority) {
                exporting.add(new ArrayList<>());
                last = holder.priority;
            }
            exporting.get(exporting.size() - 1).add(holder.holder.getExporting());
            first = false;
        }
        first = true;
        for (PriorityHolders<SouliumHolder> holder : importers) {
            if (first || last != holder.priority) {
                importing.add(new ArrayList<>());
                last = holder.priority;
            }
            importing.get(importing.size() - 1).add(holder.holder.getImporting());
            first = false;
        }
    }

    private void combine(List<List<Long>> original, List<Long> added, int position) {
        if (position>= original.size()) {
            original.add(added);
        } else {
            for (int i = 0; i < added.size(); i++) {
                original.get(position).set(i, original.get(position).get(i) + added.get(i));
            }
        }
    }

    @Override
    protected List<Class<? extends AbstractPipe>> getConnectable() {
        return List.of(EnergyPipeConnection.class, EnergyPipe.class);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnergyPipeConnectionTE(blockPos, blockState);
    }
}
