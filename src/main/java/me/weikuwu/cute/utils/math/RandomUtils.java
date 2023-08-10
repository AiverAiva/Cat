package me.weikuwu.cute.utils.math;

import net.minecraft.util.Vec3;

import java.util.Random;

public class RandomUtils {
    private static Random rand;

    public static Vec3 randomVec() {
        return new Vec3(RandomUtils.rand.nextDouble(), RandomUtils.rand.nextDouble(), RandomUtils.rand.nextDouble());
    }

    public static int randBetween(final int a, final int b) {
        return RandomUtils.rand.nextInt(b - a + 1) + a;
    }

    public static double randBetween(final double a, final double b) {
        return RandomUtils.rand.nextDouble() * (b - a) + a;
    }

    public static float randBetween(final float a, final float b) {
        return RandomUtils.rand.nextFloat() * (b - a) + a;
    }

    public static int nextInt(final int yep) {
        return RandomUtils.rand.nextInt(yep);
    }

    static {
        RandomUtils.rand = new Random();
    }
}
