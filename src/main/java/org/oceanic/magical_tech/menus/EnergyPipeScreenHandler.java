package org.oceanic.magical_tech.menus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.pipes.tileentities.EnergyPipeConnectionTE;

public class EnergyPipeScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private int priorityUp = 0;
    private int priorityDown = 0;
    private int priorityNorth = 0;
    private int prioritySouth = 0;
    private int priorityWest = 0;
    private int priorityEast = 0;
    private int ioUp = 0;
    private int ioDown = 0;
    private int ioNorth = 0;
    private int ioSouth = 0;
    private int ioWest = 0;
    private int ioEast = 0;
    public BlockPos blockPos;
    private final Component[] directionNames = new Component[6];
    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public EnergyPipeScreenHandler(int syncId, Inventory playerInventory, EnergyPipeConnectionTE te) {
        this(syncId, playerInventory, new SimpleContainer(0));
        this.priorityUp = te.priorityUp;
        this.priorityDown = te.priorityDown;
        this.priorityNorth = te.priorityNorth;
        this.prioritySouth = te.prioritySouth;
        this.priorityWest = te.priorityWest;
        this.priorityEast = te.priorityEast;
        this.ioUp = te.exportingUp;
        this.ioDown = te.exportingDown;
        this.ioNorth = te.exportingNorth;
        this.ioSouth = te.exportingSouth;
        this.ioWest = te.exportingWest;
        this.ioEast = te.exportingEast;
        this.blockPos = te.getBlockPos();
        int i = 0;
        for (Direction dir : Direction.values()) {
            if (te.getLevel() != null) directionNames[i] = (te.getLevel().getBlockState(te.getBlockPos().relative(dir)).getBlock().getName());
            i++;
        }
    }
    public EnergyPipeScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(syncId, playerInventory, new SimpleContainer(0));
        this.priorityUp = byteBuf.readInt();
        this.priorityDown = byteBuf.readInt();
        this.priorityNorth = byteBuf.readInt();
        this.prioritySouth = byteBuf.readInt();
        this.priorityWest = byteBuf.readInt();
        this.priorityEast = byteBuf.readInt();
        this.ioUp = byteBuf.readInt();
        this.ioDown = byteBuf.readInt();
        this.ioNorth = byteBuf.readInt();
        this.ioSouth = byteBuf.readInt();
        this.ioWest = byteBuf.readInt();
        this.ioEast = byteBuf.readInt();
        this.blockPos = byteBuf.readBlockPos();
        int i = 0;
        for (Direction ignored : Direction.values()) {
            directionNames[i] = byteBuf.readComponent();
            i++;
        }
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
    public String priorityFor(Direction dir) {
        if (dir == Direction.UP) return String.valueOf(priorityUp);
        if (dir == Direction.DOWN) return String.valueOf(priorityDown);
        if (dir == Direction.NORTH) return String.valueOf(priorityNorth);
        if (dir == Direction.SOUTH) return String.valueOf(prioritySouth);
        if (dir == Direction.EAST) return String.valueOf(priorityEast);
        if (dir == Direction.WEST) return String.valueOf(priorityWest);
        return "";
    }
    public int getPriority(Direction dir) {
        if (dir == Direction.UP) return priorityUp;
        if (dir == Direction.DOWN) return priorityDown;
        if (dir == Direction.NORTH) return priorityNorth;
        if (dir == Direction.SOUTH) return prioritySouth;
        if (dir == Direction.EAST) return priorityEast;
        if (dir == Direction.WEST) return priorityWest;
        return -1;
    }
    public int getIoValue(Direction dir) {
        if (dir == Direction.UP) return ioUp;
        if (dir == Direction.DOWN) return ioDown;
        if (dir == Direction.NORTH) return ioNorth;
        if (dir == Direction.SOUTH) return ioSouth;
        if (dir == Direction.EAST) return ioEast;
        if (dir == Direction.WEST) return ioWest;
        return 0;
    }
    public void modifyIo(Direction dir) {
        if (dir == Direction.UP) ioUp = (ioUp + 1) % 4;
        if (dir == Direction.DOWN) ioDown = (ioDown + 1) % 4;
        if (dir == Direction.NORTH) ioNorth = (ioNorth + 1) % 4;
        if (dir == Direction.SOUTH) ioSouth = (ioSouth + 1) % 4;
        if (dir == Direction.EAST) ioEast = (ioEast + 1) % 4;
        if (dir == Direction.WEST) ioWest = (ioWest + 1) % 4;
    }
    public void addPriority(Direction dir, int k) {
        if (dir == Direction.UP) priorityUp += k;
        if (dir == Direction.DOWN) priorityDown += k;
        if (dir == Direction.NORTH) priorityNorth += k;
        if (dir == Direction.SOUTH) prioritySouth += k;
        if (dir == Direction.EAST) priorityEast += k;
        if (dir == Direction.WEST) priorityWest += k;
    }
    public Component getNameOf(Direction dir) {
        int i = 0;
        for (Direction dir2 : Direction.values()) {
            if (dir == dir2) return directionNames[i];
            i++;
        }
        return Component.empty();
    }
    public String ioFor(Direction dir) {
        int val = getIoValue(dir);
        if (val == 1) {
            return "O";
        }
        if (val == 2) {
            return "I";
        }
        if (val == 3) {
            return "I/O";
        }
        return "";
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return ItemStack.EMPTY;
    }
}
