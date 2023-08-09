package me.weikuwu.cute.guis;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.guis.elements.Button;
import me.weikuwu.cute.utils.font.Fonts;
import me.weikuwu.cute.utils.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.weikuwu.cute.utils.gui.GuiUtils.drawBackground;
import static me.weikuwu.cute.utils.gui.GuiUtils.drawBorder;
import static net.minecraftforge.fml.client.config.GuiUtils.drawTexturedModalRect;
import static org.lwjgl.opengl.GL11.*;

public class CatRenderer {
    private static final float ASPECT_RATIO_WIDTH = 16f;
    private static final float ASPECT_RATIO_HEIGHT = 9f;

    private List<String> categories = new ArrayList<>();
    private List<String> currentModules = new ArrayList<>();
    private String selectedCategory = null;
    private String selectedModule = null;

    private Minecraft mc;
    private CatGUI catGUI;

    public CatRenderer(Minecraft mc) {
        this.mc = mc;
    }

    public void renderGui() {
        renderGui(0,0,0.0F);
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

        drawBackground(desiredWidth, desiredHeight, 0xFFC0C0C0); // Light gray color
        drawBorder(desiredWidth, desiredHeight, 2, 0xFF000000); // Black color

        // draw elements
        GL11.glPopMatrix();
    }


    private int getMaintainedWidth(int screenWidth, int screenHeight) {
        return Math.min(screenWidth, (int) (screenHeight * (ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT)));
    }

    private int getMaintainedHeight(int screenWidth, int screenHeight) {
        return Math.min(screenHeight, (int) (screenWidth * (ASPECT_RATIO_HEIGHT / ASPECT_RATIO_WIDTH)));
    }


}
