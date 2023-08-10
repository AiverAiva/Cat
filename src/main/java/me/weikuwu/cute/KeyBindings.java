package me.weikuwu.cute;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding toggleAutofish;

    public static void init() {
        toggleAutofish = new KeyBinding("Auto Fish", Keyboard.KEY_M, "Cat Mod");
        ClientRegistry.registerKeyBinding(toggleAutofish);
    }
}
