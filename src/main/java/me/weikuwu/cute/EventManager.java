package me.weikuwu.cute;

import net.minecraftforge.common.MinecraftForge;

import java.util.HashSet;
import java.util.Set;

public class EventManager {
    private final Set<Object> listeners = new HashSet<>();

    public static void registerEvents(Object... objects) {
        for (Object obj : objects) {
            MinecraftForge.EVENT_BUS.register(obj);
        }
    }
}
