package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.config.settings.Setting;
import me.weikuwu.cute.config.types.Boolean;
import me.weikuwu.cute.config.types.Number;
import me.weikuwu.cute.config.types.Select;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public abstract class ConfigInput extends GuiButton {

    public static Color white = new Color(255, 255, 255);
    public static Color green = new Color(85, 255, 85);
    public static Color red = new Color(255, 85, 85);
    public static Color transparent = new Color(255, 255, 255, 64);

    public Setting setting;

    public ConfigInput(Setting setting, int x, int y) {
        super(0, x, y, "");
        this.setting = setting;
    }

    public static ConfigInput buttonFromSetting(Setting setting, int x, int y) {
        switch (setting.annotation.type()) {
            case BOOLEAN:
                return new SwitchInput((Boolean) setting, x, y);

            case CHECKBOX:
                return new CheckboxInput((Boolean) setting, x, y);

//            case FOLDER:
//                return new FolderComponent((Folder) setting, x, y);

            case NUMBER:
                return new NumberInput((Number) setting, x, y);

            case SELECT:
                return new SelectInput((Select) setting, x, y);

//            case BUTTON:
//                return new ButtonInput((Button) setting, x, y);

//            case SPACER:
//                return new SpacerComponent((SpacerSetting) setting, x, y);

            default:
                return null;
        }
    }

    public static boolean handleMousePress(Boolean setting, boolean hovered) {
        if (hovered) {
            setting.set(!setting.get(java.lang.Boolean.class));
            return true;
        }
        return false;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
    }

    public static void drawStringWithDoubles(String text, double x, double y) {
        Fonts.Inter.drawString(text, x, y, 0xFFFFFFFF);
    }

    public void drawHoveredStringWithInts(boolean hovered, String text, int x, int y) {
        drawStringWithInts((hovered ? "§a" : "§7") + text, x, y);
    }

    public void drawStringWithInts(String text, int x, int y) {
        Fonts.Inter.drawString(text, x, y, 0xFFFFFF);
    }

}
