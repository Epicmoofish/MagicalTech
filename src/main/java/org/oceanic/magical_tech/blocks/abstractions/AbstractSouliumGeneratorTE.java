package org.oceanic.magical_tech.blocks.abstractions;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.menus.AbstractSouliumGeneratorScreenHandler;
import org.oceanic.magical_tech.soul_burning.SoulBurningMap;
import org.oceanic.magical_tech.util.ExtraMath;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class AbstractSouliumGeneratorTE extends BaseContainerBlockEntity implements SouliumHolder, WorldlyContainer, MenuProvider, ExtendedScreenHandlerFactory {
    private long soulium = 0;
    private long burnLeft = 0;
    private long currentMult = 1;
    private ItemStack stack = ItemStack.EMPTY;
    private final long souliumPerTick;
    public long getBurnLeft() {
        return burnLeft;
    }
    public long getCurrentMult() {
        return currentMult;
    }
    public static BlockEntityType<? extends AbstractSouliumGeneratorTE> getTypeOf() {
        return null;
    }
    private final long maxSoulium;
    public AbstractSouliumGeneratorTE(BlockEntityType<?> type, BlockPos pos, BlockState state, long souliumPerTick, long maxSoulium) {
        super(type, pos, state);
        this.souliumPerTick = souliumPerTick;
        this.maxSoulium = maxSoulium;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putLong("soulium", soulium);
        nbt.putLong("power_left", getBurnLeft());
        nbt.putLong("current_mult", currentMult);
        CompoundTag stack_comp = nbt.getCompound("stack");
        stack.save(stack_comp);
        nbt.put("stack", stack_comp);
        super.saveAdditional(nbt);
    }
    @Override
    public Component getDefaultName() {
        return getDisplayName();
    }
    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return createMenu(i, inventory, null);
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
        currentMult = nbt.getLong("current_mult");
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
    public static void tick(Level world, BlockPos pos, BlockState state, AbstractSouliumGeneratorTE blockEntity) {
        long burned_soulium = blockEntity.souliumPerTick * blockEntity.currentMult;
        if (blockEntity.soulium + burned_soulium > blockEntity.maxSoulium) {
            burned_soulium = blockEntity.maxSoulium - blockEntity.soulium;
        }
        long total_created;
        if (blockEntity.burnLeft >= burned_soulium) {
            blockEntity.burnLeft -= burned_soulium;
            total_created = burned_soulium;
            burned_soulium = 0;
        } else {
            total_created = blockEntity.burnLeft;
            burned_soulium -= blockEntity.burnLeft;
            blockEntity.burnLeft = 0;
        }
        if (blockEntity.burnLeft == 0) {
            if (!blockEntity.getItem(0).isEmpty()) {
                long soulium_in_item = SoulBurningMap.get(blockEntity.getItem(0).getItem());
                blockEntity.currentMult = SoulBurningMap.getMult(blockEntity.getItem(0).getItem());
                if (total_created > 0) {
                    burned_soulium = Math.min(blockEntity.souliumPerTick * blockEntity.currentMult - total_created, burned_soulium);
                } else {
                    burned_soulium = blockEntity.souliumPerTick * blockEntity.currentMult - total_created;
                }
                if (burned_soulium > 0) {
                    long count = blockEntity.getItem(0).getCount();
                    long totalUsed = ExtraMath.ceilDivision(burned_soulium, soulium_in_item);
                    totalUsed = Math.min(totalUsed, count);
                    long burnNum = soulium_in_item * totalUsed;
                    blockEntity.getItem(0).shrink((int) totalUsed);
                    if (burnNum >= burned_soulium) {
                        blockEntity.burnLeft = burnNum - burned_soulium;
                        total_created += burned_soulium;
                    } else {
                        total_created += burnNum;
                    }
                }
            }
        }
        blockEntity.soulium = blockEntity.soulium + total_created;
        AbstractSouliumGeneratorTE.setChanged(world, pos, state);
        world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    @Override
    public void removeSoulium(long amount) {
        if (amount < 0) {
            return;
        }
        if (amount > getSoulium()) {
            amount = getSoulium();
        }
        soulium = soulium - amount;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
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
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer serverPlayerEntity, FriendlyByteBuf byteBuf) {

    }
    @Override
    public abstract Component getDisplayName();

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        try {
            return getMenuClass().getConstructor(int.class, Inventory.class, Container.class).newInstance(i, inventory, this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public abstract Class<? extends AbstractSouliumGeneratorScreenHandler> getMenuClass();
    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
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
    public void clearContent() {
        this.stack = ItemStack.EMPTY;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}
