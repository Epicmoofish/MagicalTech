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

public class SouliumBatteryTE extends BlockEntity implements SouliumHolder {
    private long soulium = 0;
    private static final long maxSoulium = 1000000;
    public SouliumBatteryTE(BlockPos pos, BlockState state) {
        super(MagicalTech.SOULIUM_BATTERY_TILE_ENTITY, pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putLong("soulium", soulium);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        soulium = nbt.getLong("soulium");
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

    @Override
    public long removeSoulium(long amount) {
        if (amount + getSoulium() > getMaxSoulium()) {
            amount = getMaxSoulium() - getSoulium();
        }
        if (amount > getSoulium()) {
            amount = getSoulium();
        }
        soulium = soulium - amount;
        return amount;
    }
}
