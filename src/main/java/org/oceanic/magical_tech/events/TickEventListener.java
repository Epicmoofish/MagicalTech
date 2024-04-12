package org.oceanic.magical_tech.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.oceanic.magical_tech.blocks.abstractions.AbstractPipeConnection;
import org.oceanic.magical_tech.blocks.pipes.EnergyPipe;

import java.util.ArrayList;

public class TickEventListener {

    public static void onTick(ServerLevel world) {
    }
}
