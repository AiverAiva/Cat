package me.weikuwu.cute.utils.gui;

import me.weikuwu.cute.EventManager;
import org.lwjgl.opengl.GL11;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;

public class GuiUtils {
//    private static boolean wasMouseDown = false;

    public static void drawBackground(int width, int height, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float alpha = (float) ((color >> 24) & 0xFF) / 255.0f;
        float red = (float) ((color >> 16) & 0xFF) / 255.0f;
        float green = (float) ((color >> 8) & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;

        GL11.glColor4f(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(0, height);
        glVertex2f(width, height);
        glVertex2f(width, 0);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawBorder(int width, int height, int borderWidth, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float alpha = (float) ((color >> 24) & 0xFF) / 255.0f;
        float red = (float) ((color >> 16) & 0xFF) / 255.0f;
        float green = (float) ((color >> 8) & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;

        GL11.glColor4f(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(0, borderWidth);
        glVertex2f(width, borderWidth);
        glVertex2f(width, 0);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(0, height);
        glVertex2f(borderWidth, height);
        glVertex2f(borderWidth, 0);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        glVertex2f(width - borderWidth, 0);
        glVertex2f(width - borderWidth, height);
        glVertex2f(width, height);
        glVertex2f(width, 0);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        glVertex2f(0, height - borderWidth);
        glVertex2f(0, height);
        glVertex2f(width, height);
        glVertex2f(width, height - borderWidth);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
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
