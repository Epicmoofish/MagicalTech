package org.oceanic.magical_tech.compat.jade;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.tileentities.CrudeSouliumGeneratorTE;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SoulBurning implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        if (accessor.getBlockEntity() instanceof CrudeSouliumGeneratorTE holder) {
            tooltip.add(Component.translatable(MagicalTech.MOD_ID + ".soul_burntime", MagicalTech.souliumString(holder.getBurnLeft(), Screen.hasShiftDown())));
        } else {
            tooltip.add(Component.translatable(MagicalTech.MOD_ID + ".soulium_burntime_error"));
        }
    }
    @Override
    public ResourceLocation getUid() {
        return SouliumPlugin.SOUL_BURN;
    }
}