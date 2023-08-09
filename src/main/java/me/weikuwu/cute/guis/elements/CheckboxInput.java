package me.weikuwu.cute.guis.elements;

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
        hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

        drawRect(xPosition, yPosition, xPosition+width, yPosition+height, white.getRGB());

        if(setting.get(java.lang.Boolean.class)) {
            Fonts.Inter.drawString("§0x", xPosition+2, yPosition+1, 0xFFFFFF);
        } else if(hovered) {
            Fonts.Inter.drawString("§7x", xPosition+2, yPosition+1, 0xFFFFFF);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(hovered) {
            setting.set(!setting.get(java.lang.Boolean.class));
            return true;
        }
        return false;
    }

}
