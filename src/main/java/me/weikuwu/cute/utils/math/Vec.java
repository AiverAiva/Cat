package me.weikuwu.cute.utils.math;

import me.weikuwu.cute.utils.client.Rotation;
import me.weikuwu.cute.utils.client.RotationUtils;
import net.minecraft.client.*;
import net.minecraft.client.settings.*;
import net.minecraft.util.*;
import java.util.*;

public class Vec {
    private static Minecraft mc;
    private static Map<Integer, KeyBinding> keyBindMap;

    public static Vec3 floorVec(final Vec3 vec3) {
        return new Vec3(Math.floor(vec3.xCoord), Math.floor(vec3.yCoord), Math.floor(vec3.zCoord));
    }

    public static Vec3 ceilVec(final Vec3 vec3) {
        return new Vec3(Math.ceil(vec3.xCoord), Math.ceil(vec3.yCoord), Math.ceil(vec3.zCoord));
    }

    public static double getHorizontalDistance(final Vec3 vec1, final Vec3 vec2) {
        final double d0 = vec1.xCoord - vec2.xCoord;
        final double d2 = vec1.zCoord - vec2.zCoord;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2);
    }

    public static List<KeyBinding> getNeededKeyPresses(final Vec3 from, final Vec3 to) {
        final List<KeyBinding> damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses = new ArrayList<KeyBinding>();
        final Rotation neededRot = RotationUtils.getNeededChange(RotationUtils.getRotation(from, to));
        final double neededYaw = neededRot.getYaw() * -1.0f;
        final double n = 0;
        final List<KeyBinding> list=null;
        Vec.keyBindMap.forEach((k, v) -> {
            if (Math.abs(k - n) < 67.5 || Math.abs(k - (n + 360.0)) < 67.5) {
                list.add(v);
            }
            return;
        });
        return damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses;
    }

    public static List<KeyBinding> getOppositeKeys(final List<KeyBinding> kbs) {
        final List<KeyBinding> ret = new ArrayList<KeyBinding>();
        final List<KeyBinding> list=null;
        Vec.keyBindMap.forEach((k, v) -> {
            if (kbs.stream().anyMatch(kb -> kb.equals(v))) {
                list.add(Vec.keyBindMap.get((k + 180) % 360));
            }
            return;
        });
        return ret;
    }

    public static Vec3 times(final Vec3 vec, final float mult) {
        return new Vec3(vec.xCoord * mult, vec.yCoord * mult, vec.zCoord * mult);
    }

    static {
        Vec.mc = Minecraft.getMinecraft();
        Vec.keyBindMap = new HashMap<Integer, KeyBinding>() {
            {
                this.put(0, Vec.mc.gameSettings.keyBindForward);
                this.put(90, Vec.mc.gameSettings.keyBindLeft);
                this.put(180, Vec.mc.gameSettings.keyBindBack);
                this.put(270, Vec.mc.gameSettings.keyBindRight);
            }
        };
    }
}
