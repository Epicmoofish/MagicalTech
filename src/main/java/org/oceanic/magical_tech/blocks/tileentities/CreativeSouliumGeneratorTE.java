package org.oceanic.magical_tech.blocks.tileentities;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.SouliumHolder;

public class CreativeSouliumGeneratorTE extends BlockEntity implements SouliumHolder {

    public CreativeSouliumGeneratorTE(BlockPos pos, BlockState state) {
        super(MagicalTech.CREATIVE_GENERATOR_TILE_ENTITY, pos, state);
    }

    @Override
    public long getSoulium() {
        return Long.MAX_VALUE;
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
    public long getMaxSoulium() {
        return Long.MAX_VALUE;
    }

    @Override
    public void removeSoulium(long amount) {
    }
}
