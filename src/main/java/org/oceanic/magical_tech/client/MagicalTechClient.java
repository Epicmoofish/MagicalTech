package org.oceanic.magical_tech.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.menus.EnergyPipeScreen;
import org.oceanic.magical_tech.menus.SouliumGeneratorScreen;

public class MagicalTechClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(MagicalTech.ENERGY_PIPE_MENU, EnergyPipeScreen::new);
        MenuScreens.register(MagicalTech.CRUDE_GENERATOR_MENU, SouliumGeneratorScreen::new);
        MenuScreens.register(MagicalTech.ADVANCED_GENERATOR_MENU, SouliumGeneratorScreen::new);
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}
