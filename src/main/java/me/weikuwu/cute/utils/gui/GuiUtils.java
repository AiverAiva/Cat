package me.weikuwu.cute.utils.gui;

import net.minecraft.client.gui.Gui;

public class GuiUtils {
    public static void drawRoundedRect(int x, int y, int width, int height, int cornerRadius, int color) {
        Gui.drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color);
        Gui.drawRect(x, y + cornerRadius, x + width, y + height - cornerRadius, color);

        drawCircle(x + cornerRadius, y + cornerRadius, cornerRadius, color);
        drawCircle(x + width - cornerRadius, y + cornerRadius, cornerRadius, color);
        drawCircle(x + cornerRadius, y + height - cornerRadius, cornerRadius, color);
        drawCircle(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, color);
    }

    private static void drawCircle(int x, int y, int radius, int color) {
        int diameter = radius * 2;

        for (int i = 0; i <= radius; i++) {
            int xSegment = (int) Math.sqrt(radius * radius - i * i);
            Gui.drawRect(x - xSegment, y + i, x + xSegment, y + i + 1, color);
            Gui.drawRect(x - xSegment, y - i, x + xSegment, y - i + 1, color);
        }
    }
}
