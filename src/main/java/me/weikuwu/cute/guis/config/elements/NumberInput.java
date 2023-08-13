package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.config.types.Number;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.Minecraft;

public class NumberInput extends ConfigInput {

    private final int minusWidth = (int) Fonts.Inter.getStringWidth("-");
    private final int plusWidth = (int) Fonts.Inter.getStringWidth("+");
    private final int gap = 3;
    public Number setting;
    private boolean minusHovered = false;
    private boolean plusHovered = false;

    public NumberInput(Number setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        height = 10;
        updateText();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        calculateHovered(mouseX, mouseY);

        drawHoveredStringWithInts(minusHovered, "-", xPosition - width, yPosition);
        drawStringWithInts(displayString, xPosition - width + minusWidth + gap, yPosition);
        drawHoveredStringWithInts(plusHovered, "+", xPosition - plusWidth, yPosition);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (plusHovered || minusHovered) {
            setting.set(setting.get(Integer.class) + (plusHovered ? setting.step : -setting.step));
            updateText();
            return true;
        }
        return false;
    }

    private void calculateHovered(int mouseX, int mouseY) {
        minusHovered = isHovered(xPosition - width, xPosition - width + minusWidth + gap, mouseX, mouseY);
        plusHovered = isHovered(xPosition - plusWidth, xPosition, mouseX, mouseY);
    }

    private boolean isHovered(int xStart, int xEnd, int mouseX, int mouseY) {
        return mouseX >= xStart && mouseY >= yPosition && mouseX < xEnd && mouseY < yPosition + height;
    }

    public void updateText() {
        displayString = (setting.prefix == null ? "" : setting.prefix) + setting.get(Integer.class) + (setting.suffix == null ? "" : setting.suffix);
        width = (int) Fonts.Inter.getStringWidth(displayString) + plusWidth + minusWidth + gap * 2;
    }

}
