package me.weikuwu.cute;

import com.sun.jdi.InvalidTypeException;
import jdk.vm.ci.meta.ExceptionHandler;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class EventManager {
    public static final EventManager INSTANCE = new EventManager();
    private final Set<Object> listeners = new HashSet<>();

    public void register(Object object) {
        if (listeners.add(object)) {
            MinecraftForge.EVENT_BUS.register(object);
        } else {
            System.out.println("Registering an existed listener: " + object);
        }
    }
}
