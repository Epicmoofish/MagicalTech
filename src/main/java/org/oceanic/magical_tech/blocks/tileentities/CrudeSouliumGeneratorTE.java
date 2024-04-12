package org.oceanic.magical_tech.blocks.tileentities;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.SouliumHolder;
import org.oceanic.magical_tech.soul_burning.SoulBurningMap;

import java.util.ArrayList;

public class CrudeSouliumGeneratorTE extends BlockEntity implements SouliumHolder, WorldlyContainer {
    private long soulium = 0;
    private long burnLeft = 0;
    private ItemStack stack = ItemStack.EMPTY;
    private static final long souliumPerTick = 1;
    public long getBurnLeft() {
        return burnLeft;
    }
    private static final long maxSoulium = 10000;
    public CrudeSouliumGeneratorTE(BlockPos pos, BlockState state) {
        super(MagicalTech.CRUDE_GENERATOR_TILE_ENTITY, pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putLong("soulium", soulium);
        nbt.putLong("power_left", getBurnLeft());
        CompoundTag stack_comp = nbt.getCompound("stack");
        stack.save(stack_comp);
        nbt.put("stack", stack_comp);
        super.saveAdditional(nbt);
    }

    @Override
    public long getImporting() {
        return 0;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        soulium = nbt.getLong("soulium");
        burnLeft = nbt.getLong("power_left");
        stack = ItemStack.of(nbt.getCompound("stack"));
    }

    @Override
    public long getSoulium() {
        return soulium;
    }

    @Override
    public long getMaxSoulium() {
        return maxSoulium;
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
    public static void tick(Level world, BlockPos pos, BlockState state, CrudeSouliumGeneratorTE blockEntity) {
        if (blockEntity.burnLeft == 0) {
            if (!blockEntity.getItem(0).isEmpty()) {
                long burnTime = SoulBurningMap.get(blockEntity.getItem(0).getItem());
                if (burnTime != -1) {
                    blockEntity.getItem(0).shrink(1);
                    blockEntity.burnLeft = burnTime;
                }
            }
        }
        long burned_soulium = blockEntity.burnLeft;
        if (burned_soulium > souliumPerTick) {
            burned_soulium = souliumPerTick;
        }
        if (blockEntity.soulium + burned_soulium > maxSoulium) {
            burned_soulium = maxSoulium - blockEntity.soulium;
        }
        blockEntity.soulium = blockEntity.soulium + burned_soulium;
        blockEntity.burnLeft = blockEntity.burnLeft - burned_soulium;
        CrudeSouliumGeneratorTE.setChanged(world, pos, state);
        world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    @Override
    public long removeSoulium(long amount) {
        if (amount < 0) {
            return 0;
        }
        if (amount > getSoulium()) {
            amount = getSoulium();
        }
        soulium = soulium - amount;
        return amount;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[1];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @org.jetbrains.annotations.Nullable Direction direction) {
        if (itemStack != null) {
            try {
                return SoulBurningMap.has(itemStack.getItem());
            } catch (Exception e) {
                MagicalTech.LOGGER.info("Failed to parse item " + itemStack.getItem().getDescriptionId());
            }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return stack;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        stacks.add(stack);
        return ContainerHelper.removeItem(stacks, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return removeItem(i, 1);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.stack = itemStack;
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}
