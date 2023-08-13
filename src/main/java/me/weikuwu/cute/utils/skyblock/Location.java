package me.weikuwu.cute.utils.skyblock;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.utils.gui.ScoreboardUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class Location {
    public static boolean inSkyBlock = false;
    public static boolean inDungeon = false;
    public static boolean forceSkyBlock = false;
    public static boolean forceDungeon = false;
    private final int ticks = 0;

/*
    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (forceDungeon || forceSkyBlock) {
            if (forceSkyBlock) inSkyBlock = true;
            if (forceDungeon) inSkyBlock = true;
            inDungeon = true;
            return;
        }

        if (ticks % 20 == 0) {
            if (CatMod.mc.thePlayer != null && CatMod.mc.theWorld != null) {

                if (scoreboardObj != null) {
                    inSkyBlock =
                    inDungeon = inSkyBlock && ScoreboardUtils.scoreboardContains("The Catacombs") && !ScoreboardUtils.scoreboardContains("Queue") || ScoreboardUtils.scoreboardContains("Cleared:");
                }


            }
            ticks = 0;
        }
        ticks++;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        forceDungeon = false;
        forceSkyBlock = false;
    }
*/

    public static Locations getLocation() {
        if (isInIsland()) return Locations.ISLAND;
        if (isInHub()) return Locations.HUB;
        if (isAtLift()) return Locations.LIFT;
        if (isInMist()) return Locations.THE_MIST;
        if (isInSkyblock()) return Locations.SKYBLOCK;
        if (isInLobby()) return Locations.LOBBY;
        if (isInLimbo()) return Locations.LIMBO;
        return Locations.NONE;
    }

    public static boolean isInIsland() {
        return hasLine("Your Island");
    }

    public static boolean isJacobs() {
        return hasLine("Jacob's Contest");
    }

    public static boolean isInHub() {
        return hasLine("Village") && !hasLine("Dwarven");
    }

    public static boolean isAtLift() {
        return hasLine("The Lift");
    }

    public static boolean isInDungeon() {
        return hasLine("Cleared:") || hasLine("Start");
    }

    public static boolean isInFloor(String floor) {
        return hasLine("The Catacombs (" + floor + ")");
    }

    public static boolean slayerBossSpawned() {
        return hasLine("Slay the boss!");
    }

    public static boolean isInSkyblock() {
        return removeFormatting(getScoreboardDisplayName()).contains("SKYBLOCK");
    }

    public static boolean isInLobby() {
        return removeFormatting(getScoreboardDisplayName()).contains("HYPIXEL");
    }

    public static boolean isInMist() {
        return hasLine("The Mist");
    }

    public static boolean isInLimbo() {
        IBlockState blockState = CatMod.mc.theWorld.getBlockState(CatMod.mc.thePlayer.getPosition().down());
        return blockState != null && blockState.getBlock() == Blocks.planks;
    }

    private static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    private static String getScoreboardDisplayName() {
        return CatMod.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
    }

    private static boolean hasLine(String name) {
        return ScoreboardUtils.scoreboardContains(name);
    }

    public enum Locations {ISLAND, HUB, LIFT, SKYBLOCK, LOBBY, LIMBO, THE_MIST, NONE}
}
