package me.weikuwu.cute.guis;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class ConfigGUI extends GuiScreen {
    private GuiTextField textField;

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
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawCenteredString(fontRendererObj, "Custom GUI", width / 2, 20, 0xFFFFFF);

        textField.drawTextBox();
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
