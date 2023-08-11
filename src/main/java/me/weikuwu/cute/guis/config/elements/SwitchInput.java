package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.config.types.Boolean;
import net.minecraft.client.Minecraft;

public class SwitchInput extends ConfigInput {

    public Boolean setting;

    public SwitchInput(Boolean setting, int x, int y) {
        super(setting, x, y); // subtract width so button is right-aligned
        this.setting = setting;
        super.width = 25;
        super.height = 9;
        super.xPosition -= width;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        drawRect(xPosition, yPosition+3, xPosition+width, yPosition+6, white.getRGB());
        drawRect(setting.get(java.lang.Boolean.class) ? xPosition+width-height : xPosition, yPosition,
                setting.get(java.lang.Boolean.class) ? xPosition+width : xPosition+height, yPosition+height,
                setting.get(java.lang.Boolean.class) ? green.getRGB() : red.getRGB());
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height) {
            setting.set(!setting.get(java.lang.Boolean.class));
            return true;
        }
        return false;
    }
}
