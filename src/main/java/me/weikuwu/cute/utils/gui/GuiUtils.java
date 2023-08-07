package me.weikuwu.cute.utils.gui;

import me.weikuwu.cute.EventManager;

public class GuiUtils {
    private static boolean wasMouseDown = false;

    static {
        EventManager.INSTANCE.register(new GuiUtils());
    }

    public static boolean wasMouseDown() {
        return wasMouseDown;
    }

//    public static void drawRoundedRect(int x, int y, int width, int height, int cornerRadius, int color) {
//        Gui.drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color);
//        Gui.drawRect(x, y + cornerRadius, x + width, y + height - cornerRadius, color);
//
//        drawCircle(x + cornerRadius, y + cornerRadius, cornerRadius, color);
//        drawCircle(x + width - cornerRadius, y + cornerRadius, cornerRadius, color);
//        drawCircle(x + cornerRadius, y + height - cornerRadius, cornerRadius, color);
//        drawCircle(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, color);
//    }
//
//    private static void drawCircle(int x, int y, int radius, int color) {
//        int diameter = radius * 2;
//
//        for (int i = 0; i <= radius; i++) {
//            int xSegment = (int) Math.sqrt(radius * radius - i * i);
//            Gui.drawRect(x - xSegment, y + i, x + xSegment, y + i + 1, color);
//            Gui.drawRect(x - xSegment, y - i, x + xSegment, y - i + 1, color);
//        }
//    }
}
