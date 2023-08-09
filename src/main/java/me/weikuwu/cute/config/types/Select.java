package me.weikuwu.cute.config.types;

import me.weikuwu.cute.config.settings.Property;
import me.weikuwu.cute.config.settings.Setting;

import java.lang.reflect.Field;

public class Select extends Setting {

    public String[] options;

    public Select(Property annotation, Field field) {
        super(annotation, field);
        options = annotation.options();
    }

    @Override
    public boolean set(Object value) {
        if (((java.lang.Number) value).intValue() > options.length - 1) return super.set(0);
        if (((java.lang.Number) value).intValue() < 0) return super.set(options.length - 1);
        return super.set(value);
    }
}
