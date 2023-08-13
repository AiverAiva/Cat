package me.weikuwu.cute;

import net.minecraftforge.common.MinecraftForge;

public class EventManager {
    public static void registerEvents(Object... objects) {
        for (Object obj : objects) {
            MinecraftForge.EVENT_BUS.register(obj);
        }
    }
}
