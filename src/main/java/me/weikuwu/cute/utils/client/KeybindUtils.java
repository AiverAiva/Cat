package me.weikuwu.cute.utils.client;

import me.weikuwu.cute.CatMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KeybindUtils {

    private static Method clickMouse;
    private static Method rightClickMouse;

    public static void setup() {
        setupMethod("clickMouse", "func_147116_af");
        setupMethod("rightClickMouse", "func_147121_ag");
    }

    private static void setupMethod(String methodName, String obfuscatedMethodName) {
        try {
            Method method = findMethod(Minecraft.class, methodName, obfuscatedMethodName);
            method.setAccessible(true);
            if (methodName.equals("clickMouse")) {
                clickMouse = method;
            } else if (methodName.equals("rightClickMouse")) {
                rightClickMouse = method;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static Method findMethod(Class<?> clazz, String methodName, String obfuscatedMethodName) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return clazz.getDeclaredMethod(obfuscatedMethodName);
        }
    }

    private static void invokeMethod(Method method) {
        try {
            method.invoke(CatMod.mc);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void leftClick() {
        invokeMethod(clickMouse);
    }

    public static void rightClick() {
        invokeMethod(rightClickMouse);
    }

    public static void stopMovement() {
        KeyBinding.setKeyBindState(CatMod.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(CatMod.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(CatMod.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(CatMod.mc.gameSettings.keyBindBack.getKeyCode(), false);
    }
}
