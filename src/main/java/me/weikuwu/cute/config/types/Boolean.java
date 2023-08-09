package me.weikuwu.cute.config.types;

import me.weikuwu.cute.config.settings.Property;
import me.weikuwu.cute.config.settings.Setting;
import me.weikuwu.cute.config.types.ParentSetting;

import java.lang.reflect.Field;

public class Boolean extends ParentSetting {

    public Property.Type type;

    public Boolean(Property annotation, Field field, Property.Type type) {
        super(annotation, field);
        this.type = type;
    }

    @Override
    public boolean set(Object value) {
        try {
            for (Setting child : children) {
                if (child instanceof ParentSetting) child.set(false);
            }

            return super.set(value);
        } catch (Exception exception) {
            System.out.println("Failed to set " + name + " to " + value);
            exception.printStackTrace();
        }
        return false;
    }
}
