package me.weikuwu.cute;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding toggleAutofish = new KeyBinding("Auto Fish", Keyboard.KEY_M, "Cat Mod");

    public static void init() {
        ClientRegistry.registerKeyBinding(toggleAutofish);
    }
}
