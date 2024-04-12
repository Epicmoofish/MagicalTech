package org.oceanic.magical_tech.transferrable;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.oceanic.magical_tech.blocks.abstractions.SouliumHolder;

public class DSTransfer extends Transferable<Long> {
    @Override
    public Long give(BlockEntity be, Long information) {
        if (be instanceof SouliumHolder sh) {
            return sh.addSoulium(information);
        }
        return (long)0;
    }

    @Override
    public Long take(BlockEntity be, Long information) {
        if (be instanceof SouliumHolder sh) {
            long takeableSoulium = sh.getSoulium();
            return sh.removeSoulium(takeableSoulium);
        }
        return (long)0;
    }
}
