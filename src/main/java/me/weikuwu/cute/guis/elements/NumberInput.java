package me.weikuwu.cute.guis.elements;

import me.weikuwu.cute.config.types.Number;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.Minecraft;

public class NumberInput extends ConfigInput {

    private int minusWidth = (int) Fonts.Inter.getStringWidth("-");
    private int plusWidth = (int) Fonts.Inter.getStringWidth("+");
    private int gap = 3;
    private boolean minusHovered = false;
    private boolean plusHovered = false;

    public Number setting;

    public NumberInput(Number setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        height = 10;
        updateText();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        plusHovered = mouseX>=xPosition-plusWidth-gap && mouseY>=yPosition && mouseX<xPosition && mouseY<yPosition+height;
        minusHovered = mouseX>=xPosition-width && mouseY>=yPosition && mouseX<xPosition-width+minusWidth+gap && mouseY<yPosition+height;

        //                    - width +                  |---- xPosition
        //     - hitbox                        + hitbox
        // |-------------|------------------|------------|
        // minusWidth     displayStringWidth     plusWidth
        // pro-tip: it helps to trace your finger along this diagram

        Fonts.Inter.drawString((minusHovered?"§c":"§7")+"-", xPosition-width, yPosition, 0xFFFFFF);
        Fonts.Inter.drawString(displayString, xPosition-width+minusWidth+gap, yPosition, 0xFFFFFF);
        Fonts.Inter.drawString((plusHovered?"§a":"§7")+"+", xPosition-plusWidth, yPosition, 0xFFFFFF);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(plusHovered || minusHovered) {
            if(plusHovered) setting.set(setting.get(Integer.class)+setting.step);
            if(minusHovered) setting.set(setting.get(Integer.class)-setting.step);
            updateText();
            return true;
        }
        return false;
    }

    public void updateText() {
        displayString = (setting.prefix == null ? "" : setting.prefix) + setting.get(Integer.class) + (setting.suffix == null ? "" : setting.suffix);
        width = (int) Fonts.Inter.getStringWidth(displayString) + plusWidth+minusWidth + gap*2;
    }

}
