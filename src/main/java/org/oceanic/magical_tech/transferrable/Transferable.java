package org.oceanic.magical_tech.transferrable;

import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class Transferable<T> {
    public abstract T give(BlockEntity be, T information);
    public abstract T take(BlockEntity be, T information);
}
