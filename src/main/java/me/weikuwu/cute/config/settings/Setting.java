package me.weikuwu.cute.config.settings;

import me.weikuwu.cute.config.types.ParentSetting;
import me.weikuwu.cute.events.SettingUpdateEvent;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;

public abstract class Setting {

    public String name;
    public ParentSetting parent = null;
    public String note;
    public boolean warning;
    public boolean beta;
    public Field field;
    public Property annotation;

    public Setting(Property annotation, Field field) {
        this.annotation = annotation;
        name = annotation.name();
        warning = annotation.warning();
        beta = annotation.beta();
        if (!annotation.note().equals("")) note = annotation.note();
        this.field = field;
    }

    public int getIndent(int startingIndent) {
        return getIndent(startingIndent, this);
    }

    public int getIndent(int startingIndent, Setting setting) {
        if (setting.parent != null) {
            startingIndent += 10;
            return setting.getIndent(startingIndent, setting.parent);
        }
        return startingIndent;
    }

    public <T> T get(Class<T> type) {
        try {
            return type.cast(field.get(Object.class));
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean set(Object value) {
        try {
            Object oldValue = get(Object.class);
            field.set(value.getClass(), value);
            MinecraftForge.EVENT_BUS.post(new SettingUpdateEvent(this, oldValue, value));
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public void forceSet(Object value) {
        try {
            field.set(value.getClass(), value);
        } catch (Exception ignored) {
        }
    }

}