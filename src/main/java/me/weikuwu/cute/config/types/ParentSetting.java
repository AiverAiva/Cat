package me.weikuwu.cute.config.types;

import me.weikuwu.cute.config.settings.Property;
import me.weikuwu.cute.config.settings.Setting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class ParentSetting extends Setting {

    public ArrayList<Setting> children = new ArrayList<>();

    public ParentSetting(Property annotation, Field field) {
        super(annotation, field);
    }

    public ArrayList<Setting> getChildren(ArrayList<Setting> settings) {
        return settings.stream().filter(setting -> setting.parent == this).collect(Collectors.toCollection(ArrayList::new));
    }
}
