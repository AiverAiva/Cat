package me.weikuwu.cute.guis;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.guis.elements.Button;
import me.weikuwu.cute.utils.font.Fonts;
import me.weikuwu.cute.utils.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.io.IOException;


public class CatGUI extends GuiScreen {
    private GuiTextField textField;
    private static final float ASPECT_RATIO_WIDTH = 16f;
    private static final float ASPECT_RATIO_HEIGHT = 9f;

    private CatRenderer guiRenderer;

    @Override
    public void initGui() {
        this.buttonList.add(new Button(1, this.width / 2 - 50, this.height / 2 - 20, 100, 40, "Click Me!"));
        int guiLeft = (width - 200) / 2;
        int guiTop = (height - 140) / 2;
        textField = new GuiTextField(0, fontRendererObj, guiLeft + 20, guiTop + 30, 160, 20);
        textField.setFocused(true);
        textField.setMaxStringLength(50);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        if (guiRenderer == null) {
            guiRenderer = new CatRenderer(mc);
        }
        guiRenderer.renderGui(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (textField.textboxKeyTyped(typedChar, keyCode)) {
            // what happens here?
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}

//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        drawDefaultBackground();
//        super.drawScreen(mouseX, mouseY, partialTicks);
//
//        int guiWidth = 1920;
//        int guiHeight = 1080;
//        int screenWidth = Minecraft.getMinecraft().displayWidth;
//        int screenHeight = Minecraft.getMinecraft().displayHeight;
//        double scaleFactor = 1920.0 / screenWidth;
//        int rectWidth = (int) (100 * scaleFactor);
//        int rectHeight = (int) (50 * scaleFactor);
//        int rectX = (screenWidth - rectWidth) / 2;
//        int rectY = (screenHeight - rectHeight) / 2;
//        Fonts.Inter.drawString("Custom GUI", width / 2, 20, 0xFFFFFFFF);
//        textField.drawTextBox();
//    }