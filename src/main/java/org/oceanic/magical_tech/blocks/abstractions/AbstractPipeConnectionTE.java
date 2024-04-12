package org.oceanic.magical_tech.blocks.abstractions;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipeConnection;
import org.oceanic.magical_tech.blocks.abstractions.SouliumHolder;

public abstract class AbstractPipeConnectionTE extends BlockEntity {
    public int exportingUp = 1;
    public int exportingDown = 1;
    public int exportingNorth = 1;
    public int exportingSouth = 1;
    public int exportingEast = 1;
    public int exportingWest = 1;
    public int roundRobinNum = 0;
    public int roundRobinIndex = 0;
    public int priorityUp = 1;
    public int priorityDown = 1;
    public int priorityNorth = 1;
    public int prioritySouth = 1;
    public int priorityEast = 1;
    public int priorityWest = 1;
    public AbstractPipeConnectionTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public boolean isExporting(Direction dir) {
        if (dir == Direction.UP) return (exportingUp & 1) == 1;
        if (dir == Direction.DOWN) return (exportingDown & 1) == 1;
        if (dir == Direction.NORTH) return (exportingNorth & 1) == 1;
        if (dir == Direction.SOUTH) return (exportingSouth & 1) == 1;
        if (dir == Direction.EAST) return (exportingEast & 1) == 1;
        if (dir == Direction.WEST) return (exportingWest & 1) == 1;
        return false;
    }
    public boolean isImporting(Direction dir) {
        if (dir == Direction.UP) return (exportingUp & 2) == 2;
        if (dir == Direction.DOWN) return (exportingDown & 2) == 2;
        if (dir == Direction.NORTH) return (exportingNorth & 2) == 2;
        if (dir == Direction.SOUTH) return (exportingSouth & 2) == 2;
        if (dir == Direction.EAST) return (exportingEast & 2) == 2;
        if (dir == Direction.WEST) return (exportingWest & 2) == 2;
        return false;
    }
    public int getPriority(Direction dir) {
        if (dir == Direction.UP) return priorityUp;
        if (dir == Direction.DOWN) return priorityDown;
        if (dir == Direction.NORTH) return priorityNorth;
        if (dir == Direction.SOUTH) return prioritySouth;
        if (dir == Direction.EAST) return priorityEast;
        if (dir == Direction.WEST) return priorityWest;
        return 0;
    }
    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putInt("exporting_up", exportingUp);
        nbt.putInt("exporting_down", exportingDown);
        nbt.putInt("exporting_north", exportingNorth);
        nbt.putInt("exporting_south", exportingSouth);
        nbt.putInt("exporting_east", exportingEast);
        nbt.putInt("exporting_west", exportingWest);
        nbt.putInt("priority_up", priorityUp);
        nbt.putInt("priority_down", priorityDown);
        nbt.putInt("priority_north", priorityNorth);
        nbt.putInt("priority_south", prioritySouth);
        nbt.putInt("priority_east", priorityEast);
        nbt.putInt("priority_west", priorityWest);
        nbt.putInt("round_robin_num", roundRobinNum);
        nbt.putInt("round_robin_index", roundRobinIndex);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        exportingUp = nbt.getInt("exporting_up");
        exportingDown = nbt.getInt("exporting_down");
        exportingNorth = nbt.getInt("exporting_north");
        exportingSouth = nbt.getInt("exporting_south");
        exportingEast = nbt.getInt("exporting_east");
        exportingWest = nbt.getInt("exporting_west");
        priorityUp = nbt.getInt("priority_up");
        priorityDown = nbt.getInt("priority_down");
        priorityNorth = nbt.getInt("priority_north");
        prioritySouth = nbt.getInt("priority_south");
        priorityEast = nbt.getInt("priority_east");
        priorityWest = nbt.getInt("priority_west");
        roundRobinNum = nbt.getInt("round_robin_num");
        roundRobinIndex = nbt.getInt("round_robin_index");
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    public static void tick(Level world, BlockPos pos, BlockState state, AbstractPipeConnectionTE blockEntity) {
        if (state.getBlock() instanceof AbstractPipeConnection conn) {
            conn.doExports(pos, world);
        }
    }
}
