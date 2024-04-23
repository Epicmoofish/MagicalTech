package org.oceanic.magical_tech.soul_burning;

import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SoulBurningMap {
    private static Map<Item, Long> power_stored = new HashMap<>();
    private static Map<Item, Long> multiplier = new HashMap<>();
    public static void reset(){
        power_stored = new HashMap<>();
        multiplier = new HashMap<>();
    }
    public static void put(Item id, long value){
        power_stored.put(id, value);
    }
    public static long get(Item id){
        return power_stored.getOrDefault(id, (long) -1);
    }
    public static void putMult(Item id, long value){
        multiplier.put(id, value);
    }
    public static long getMult(Item id){
        return multiplier.getOrDefault(id, (long) 1);
    }
    public static boolean has(Item id){
        return get(id) != -1;
    }
    public static Set<Item> getKeys(){
        Set<Item> items = power_stored.keySet();
        items.removeIf((val) -> !has(val));
        return items;
    }
}
