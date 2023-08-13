package me.weikuwu.cute.utils.skyblock;

import me.weikuwu.cute.CatMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyblockUtils {

    public static int getMobHp(EntityArmorStand aStand) {
        String stripped = stripString(aStand.getName());
        if (stripped.contains("Grinch")) {
            return 1;
        }

        Pattern pattern = Pattern.compile(" ?.+? ([.\\d]+)([Mk]?)/[.\\d]+[Mk]? ?");
        Matcher mat = pattern.matcher(stripped);
        if (mat.matches()) {
            double mobHp = Double.parseDouble(mat.group(1));
            String abbr = mat.group(2);
            mobHp *= (abbr.equals("k") ? 1000 : (abbr.equals("M") ? 1000000 : 1));
            return (int) Math.ceil(mobHp);
        }

        return -1;
    }

    public static Entity getEntityCuttingOtherEntity(Entity e, Class<?> entityType) {
        List<Entity> possible = CatMod.mc.theWorld.getEntitiesInAABBexcluding(
                e, e.getEntityBoundingBox().expand(0.3, 2.0, 0.3),
                a -> !a.isDead && !(a instanceof EntityFireball) && !(a instanceof EntityFishHook) && (entityType == null || entityType.isInstance(a))
        );

        return possible.isEmpty() ? null : Collections.min(possible, Comparator.comparing(e2 -> e2.getDistanceToEntity(e)));
    }

    public static String stripString(String s) {
        StringBuilder validated = new StringBuilder();
        for (char a : s.toCharArray()) {
            if (a < '\u007f' && a > '\u0014') {
                validated.append(a);
            }
        }
        return validated.toString();
    }
}
