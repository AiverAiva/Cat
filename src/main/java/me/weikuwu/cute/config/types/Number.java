package me.weikuwu.cute.config.types;

import me.weikuwu.cute.config.settings.Property;
import me.weikuwu.cute.config.settings.Setting;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class Number extends Setting implements Comparable<Integer> {

    public int step, min, max;
    public String prefix, suffix;

    public Number(Property annotation, Field field) {
        super(annotation, field);
        step = annotation.step();
        min = annotation.min();
        max = annotation.max();
        prefix = annotation.prefix();
        suffix = annotation.suffix();
    }

    @Override
    public boolean set(Object value) {
        return super.set(MathHelper.clamp_int((int) value, min, max));
    }

    @Override
    public int compareTo(@NotNull Integer other) {
        return Integer.compare(get(int.class), other);
    }
}
