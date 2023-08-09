package me.weikuwu.cute.utils.gui;

import me.weikuwu.cute.EventManager;
import org.lwjgl.opengl.GL11;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;

public class GuiUtils {
//    private static boolean wasMouseDown = false;

    static {
        EventManager.INSTANCE.register(new GuiUtils());
    }

//    public static boolean wasMouseDown() {
//        return wasMouseDown;
//    }
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
