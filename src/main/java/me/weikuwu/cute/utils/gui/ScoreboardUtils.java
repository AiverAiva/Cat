package me.weikuwu.cute.utils.gui;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.utils.Utils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardUtils {

    public static String cleanSB(String scoreboard) {
        return StringUtils.stripControlCodes(scoreboard).chars().filter(c -> c > 20 && c < 127)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public static List<String> getScoreboard() {
        if (CatMod.mc.theWorld == null) return new ArrayList<>();
        Scoreboard scoreboard = CatMod.mc.theWorld.getScoreboard();
        if (scoreboard == null) return new ArrayList<>();

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return new ArrayList<>();

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = scores.stream()
                .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#"))
                .collect(Collectors.toList());

        scores = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, scores.size() - 15)) : list;

        return scores.stream().map(score -> {
                    ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                    return ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
                })
                .collect(Collectors.toList());
    }

    public static boolean scoreboardContains(String string) {
        List<String> scoreboard = getScoreboard();
        return scoreboard.stream()
                .map(line -> Utils.removeFormatting(cleanSB(line)))
                .anyMatch(line -> line.contains(string));
    }
}
