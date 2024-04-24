package org.oceanic.magical_tech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.oceanic.magical_tech.blocks.CreativeSouliumGenerator;
import org.oceanic.magical_tech.blocks.AbstractSouliumGenerator;
import org.oceanic.magical_tech.blocks.SouliumBattery;
import org.oceanic.magical_tech.blocks.pipes.EnergyPipe;
import org.oceanic.magical_tech.blocks.pipes.EnergyPipeConnection;
import org.oceanic.magical_tech.blocks.pipes.tileentities.EnergyPipeConnectionTE;
import org.oceanic.magical_tech.blocks.tileentities.AdvancedSouliumTE;
import org.oceanic.magical_tech.blocks.tileentities.CreativeSouliumGeneratorTE;
import org.oceanic.magical_tech.blocks.tileentities.CrudeSouliumTE;
import org.oceanic.magical_tech.blocks.tileentities.SouliumBatteryTE;
import org.oceanic.magical_tech.data_structures.Mutable;
import org.oceanic.magical_tech.events.TickEventListener;
import org.oceanic.magical_tech.items.WrenchItem;
import org.oceanic.magical_tech.menus.AdvancedSouliumGeneratorScreenHandler;
import org.oceanic.magical_tech.menus.CrudeSouliumGeneratorScreenHandler;
import org.oceanic.magical_tech.menus.EnergyPipeScreenHandler;
import org.oceanic.magical_tech.menus.AbstractSouliumGeneratorScreenHandler;
import org.oceanic.magical_tech.soul_burning.SoulBurningMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MagicalTech implements ModInitializer {
    public static final String MOD_ID = "magical_tech";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Item WRENCH  = new WrenchItem(new FabricItemSettings());
    public static final Block CREATIVE_SOULIUM_GENERATOR  = new CreativeSouliumGenerator(FabricBlockSettings.create().strength(4.0f));
    public static final Block CRUDE_SOULIUM_GENERATOR = new AbstractSouliumGenerator<>(FabricBlockSettings.create().strength(4.0f), CrudeSouliumTE.class);
    public static final Block ADVANCED_SOULIUM_GENERATOR = new AbstractSouliumGenerator<>(FabricBlockSettings.create().strength(4.0f), AdvancedSouliumTE.class);
    public static final Block SOULIUM_BATTERY  = new SouliumBattery(FabricBlockSettings.create().strength(4.0f));
    public static final Block ENERGY_PIPE  = new EnergyPipe(FabricBlockSettings.create().strength(4.0f));
    public static final Block ENERGY_PIPE_CONNECTION  = new EnergyPipeConnection(FabricBlockSettings.create().strength(4.0f));
    public static final ResourceLocation ENERGY_PIPE_PACKET = new ResourceLocation(MOD_ID, "energy_pipe_packet");
    public static final MenuType<EnergyPipeScreenHandler> ENERGY_PIPE_MENU = new ExtendedScreenHandlerType<>(EnergyPipeScreenHandler::new);
    public static final MenuType<AbstractSouliumGeneratorScreenHandler> CRUDE_GENERATOR_MENU = new ExtendedScreenHandlerType<>(CrudeSouliumGeneratorScreenHandler::new);
    public static final MenuType<AbstractSouliumGeneratorScreenHandler> ADVANCED_GENERATOR_MENU = new ExtendedScreenHandlerType<>(AdvancedSouliumGeneratorScreenHandler::new);

    private static final CreativeModeTab MAGICAL_TECH = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.SOUL_CAMPFIRE))
            .title(Component.translatable("itemGroup."+ MOD_ID + ".magical_tech"))
            .displayItems((context, entries) -> {
                entries.accept(CRUDE_SOULIUM_GENERATOR);
                entries.accept(ADVANCED_SOULIUM_GENERATOR);
                entries.accept(CREATIVE_SOULIUM_GENERATOR);
                entries.accept(SOULIUM_BATTERY);
                entries.accept(ENERGY_PIPE);
                entries.accept(WRENCH);
            })
            .build();
    public static final BlockEntityType<CreativeSouliumGeneratorTE> CREATIVE_GENERATOR_TILE_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(MOD_ID, "creative_soulium_block_entity"),
            FabricBlockEntityTypeBuilder.create(CreativeSouliumGeneratorTE::new, CREATIVE_SOULIUM_GENERATOR).build()
    );
    public static final BlockEntityType<CrudeSouliumTE> CRUDE_GENERATOR_TILE_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(MOD_ID, "crude_soulium_block_entity"),
            FabricBlockEntityTypeBuilder.create(CrudeSouliumTE::new, CRUDE_SOULIUM_GENERATOR).build()
    );
    public static final BlockEntityType<AdvancedSouliumTE> ADVANCED_GENERATOR_TILE_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(MOD_ID, "advanced_soulium_block_entity"),
            FabricBlockEntityTypeBuilder.create(AdvancedSouliumTE::new, ADVANCED_SOULIUM_GENERATOR).build()
    );
    public static final BlockEntityType<EnergyPipeConnectionTE> ENERGY_PIPE_TILE_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(MOD_ID, "energy_pipe_block_entity"),
            FabricBlockEntityTypeBuilder.create(EnergyPipeConnectionTE::new, ENERGY_PIPE_CONNECTION).build()
    );
    public static final BlockEntityType<SouliumBatteryTE> SOULIUM_BATTERY_TILE_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(MOD_ID, "soulium_battery_block_entity"),
            FabricBlockEntityTypeBuilder.create(SouliumBatteryTE::new, SOULIUM_BATTERY).build()
    );
    public static JsonElement readJson(InputStream stream) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return GsonHelper.fromJson(GSON, reader, JsonElement.class);
        }
    }
    public static Direction relativeDirection(BlockPos pos1, BlockPos pos2) {
        BlockPos relative = pos1.subtract(pos2);
        if (relative.equals(new BlockPos(0, -1, 0))) {
            return Direction.UP;
        }
        if (relative.equals(new BlockPos(0, 1, 0))) {
            return Direction.DOWN;
        }
        if (relative.equals(new BlockPos(0, 0, 1))) {
            return Direction.NORTH;
        }
        if (relative.equals(new BlockPos(0, 0, -1))) {
            return Direction.SOUTH;
        }
        if (relative.equals(new BlockPos(-1, 0, 0))) {
            return Direction.EAST;
        }
        if (relative.equals(new BlockPos(1, 0, 0))) {
            return Direction.WEST;
        }
        return null;
    }
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();
    @Override
    public void onInitialize() {

        ServerTickEvents.START_WORLD_TICK.register(TickEventListener::onTick);
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(MOD_ID, "soul_burning");
            }

            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                SoulBurningMap.reset();
                LOGGER.info("Loading resource json values");
                Map<ResourceLocation, Resource> map = manager.listResources("soul_burning", path -> path.getPath().equals("soul_burning/power_stored.json"));
                for(ResourceLocation id : map.keySet()) {
                    if (manager.getResource(id).isPresent()) {
                        try (InputStream stream = manager.getResource(id).get().open()) {
                            JsonElement jsonElement = readJson(stream);
                            JsonObject obj = jsonElement.getAsJsonObject();
                            Set<String> keyset = obj.keySet();
                            for (String key : keyset) {
                                try {
                                    ResourceLocation iden = ResourceLocation.tryParse(key);
                                    Item item = BuiltInRegistries.ITEM.get(iden);
                                    SoulBurningMap.put(item, obj.get(key).getAsLong());
                                } catch (Exception e) {
                                    LOGGER.error("Key is not a valid identifier: " + key);
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.error("Error occurred while loading resource json" + id.toString(), e);
                        }
                    }
                }
                Map<ResourceLocation, Resource> map2 = manager.listResources("soul_burning", path -> path.getPath().equals("soul_burning/power_multiplier.json"));
                for(ResourceLocation id : map2.keySet()) {
                    if (manager.getResource(id).isPresent()) {
                        try (InputStream stream = manager.getResource(id).get().open()) {
                            JsonElement jsonElement = readJson(stream);
                            JsonObject obj = jsonElement.getAsJsonObject();
                            Set<String> keyset = obj.keySet();
                            for (String key : keyset) {
                                try {
                                    ResourceLocation iden = ResourceLocation.tryParse(key);
                                    Item item = BuiltInRegistries.ITEM.get(iden);
                                    SoulBurningMap.putMult(item, obj.get(key).getAsLong());
                                } catch (Exception e) {
                                    LOGGER.error("Key is not a valid identifier: " + key);
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.error("Error occurred while loading resource json" + id.toString(), e);
                        }
                    }
                }
            }
        });
        registerBlocks();
        registerItems();
        registerGroups();
        ServerPlayNetworking.registerGlobalReceiver(ENERGY_PIPE_PACKET, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int[] io = new int[6];
            int[] priority = new int[6];
            for (int i = 0; i < 6; i++) {
                priority[i] = buf.readInt();
            }
            for (int i = 0; i < 6; i++) {
                io[i] = buf.readInt();
            }
            server.execute(() -> {
                if (player.level().getBlockEntity(pos) instanceof EnergyPipeConnectionTE te) {
                    te.exportingUp = io[0];
                    te.exportingDown = io[1];
                    te.exportingNorth = io[2];
                    te.exportingSouth = io[3];
                    te.exportingEast = io[4];
                    te.exportingWest = io[5];
                    te.priorityUp = priority[0];
                    te.priorityDown = priority[1];
                    te.priorityNorth = priority[2];
                    te.prioritySouth = priority[3];
                    te.priorityEast = priority[4];
                    te.priorityWest = priority[5];
                    te.setChanged();
                }
            });
        });
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(MOD_ID, "energy_pipe"), ENERGY_PIPE_MENU);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(MOD_ID, "crude_generator"), CRUDE_GENERATOR_MENU);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(MOD_ID, "advanced_generator"), ADVANCED_GENERATOR_MENU);
    }
    public void registerGroups() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(MOD_ID, "magical_tech"), MAGICAL_TECH);
    }
    public void registerBlocks() {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "creative_soulium_generator"), CREATIVE_SOULIUM_GENERATOR);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "crude_soulium_generator"), CRUDE_SOULIUM_GENERATOR);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "advanced_soulium_generator"), ADVANCED_SOULIUM_GENERATOR);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "energy_pipe"), ENERGY_PIPE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "energy_pipe_connection"), ENERGY_PIPE_CONNECTION);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "soulium_battery"), SOULIUM_BATTERY);
    }
    public void registerItems() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "wrench"), WRENCH);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "creative_soulium_generator"), new BlockItem(CREATIVE_SOULIUM_GENERATOR, new FabricItemSettings()));
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "crude_soulium_generator"), new BlockItem(CRUDE_SOULIUM_GENERATOR, new FabricItemSettings()));
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "advanced_soulium_generator"), new BlockItem(ADVANCED_SOULIUM_GENERATOR, new FabricItemSettings()));
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "energy_pipe"), new BlockItem(ENERGY_PIPE, new FabricItemSettings()));
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "soulium_battery"), new BlockItem(SOULIUM_BATTERY, new FabricItemSettings()));
    }
    public static List<Long> balanceEnergy(List<Long> exporters, long importTotal, Mutable<Integer> starting) {
        List<Long> balanced = new ArrayList<>();
        List<Boolean> atCapacity = new ArrayList<>();
        int exportersLeft = exporters.size();
        for (Long ignored : exporters) {
            balanced.add((long)0);
            atCapacity.add(false);
        }
        while (importTotal > 0) {
            long evenSplit = importTotal / exportersLeft;
            long usedTotal = 0;
            if (evenSplit == 0) {
                for (int i = starting.get(); i < exporters.size() + starting.get(); i++) {
                    int j = i % exporters.size();
                    if (!(atCapacity.get(j))) {
                        if (usedTotal == importTotal) {
                            starting.set(j);
                            break;
                        }
                        balanced.set(j, balanced.get(j) + 1);
                        usedTotal++;
                    }
                }
            } else {
                for (int i = 0; i < exporters.size(); i++) {
                    if (!(atCapacity.get(i))) {
                        if (exporters.get(i) < evenSplit + balanced.get(i)) {
                            usedTotal += exporters.get(i);
                            balanced.set(i, exporters.get(i) + balanced.get(i));
                            atCapacity.set(i, true);
                            exportersLeft--;
                        } else {
                            usedTotal += evenSplit;
                            balanced.set(i, evenSplit + balanced.get(i));
                            if (exporters.get(i) == evenSplit) {
                                atCapacity.set(i, true);
                                exportersLeft--;
                            }
                        }
                    }
                }
            }
            importTotal -= usedTotal;
        }
        return balanced;
    }
    public static long getTotals(List<Long> longs) {
        long x = 0;
        for (long val : longs) {
            try {
                x = Math.addExact(x, val);
            } catch (ArithmeticException ex) {
                x = Long.MAX_VALUE;
            }
        }
        return x;
    }
    public static String souliumString(long soulium, boolean isSneaking) {
        String s = Long.toString(soulium);
        if (isSneaking) return s;
        // One quintillion
        if (soulium >= Long.parseLong("1000000000000000000")) {
            return s.substring(0, s.length() - 18) + "." + s.substring(s.length() - 18, s.length() - 15) + "E";
        }
        // One quadrillion
        if (soulium >= Long.parseLong("1000000000000000")) {
            return s.substring(0, s.length() - 15) + "." + s.substring(s.length() - 15, s.length() - 12) + "P";
        }
        // One trillion
        if (soulium >= Long.parseLong("1000000000000")) {
            return s.substring(0, s.length() - 12) + "." + s.substring(s.length() - 12, s.length() - 9) + "T";
        }
        // One billion
        if (soulium >= Long.parseLong("1000000000")) {
            return s.substring(0, s.length() - 9) + "." + s.substring(s.length() - 9, s.length() - 6) + "G";
        }
        // One million
        if (soulium >= Long.parseLong("1000000")) {
            return s.substring(0, s.length() - 6) + "." + s.substring(s.length() - 6, s.length() - 3) + "M";
        }
        return s;
    }
}
