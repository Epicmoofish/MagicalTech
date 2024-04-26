package org.oceanic.magical_tech.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import org.oceanic.magical_tech.MagicalTech;

public class AdvancedSouliumGeneratorScreenHandler extends AbstractSouliumGeneratorScreenHandler {
    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.

    // This is not actually unused it is just called from something that the linter doesn't see
    @SuppressWarnings("unused")
    public AdvancedSouliumGeneratorScreenHandler(int syncId, Inventory playerInventory, Container te, ContainerData data) {
        super(MagicalTech.ADVANCED_GENERATOR_MENU, syncId, playerInventory, te, data);
    }
    public AdvancedSouliumGeneratorScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        super(MagicalTech.ADVANCED_GENERATOR_MENU, syncId, playerInventory, byteBuf);
    }


}