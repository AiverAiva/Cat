package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.config.types.Boolean;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.Minecraft;

public class CheckboxInput extends ConfigInput {

    public Boolean setting;

    public CheckboxInput(Boolean setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        super.width = 9;
        super.height = 9;
        super.xPosition -= 9;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hovered = isHovered(mouseX, mouseY);

        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, white.getRGB());

        String color = setting.get(java.lang.Boolean.class) ? "§0x" : (hovered ? "§7x" : "");
        Fonts.Inter.drawString(color, xPosition + 2, yPosition + 1, 0xFFFFFF);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return handleMousePress(setting, hovered);
    }

}
