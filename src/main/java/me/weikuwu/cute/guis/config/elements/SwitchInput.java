package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.config.types.Boolean;
import net.minecraft.client.Minecraft;

public class SwitchInput extends ConfigInput {

    public Boolean setting;

    public SwitchInput(Boolean setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        super.width = 25;
        super.height = 9;
        super.xPosition -= width;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hovered = isHovered(mouseX, mouseY);
        boolean isEnabled = setting.get(java.lang.Boolean.class);

        drawRect(xPosition, yPosition + 3, xPosition + width, yPosition + 6, white.getRGB());

        drawRect(isEnabled ? xPosition + width - height : xPosition, yPosition,
                isEnabled ? xPosition + width : xPosition + height, yPosition + height,
                isEnabled ? green.getRGB() : red.getRGB());
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return handleMousePress(setting, hovered);
    }

}
