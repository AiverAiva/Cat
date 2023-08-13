package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.config.types.Select;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.Minecraft;

public class SelectInput extends ConfigInput {

    private final int arrowWidth = (int) Fonts.Inter.getStringWidth("<");
    private final int gap = 3;
    public Select setting;
    private boolean leftHovered = false;
    private boolean rightHovered = false;

    public SelectInput(Select setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        height = 10;
        updateText();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        leftHovered = isHovered(mouseX, mouseY, xPosition - width, xPosition - width + arrowWidth + gap, yPosition, yPosition + height);
        rightHovered = isHovered(mouseX, mouseY, xPosition - arrowWidth - gap, xPosition, yPosition, yPosition + height);

        drawHoveredStringWithInts(leftHovered, "<", xPosition - width, yPosition);
        drawStringWithInts(displayString, xPosition - width + arrowWidth + gap, yPosition);
        drawHoveredStringWithInts(rightHovered, ">", xPosition - arrowWidth, yPosition);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (rightHovered || leftHovered) {
            setting.set(setting.get(Integer.class) + (rightHovered ? 1 : -1));
            updateText();
            return true;
        }
        return false;
    }

    private boolean isHovered(int x, int y, int minX, int maxX, int minY, int maxY) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }

    public void updateText() {
        displayString = setting.options[setting.get(Integer.class)];
        width = (int) Fonts.Inter.getStringWidth(displayString) + arrowWidth * 2 + gap * 2;
    }
}
