package me.weikuwu.cute.utils.client;

import me.weikuwu.cute.utils.math.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RotationUtils {
    private static final Minecraft mc;
    private static final float[][] BLOCK_SIDES;
    public static Rotation startRot;
    public static Rotation neededChange;
    public static Rotation endRot;
    public static long startTime;
    public static long endTime;
    public static boolean done;

    static {
        mc = Minecraft.getMinecraft();
        RotationUtils.done = true;
        BLOCK_SIDES = new float[][]{{0.5f, 0.01f, 0.5f}, {0.5f, 0.99f, 0.5f}, {0.01f, 0.5f, 0.5f}, {0.99f, 0.5f, 0.5f}, {0.5f, 0.5f, 0.01f}, {0.5f, 0.5f, 0.99f}};
    }

    public static Rotation getRotation(final Vec3 vec) {
        final Vec3 eyes = RotationUtils.mc.thePlayer.getPositionEyes(1.0f);
        return getRotation(eyes, vec);
    }

    public static Rotation getRotation(final Vec3 from, final Vec3 to) {
        final double diffX = to.xCoord - from.xCoord;
        final double diffY = to.yCoord - from.yCoord;
        final double diffZ = to.zCoord - from.zCoord;
        return new Rotation(MathHelper.wrapAngleTo180_float((float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0)), (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))));
    }

    public static Rotation getRotation(final BlockPos bp) {
        final Vec3 vec = new Vec3(bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5);
        return getRotation(vec);
    }

    public static void setup(final Rotation rot, final Long aimTime) {
        RotationUtils.done = false;
        RotationUtils.startRot = new Rotation(RotationUtils.mc.thePlayer.rotationYaw, RotationUtils.mc.thePlayer.rotationPitch);
//        RotationUtils.neededChange = getNeededChange(RotationUtils.startRot, rot);
        RotationUtils.endRot = new Rotation(RotationUtils.startRot.getYaw() + RotationUtils.neededChange.getYaw(), RotationUtils.startRot.getPitch() + RotationUtils.neededChange.getPitch());
        RotationUtils.startTime = System.currentTimeMillis();
        RotationUtils.endTime = System.currentTimeMillis() + aimTime;
    }

    public static void reset() {
        RotationUtils.done = true;
        RotationUtils.startRot = null;
        RotationUtils.neededChange = null;
        RotationUtils.endRot = null;
        RotationUtils.startTime = 0L;
        RotationUtils.endTime = 0L;
    }

    public static void update() {
        if (System.currentTimeMillis() <= RotationUtils.endTime) {
            RotationUtils.mc.thePlayer.rotationYaw = interpolate(RotationUtils.startRot.getYaw(), RotationUtils.endRot.getYaw());
            RotationUtils.mc.thePlayer.rotationPitch = interpolate(RotationUtils.startRot.getPitch(), RotationUtils.endRot.getPitch());
        } else if (!RotationUtils.done) {
            RotationUtils.mc.thePlayer.rotationYaw = RotationUtils.endRot.getYaw();
            RotationUtils.mc.thePlayer.rotationPitch = RotationUtils.endRot.getPitch();
            reset();
        }
    }

    public static void snapAngles(final Rotation rot) {
        RotationUtils.mc.thePlayer.rotationYaw = rot.getYaw();
        RotationUtils.mc.thePlayer.rotationPitch = rot.getPitch();
    }

    private static float interpolate(final float start, final float end) {
        final float spentMillis = (float) (System.currentTimeMillis() - RotationUtils.startTime);
        final float relativeProgress = spentMillis / (RotationUtils.endTime - RotationUtils.startTime);
        return (end - start) * easeOutCubic(relativeProgress) + start;
    }

//    public static Rotation getNeededChange(final Rotation startRot, final Rotation endRot) {
//        float yawChng = MathHelper.warpAngleTo180_float(endRot.getYaw()) - MathHelper.warpAngleTo180_float(startRot.getYaw());
//        if (yawChng <= -180.0f) {
//            yawChng += 360.0f;
//        }
//        else if (yawChng > 180.0f) {
//            yawChng -= 360.0f;
//        }
//        if (BloodCamp.godGamerMode.isEnabled()) {
//            if (yawChng < 0.0f) {
//                yawChng += 360.0f;
//            }
//            else {
//                yawChng -= 360.0f;
//            }
//        }
//        return new Rotation(yawChng, endRot.getPitch() - startRot.getPitch());
//    }

    public static float easeOutCubic(final double number) {
        return (float) (1.0 - Math.pow(1.0 - number, 3.0));
    }

    public static double fovFromEntity(final Entity en) {
        return ((RotationUtils.mc.thePlayer.rotationYaw - fovToEntity(en)) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static double fovFromVec3(final Vec3 vec) {
        return ((RotationUtils.mc.thePlayer.rotationYaw - fovToVec3(vec)) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static float fovToVec3(final Vec3 vec) {
        final double x = vec.xCoord - RotationUtils.mc.thePlayer.posX;
        final double z = vec.zCoord - RotationUtils.mc.thePlayer.posZ;
        final double yaw = Math.atan2(x, z) * 57.2957795;
        return (float) (yaw * -1.0);
    }

//    public static Rotation getNeededChange(final Rotation endRot) {
//        final Rotation startRot = new Rotation(RotationUtils.mc.thePlayer.rotationYaw, RotationUtils.mc.thePlayer.rotationPitch);
//        return getNeededChange(startRot, endRot);
//    }

    public static float fovToEntity(final Entity ent) {
        final double x = ent.posX - RotationUtils.mc.thePlayer.posX;
        final double z = ent.posZ - RotationUtils.mc.thePlayer.posZ;
        final double yaw = Math.atan2(x, z) * 57.2957795;
        return (float) (yaw * -1.0);
    }

    public static List<Vec3> getBlockSides(final BlockPos bp) {
        final List<Vec3> ret = new ArrayList<Vec3>();
        for (final float[] side : RotationUtils.BLOCK_SIDES) {
            ret.add(new Vec3(bp).addVector(side[0], side[1], side[2]));
        }
        return ret;
    }

    public static boolean lookingAt(final BlockPos blockPos, final float range) {
        final float stepSize = 0.15f;
        Vec3 position = new Vec3(RotationUtils.mc.thePlayer.posX, RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight(), RotationUtils.mc.thePlayer.posZ);
        final Vec3 look = RotationUtils.mc.thePlayer.getLook(0.0f);
        final Vector3f step = new Vector3f((float) look.xCoord, (float) look.yCoord, (float) look.zCoord);
        step.scale(stepSize / step.length());
        for (int i = 0; i < Math.floor(range / stepSize) - 2.0; ++i) {
            final BlockPos blockAtPos = new BlockPos(position.xCoord, position.yCoord, position.zCoord);
            if (blockAtPos.equals(blockPos)) {
                return true;
            }
            position = position.add(new Vec3(step.x, step.y, step.z));
        }
        return false;
    }

    public static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f2 = -MathHelper.cos(-pitch * 0.017453292f);
        return new Vec3(MathHelper.sin(-yaw * 0.017453292f - 3.1415927f) * f2, MathHelper.sin(-pitch * 0.017453292f), MathHelper.cos(-yaw * 0.017453292f - 3.1415927f) * f2);
    }

    public static Vec3 getLook(final Vec3 vec) {
        final double diffX = vec.xCoord - RotationUtils.mc.thePlayer.posX;
        final double diffY = vec.yCoord - (RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - RotationUtils.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        return getVectorForRotation((float) (-(MathHelper.atan2(diffY, dist) * 180.0 / 3.141592653589793)), (float) (MathHelper.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0));
    }

    public static EnumFacing calculateEnumfacing(final Vec3 pos) {
        final int x = MathHelper.floor_double(pos.xCoord);
        final int y = MathHelper.floor_double(pos.yCoord);
        final int z = MathHelper.floor_double(pos.zCoord);
        final MovingObjectPosition position = calculateIntercept(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), pos, 50.0f);
        return (position != null) ? position.sideHit : null;
    }

    public static MovingObjectPosition calculateIntercept(final AxisAlignedBB aabb, final Vec3 block, final float range) {
        final Vec3 vec3 = RotationUtils.mc.thePlayer.getPositionEyes(1.0f);
        final Vec3 vec4 = getLook(block);
        return aabb.calculateIntercept(vec3, vec3.addVector(vec4.xCoord * range, vec4.yCoord * range, vec4.zCoord * range));
    }

    public static List<Vec3> getPointsOnBlock(final BlockPos bp) {
        final List<Vec3> ret = new ArrayList<Vec3>();
        for (final float[] side : RotationUtils.BLOCK_SIDES) {
            for (int i = 0; i < 20; ++i) {
                float x = side[0];
                float y = side[1];
                float z = side[2];
                if (x == 0.5) {
                    x = RandomUtils.randBetween(0.1f, 0.9f);
                }
                if (y == 0.5) {
                    y = RandomUtils.randBetween(0.1f, 0.9f);
                }
                if (z == 0.5) {
                    z = RandomUtils.randBetween(0.1f, 0.9f);
                }
                ret.add(new Vec3(bp).addVector(x, y, z));
            }
        }
        return ret;
    }

    public static Rotation getNeededChange(final Rotation startRot, final Rotation endRot) {
        float yawChng = MathHelper.wrapAngleTo180_float(endRot.getYaw()) - MathHelper.wrapAngleTo180_float(startRot.getYaw());
        if (yawChng <= -180.0f) {
            yawChng += 360.0f;
        } else if (yawChng > 180.0f) {
            yawChng -= 360.0f;
        }
//        if (BloodCamp.godGamerMode.isEnabled()) {
//            if (yawChng < 0.0f) {
//                yawChng += 360.0f;
//            }
//            else {
//                yawChng -= 360.0f;
//            }
//        }
        return new Rotation(yawChng, endRot.getPitch() - startRot.getPitch());
    }

    public static Rotation getNeededChange(final Rotation endRot) {
        final Rotation startRot = new Rotation(RotationUtils.mc.thePlayer.rotationYaw, RotationUtils.mc.thePlayer.rotationPitch);
        return getNeededChange(startRot, endRot);
    }
}
