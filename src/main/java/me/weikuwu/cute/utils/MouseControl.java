package me.weikuwu.cute.utils;

import me.weikuwu.cute.CatMod;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

public class MouseControl {
    public static boolean isUngrabbed;
    private static boolean doesGameWantUngrab;
    private static MouseHelper oldMouseHelper;

    public static void ungrabMouse() {
        if (!CatMod.mc.inGameHasFocus || isUngrabbed) return;

        if (oldMouseHelper == null) oldMouseHelper = CatMod.mc.mouseHelper;

        CatMod.mc.gameSettings.pauseOnLostFocus = false;
        doesGameWantUngrab = !Mouse.isGrabbed();
        oldMouseHelper.ungrabMouseCursor();
        CatMod.mc.inGameHasFocus = true;
        CatMod.mc.mouseHelper = new MouseHelper() {
            public void func_74374_c() {
            }

            public void func_74372_a() {
                doesGameWantUngrab = false;
            }

            public void func_74373_b() {
                doesGameWantUngrab = true;
            }
        };
        isUngrabbed = true;
    }

    public static void regrabMouse() {
        if (!isUngrabbed) return;

        isUngrabbed = false;
        CatMod.mc.mouseHelper = oldMouseHelper;
        if (!doesGameWantUngrab) CatMod.mc.mouseHelper.grabMouseCursor();
        oldMouseHelper = null;
    }
}
