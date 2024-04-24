package org.oceanic.magical_tech.menus;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.oceanic.magical_tech.soul_burning.SoulBurningMap;

public abstract class AbstractSouliumGeneratorScreenHandler extends AbstractContainerMenu {
    private final Container inventory;

    public AbstractSouliumGeneratorScreenHandler(MenuType<AbstractSouliumGeneratorScreenHandler> generatorMenu, int syncId, Inventory playerInventory, Container inventory) {
        super(generatorMenu, syncId);
        checkContainerSize(inventory, 1);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
        //some inventories do custom logic when a player opens it.
        this.addSlot(new SoulBurningSlot(inventory, 0, 80, 35));
        int j;
        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }
        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }
    }
    private static class SoulBurningSlot extends Slot {
        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return SoulBurningMap.has(itemStack.getItem());
        }
        public SoulBurningSlot(Container container, int i, int j, int k) {
            super(container, i, j, k);
        }
    }
    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            boolean flag = false;
            if (i < 1) {
                flag = !this.moveItemStackTo(itemStack2, 1, 37, true);
            } else {
                if (SoulBurningMap.has(itemStack2.getItem())) {
                    flag = !this.moveItemStackTo(itemStack2, 0, 1, false);
                }
            }
            if (flag) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack2);
        }
        return itemStack;
    }

}
