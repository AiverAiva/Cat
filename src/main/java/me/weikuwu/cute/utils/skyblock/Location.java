package me.weikuwu.cute.utils.skyblock;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.events.TickEndEvent;
import me.weikuwu.cute.utils.gui.ScoreboardUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class Location {
    public static boolean inSkyBlock = false;
    public static boolean inDungeon = false;
    public static boolean forceSkyBlock = false;
    public static boolean forceDungeon = false;
    private int ticks = 0;
    public static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }
    private static Minecraft mc;

//    @SubscribeEvent
//    public void onTick(TickEndEvent event) {
//        if (forceDungeon || forceSkyBlock) {
//            if (forceSkyBlock) inSkyBlock = true;
//            if (forceDungeon) inSkyBlock = true;
//            inDungeon = true;
//            return;
//        }
//
//        if (ticks % 20 == 0) {
//            if (CatMod.mc.thePlayer != null && CatMod.mc.theWorld != null) {
//
//                if (scoreboardObj != null) {
//                    inSkyBlock =
//                    inDungeon = inSkyBlock && ScoreboardUtils.scoreboardContains("The Catacombs") && !ScoreboardUtils.scoreboardContains("Queue") || ScoreboardUtils.scoreboardContains("Cleared:");
//                }
//
//
//            }
//            ticks = 0;
//        }
//        ticks++;
//    }
//
//    @SubscribeEvent
//    public void onWorldLoad(WorldEvent.Load event) {
//        forceDungeon = false;
//        forceSkyBlock = false;
//    }

    public static Locations getLocation() {
        if (isInIsland()) {
            return Locations.ISLAND;
        }
        if (isInHub()) {
            return Locations.HUB;
        }
        if (isAtLift()) {
            return Locations.LIFT;
        }
        if (isInMist()) {
            return Locations.THE_MIST;
        }
        if (isInSkyblock()) {
            return Locations.SKYBLOCK;
        }
        if (isInLobby()) {
            return Locations.LOBBY;
        }
        final IBlockState ibs = Location.mc.theWorld.getBlockState(Location.mc.thePlayer.getPosition().down());
        if (ibs != null && ibs.getBlock() == Blocks.planks) {
            return Locations.LIMBO;
        }
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

    public static boolean isInFloor(final String floor) {
        return hasLine("The Catacombs (" + floor + ")");
    }

    public static boolean slayerBossSpawned() {
        return hasLine("Slay the boss!");
    }

    public static boolean isInSkyblock() {
        ScoreObjective scoreboardObj = CatMod.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
        return removeFormatting(scoreboardObj.getDisplayName()).contains("SKYBLOCK");
    }

    public static boolean isInLobby() {
        ScoreObjective scoreboardObj = CatMod.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
        return removeFormatting(scoreboardObj.getDisplayName()).contains("HYPIXEL") || removeFormatting(scoreboardObj.getDisplayName()).contains("HYPIXEL");
    }

    public static boolean isInMist() {
        return hasLine("The Mist");
    }

    private static boolean hasLine(String name){
        return ScoreboardUtils.scoreboardContains(name);
    }

    public enum Locations {
        ISLAND,
        HUB,
        LIFT,
        SKYBLOCK,
        LOBBY,
        LIMBO,
        THE_MIST,
        NONE;
    }
}
