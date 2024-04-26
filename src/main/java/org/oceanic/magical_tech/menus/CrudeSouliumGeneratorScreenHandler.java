package org.oceanic.magical_tech.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import org.oceanic.magical_tech.MagicalTech;

public class CrudeSouliumGeneratorScreenHandler extends AbstractSouliumGeneratorScreenHandler {
    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    @SuppressWarnings("unused")
    public CrudeSouliumGeneratorScreenHandler(int syncId, Inventory playerInventory, Container te, ContainerData data) {
        super(MagicalTech.CRUDE_GENERATOR_MENU, syncId, playerInventory, te, data);
    }
    public CrudeSouliumGeneratorScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        super(MagicalTech.CRUDE_GENERATOR_MENU, syncId, playerInventory, byteBuf);
    }
}