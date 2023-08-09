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
        if (setting == null) return false;
        return ((Folder) setting).isChildEnabled();
    }

    public boolean isChildEnabled() {
        for (Setting child : children) {
            if (child instanceof Boolean && child.get(java.lang.Boolean.class)) return true;
            if (child instanceof Folder && ((Folder) child).isChildEnabled()) return true;
        }
        return false;
    }
}
