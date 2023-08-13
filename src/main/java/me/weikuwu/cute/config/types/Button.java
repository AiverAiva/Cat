package me.weikuwu.cute.config.types;

import me.weikuwu.cute.config.settings.DoNotSave;
import me.weikuwu.cute.config.settings.Property;
import me.weikuwu.cute.config.settings.Setting;

import java.lang.reflect.Field;

public class Button extends Setting implements DoNotSave {

    private final Runnable runnable;
    public String buttonText = "Click";

    public Button(Property annotation, Field field) {
        super(annotation, field);
        buttonText = annotation.button().isEmpty() ? buttonText : annotation.button();
        runnable = get(Runnable.class);
    }

    public void run() {
        runnable.run();
    }
}
