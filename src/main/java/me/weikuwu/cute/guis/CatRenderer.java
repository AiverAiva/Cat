package me.weikuwu.cute.guis;

import me.weikuwu.cute.CatMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;

public class CatRenderer {
    private static final float ASPECT_RATIO_WIDTH = 16f;
    private static final float ASPECT_RATIO_HEIGHT = 9f;

    private Minecraft mc;

    public CatRenderer(Minecraft mc) {
        this.mc = mc;
    }

    public void renderGui(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(CatMod.mc);
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();

        int desiredWidth = getMaintainedWidth(screenWidth, screenHeight);
        int desiredHeight = getMaintainedHeight(screenWidth, screenHeight);

        int offsetX = (screenWidth - desiredWidth) / 2;
        int offsetY = (screenHeight - desiredHeight) / 2;

        GL11.glPushMatrix();
        GL11.glTranslatef(offsetX, offsetY, 0.0f);

//        drawBackground(desiredWidth, desiredHeight, 0xFFC0C0C0); // Light gray color
//        drawBorder(desiredWidth, desiredHeight, 2, 0xFF000000); // Black color

        // draw elements
        GL11.glPopMatrix();
    }

    private int getMaintainedWidth(int screenWidth, int screenHeight) {
        return Math.min(screenWidth, (int) (screenHeight * (ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT)));
    }

    private int getMaintainedHeight(int screenWidth, int screenHeight) {
        return Math.min(screenHeight, (int) (screenWidth * (ASPECT_RATIO_HEIGHT / ASPECT_RATIO_WIDTH)));
    }

    private void drawBackground(int width, int height, int color) {
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

    private void drawBorder(int width, int height, int borderWidth, int color) {
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


}
