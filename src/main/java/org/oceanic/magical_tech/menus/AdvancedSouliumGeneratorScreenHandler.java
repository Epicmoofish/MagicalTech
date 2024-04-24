package org.oceanic.magical_tech.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.AbstractSouliumGeneratorTE;

public class AdvancedSouliumGeneratorScreenHandler extends AbstractSouliumGeneratorScreenHandler {
    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    @SuppressWarnings("unused")
    public AdvancedSouliumGeneratorScreenHandler(int syncId, Inventory playerInventory, AbstractSouliumGeneratorTE ignored) {
        this(syncId, playerInventory, new SimpleContainer(1));

    }

    public AdvancedSouliumGeneratorScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf ignored) {
        this(syncId, playerInventory, new SimpleContainer(1));
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public AdvancedSouliumGeneratorScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(MagicalTech.ADVANCED_GENERATOR_MENU, syncId, playerInventory, inventory);
    }

}