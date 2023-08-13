package me.weikuwu.cute.config.types;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.config.ConfigManager;
import me.weikuwu.cute.config.settings.DoNotSave;
import me.weikuwu.cute.config.settings.Property;
import me.weikuwu.cute.config.settings.Setting;

import java.lang.reflect.Field;

public class Folder extends ParentSetting implements DoNotSave {

    public Folder(Property annotation, Field field) {
        super(annotation, field);
    }

    public static boolean isEnabled(String name) {
        Setting setting = ConfigManager.getSettingByName(name, CatMod.settings);
        return setting instanceof Folder && ((Folder) setting).isChildEnabled();
    }

    public boolean isChildEnabled() {
        return children.stream().anyMatch(child -> (child instanceof Boolean && child.get(java.lang.Boolean.class)) ||
                (child instanceof Folder && ((Folder) child).isChildEnabled()));
    }
}
