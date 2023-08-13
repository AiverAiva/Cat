package me.weikuwu.cute.guis.config.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.audio.SoundHandler;

public class ScrollBar extends GuiButton {

    public ScrollBar(int y, int viewport, int contentHeight, int scrollOffset, int x, boolean hovered) {
        super(0, x, y, "");
        yPosition += Math.round((scrollOffset / (float) contentHeight) * viewport);
        width = 5;
        height = contentHeight > viewport ? Math.round((viewport / (float) contentHeight) * viewport) : 0;
        this.hovered = hovered;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hovered = isHovered(mouseX, mouseY);
        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, hovered ? ConfigInput.white.getRGB() : ConfigInput.transparent.getRGB());
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
    }

}
