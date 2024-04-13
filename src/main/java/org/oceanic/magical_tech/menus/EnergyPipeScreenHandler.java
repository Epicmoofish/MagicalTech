package org.oceanic.magical_tech.menus;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.client.MagicalTechClient;

public class EnergyPipeScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public EnergyPipeScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(0));
    }
    public EnergyPipeScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf pos) {
        this(syncId, playerInventory);
//        this.pos = pos;
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public EnergyPipeScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(MagicalTech.ENERGY_PIPE_MENU, syncId);
        checkContainerSize(inventory, 0);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.startOpen(playerInventory.player);

    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;

        return newStack;
    }
}
