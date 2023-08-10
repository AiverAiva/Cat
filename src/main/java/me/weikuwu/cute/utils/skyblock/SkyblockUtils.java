package me.weikuwu.cute.utils.skyblock;

import net.minecraft.client.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.entity.item.*;
import java.util.regex.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.block.state.*;
import com.google.common.collect.*;
import net.minecraft.scoreboard.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.projectile.*;
import java.util.*;

public class SkyblockUtils {
    private static Minecraft mc;

    public static int getMobHp(final EntityArmorStand aStand) {
        double mobHp = -1.0;
        String abbr = "";
        final Pattern pattern = Pattern.compile(" ?.+? ([.\\d]+)([Mk]?)/[.\\d]+[Mk]? ?");
        final String stripped = stripString(aStand.getName());
        final Matcher mat = pattern.matcher(stripped);
        if (stripped.contains("Grinch")) {
            return 1;
        }
        if (mat.matches()) {
            try {
                mobHp = Double.parseDouble(mat.group(1));
                abbr = mat.group(2);
            }
            catch (NumberFormatException ex) {}
        }
        if (mobHp != -1.0) {
            mobHp *= (abbr.equals("k") ? 1000 : (abbr.equals("M") ? 1000000 : 1));
        }
        return (int)Math.ceil(mobHp);
    }

    public static Entity getEntityCuttingOtherEntity(final Entity e, final Class<?> entityType) {
        final List<Entity> possible = (List<Entity>)SkyblockUtils.mc.theWorld.getEntitiesInAABBexcluding(e, e.getEntityBoundingBox().expand(0.3, 2.0, 0.3), a -> !a.isDead && !a.equals((Object)SkyblockUtils.mc.thePlayer) && !(a instanceof EntityFireball) && !(a instanceof EntityFishHook) && (entityType == null || entityType.isInstance(a)));
        if (!possible.isEmpty()) {
            return Collections.min((Collection<? extends Entity>)possible, Comparator.comparing(e2 -> e2.getDistanceToEntity(e)));
        }
        return null;
    }

    public static String stripString(final String s) {
        final char[] nonValidatedString = StringUtils.stripControlCodes(s).toCharArray();
        final StringBuilder validated = new StringBuilder();
        for (final char a : nonValidatedString) {
            if (a < '\u007f' && a > '\u0014') {
                validated.append(a);
            }
        }
        return validated.toString();
    }
}
