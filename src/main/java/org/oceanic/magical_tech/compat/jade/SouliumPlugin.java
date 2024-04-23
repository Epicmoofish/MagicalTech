package org.oceanic.magical_tech.compat.jade;

import net.minecraft.resources.ResourceLocation;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.blocks.AbstractSouliumGenerator;
import org.oceanic.magical_tech.blocks.abstractions.SouliumBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class SouliumPlugin implements IWailaPlugin {

    public static final ResourceLocation SOULIUM = new ResourceLocation(MagicalTech.MOD_ID, "soulium_plugin");
    public static final ResourceLocation SOUL_BURN = new ResourceLocation(MagicalTech.MOD_ID, "soulium_burning");

    @Override
    public void register(IWailaCommonRegistration registration) {
        //TODO register data providers
    }
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SouliumProvider.INSTANCE, SouliumBlock.class);
        registration.registerBlockComponent(SoulBurning.INSTANCE, AbstractSouliumGenerator.class);
    }
}
