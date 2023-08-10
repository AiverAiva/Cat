package me.weikuwu.cute.utils.client;

import net.minecraft.client.*;
import java.lang.reflect.*;
import net.minecraft.client.settings.*;

public class KeybindUtils {
    private static Minecraft mc;
    private static Method clickMouse;
    private static Method rightClickMouse;

    public static void setup() {
        try {
            KeybindUtils.clickMouse = Minecraft.class.getDeclaredMethod("clickMouse", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException e) {
            try {
                KeybindUtils.clickMouse = Minecraft.class.getDeclaredMethod("func_147116_af", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        try {
            KeybindUtils.rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException e) {
            try {
                KeybindUtils.rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException e2) {
                e.printStackTrace();
            }
        }
        if (KeybindUtils.clickMouse != null) {
            KeybindUtils.clickMouse.setAccessible(true);
        }
        if (KeybindUtils.rightClickMouse != null) {
            KeybindUtils.rightClickMouse.setAccessible(true);
        }
    }

    public static void leftClick() {
        try {
            KeybindUtils.clickMouse.invoke(Minecraft.getMinecraft(), new Object[0]);
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void rightClick() {
        try {
            KeybindUtils.rightClickMouse.invoke(Minecraft.getMinecraft(), new Object[0]);
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
    }

    public static void stopMovement() {
        KeyBinding.setKeyBindState(KeybindUtils.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(KeybindUtils.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(KeybindUtils.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(KeybindUtils.mc.gameSettings.keyBindBack.getKeyCode(), false);
    }

    static {
        KeybindUtils.mc = Minecraft.getMinecraft();
    }
}
