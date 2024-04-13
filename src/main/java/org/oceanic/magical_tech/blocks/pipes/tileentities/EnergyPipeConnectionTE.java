package org.oceanic.magical_tech.blocks.pipes.tileentities;


import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipeConnectionTE;
import org.oceanic.magical_tech.menus.EnergyPipeScreen;
import org.oceanic.magical_tech.menus.EnergyPipeScreenHandler;

import static org.oceanic.magical_tech.MagicalTech.MOD_ID;

public class EnergyPipeConnectionTE extends AbstractPipeConnectionTE {
    public EnergyPipeConnectionTE(BlockPos pos, BlockState state) {
        super(MagicalTech.ENERGY_PIPE_TILE_ENTITY, pos, state);
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable(MOD_ID, ".energy_pipe.title");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnergyPipeScreenHandler(i, inventory);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.getBlockPos());
    }
}
