package me.weikuwu.cute.utils.math;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.utils.client.Rotation;
import me.weikuwu.cute.utils.client.RotationUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Vec {
    private static final Map<Integer, KeyBinding> keyBindMap = new HashMap<Integer, KeyBinding>() {
        {
            put(0, CatMod.mc.gameSettings.keyBindForward);
            put(90, CatMod.mc.gameSettings.keyBindLeft);
            put(180, CatMod.mc.gameSettings.keyBindBack);
            put(270, CatMod.mc.gameSettings.keyBindRight);
        }
    };

    public static Vec3 floorVec(Vec3 vec3) {
        return new Vec3(Math.floor(vec3.xCoord), Math.floor(vec3.yCoord), Math.floor(vec3.zCoord));
    }

    public static Vec3 ceilVec(Vec3 vec3) {
        return new Vec3(Math.ceil(vec3.xCoord), Math.ceil(vec3.yCoord), Math.ceil(vec3.zCoord));
    }

    public static double getHorizontalDistance(Vec3 vec1, Vec3 vec2) {
        double d0 = vec1.xCoord - vec2.xCoord;
        double d2 = vec1.zCoord - vec2.zCoord;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2);
    }

    public static List<KeyBinding> getNeededKeyPresses(Vec3 from, Vec3 to) {
        Rotation neededRot = RotationUtils.getNeededChange(RotationUtils.getRotation(from, to));
        double neededYaw = neededRot.getYaw() * -1.0;
        double n = 0;
        return keyBindMap.entrySet().stream()
                .filter(entry -> Math.abs(entry.getKey() - n) < 67.5 || Math.abs(entry.getKey() - (n + 360.0)) < 67.5)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public static List<KeyBinding> getOppositeKeys(List<KeyBinding> kbs) {
        return keyBindMap.entrySet().stream()
                .filter(entry -> kbs.stream().anyMatch(kb -> kb.equals(entry.getValue())))
                .map(entry -> keyBindMap.get((entry.getKey() + 180) % 360))
                .collect(Collectors.toList());
    }

    public static Vec3 times(Vec3 vec, float mult) {
        return new Vec3(vec.xCoord * mult, vec.yCoord * mult, vec.zCoord * mult);
    }
}
