package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.config.types.Select;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.Minecraft;

public class SelectInput extends ConfigInput {

    private final int leftWidth = (int) Fonts.Inter.getStringWidth("<");
    private final int rightWidth = (int) Fonts.Inter.getStringWidth(">");
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
        rightHovered = mouseX >= xPosition - rightWidth - gap && mouseY >= yPosition && mouseX < xPosition && mouseY < yPosition + height;
        leftHovered = mouseX >= xPosition - width && mouseY >= yPosition && mouseX < xPosition - width + leftWidth + gap && mouseY < yPosition + height;

        Fonts.Inter.drawString((leftHovered ? "§a" : "§7") + "<", xPosition - width, yPosition, 0xFFFFFF);
        Fonts.Inter.drawString(displayString, xPosition - width + leftWidth + gap, yPosition, 0xFFFFFF);
        Fonts.Inter.drawString((rightHovered ? "§a" : "§7") + ">", xPosition - rightWidth, yPosition, 0xFFFFFF);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (rightHovered || leftHovered) {
            if (rightHovered) setting.set(setting.get(Integer.class) + 1);
            if (leftHovered) setting.set(setting.get(Integer.class) - 1);
            updateText();
            return true;
        }
        return false;
    }

    public void updateText() {
        displayString = setting.options[setting.get(Integer.class)];
        width = (int) Fonts.Inter.getStringWidth(displayString) + rightWidth + leftWidth + gap * 2;
    }
}
