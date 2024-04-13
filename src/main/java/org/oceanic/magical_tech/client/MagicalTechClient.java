package org.oceanic.magical_tech.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.menus.EnergyPipeScreen;

public class MagicalTechClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(MagicalTech.ENERGY_PIPE_MENU, EnergyPipeScreen::new);
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}
