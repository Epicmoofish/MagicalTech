package org.oceanic.magical_tech.compat.jade;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.abstractions.SouliumHolder;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ProgressStyle;

public enum SouliumProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        if (accessor.getBlockEntity() instanceof SouliumHolder holder) {
            float percent = ((float) holder.getSoulium()) / ((float) holder.getMaxSoulium());
            float ratio = percent;
            percent = percent * 1000;
            percent = Math.round(percent);
            percent = percent / 10;
            IElementHelper helper = IElementHelper.get();
            ProgressStyle style = helper.progressStyle().color(0xFF00DDDD, 0xFF008888);
            tooltip.add(helper.progress(ratio, Component.translatable(MagicalTech.MOD_ID + ".soulium_provider",percent, MagicalTech.souliumString(holder.getSoulium(), Screen.hasShiftDown()), MagicalTech.souliumString(holder.getMaxSoulium(), Screen.hasShiftDown())), style, BoxStyle.getNestedBox(), true));
        } else {
            tooltip.add(Component.translatable(MagicalTech.MOD_ID + ".soulium_provider_error"));
        }
    }
    @Override
    public ResourceLocation getUid() {
        return SouliumPlugin.SOULIUM;
    }
}
