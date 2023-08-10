package me.weikuwu.cute.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

public class MouseControl {
    private static boolean doesGameWantUngrab;
    public static boolean isUngrabbed;
    private static Minecraft mc;
    private static MouseHelper oldMouseHelper;

    public static void ungrabMouse() {
        if (!MouseControl.mc.inGameHasFocus || MouseControl.isUngrabbed) {
            return;
        }
        if (MouseControl.oldMouseHelper == null) {
            MouseControl.oldMouseHelper = MouseControl.mc.mouseHelper;
        }
        MouseControl.mc.gameSettings.pauseOnLostFocus = false;
        MouseControl.doesGameWantUngrab = !Mouse.isGrabbed();
        MouseControl.oldMouseHelper.ungrabMouseCursor();
        MouseControl.mc.inGameHasFocus = true;
        MouseControl.mc.mouseHelper = new MouseHelper() {
            public void func_74374_c() {
            }

            public void func_74372_a() {
                MouseControl.doesGameWantUngrab = false;
            }

            public void func_74373_b() {
                MouseControl.doesGameWantUngrab = true;
            }
        };
        MouseControl.isUngrabbed = true;
    }

    public static void regrabMouse() {
        if (!MouseControl.isUngrabbed) {
            return;
        }
        MouseControl.isUngrabbed = false;
        MouseControl.mc.mouseHelper = MouseControl.oldMouseHelper;
        if (!MouseControl.doesGameWantUngrab) {
            MouseControl.mc.mouseHelper.grabMouseCursor();
        }
        MouseControl.oldMouseHelper = null;
    }
}
