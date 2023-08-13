package me.weikuwu.cute.utils.gui;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class GuiUtils {
    // private static boolean wasMouseDown = false;

    public static void drawBackground(int width, int height, int color) {
        drawHorizontalBorder(width, height, color);

        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawBorder(int width, int height, int borderWidth, int color) {
        drawHorizontalBorder(width, borderWidth, color);

        glBegin(GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(0, height);
        glVertex2f(borderWidth, height);
        glVertex2f(borderWidth, 0);
        glEnd();

        glBegin(GL_QUADS);
        glVertex2f(width - borderWidth, 0);
        glVertex2f(width - borderWidth, height);
        glVertex2f(width, height);
        glVertex2f(width, 0);
        glEnd();

        glBegin(GL_QUADS);
        glVertex2f(0, height - borderWidth);
        glVertex2f(0, height);
        glVertex2f(width, height);
        glVertex2f(width, height - borderWidth);
        glEnd();

        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL_BLEND);
        GL11.glPopMatrix();
    }

    private static void drawHorizontalBorder(int width, int borderWidth, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float alpha = (color >> 24 & 0xFF) / 255.0f;
        float red = (color >> 16 & 0xFF) / 255.0f;
        float green = (color >> 8 & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        GL11.glColor4f(red, green, blue, alpha);

        glBegin(GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(0, borderWidth);
        glVertex2f(width, borderWidth);
        glVertex2f(width, 0);
        glEnd();
    }

/*
    public static boolean wasMouseDown() {
        return wasMouseDown;
    }
    private static void drawCircle(int x, int y, int radius, int color) {
        int diameter = radius * 2;

        for (int i = 0; i <= radius; i++) {
            int xSegment = (int) Math.sqrt(radius * radius - i * i);
            Gui.drawRect(x - xSegment, y + i, x + xSegment, y + i + 1, color);
            Gui.drawRect(x - xSegment, y - i, x + xSegment, y - i + 1, color);
        }
    }
*/
}
