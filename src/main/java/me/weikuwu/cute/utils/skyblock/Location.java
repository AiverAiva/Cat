package me.weikuwu.cute.utils.skyblock;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.EventManager;
import me.weikuwu.cute.events.TickEndEvent;
import me.weikuwu.cute.utils.gui.ScoreboardUtils;
import net.minecraft.block.Block;
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

    public static String getSkyBlockID(ItemStack item) {
        if (item != null) {
            NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
            if (extraAttributes != null && extraAttributes.hasKey("id")) {
                return extraAttributes.getString("id");
            }
        }
        return "";
    }

    public static String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest) ((GuiChest) gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    public static boolean isInteractable(Block block) {
        return new ArrayList<>(Arrays.asList(Blocks.chest, Blocks.lever, Blocks.trapped_chest, Blocks.wooden_button, Blocks.stone_button, Blocks.skull)).contains(block);
    }

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
                ScoreObjective scoreboardObj = CatMod.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                if (scoreboardObj != null) {
                    inSkyBlock = removeFormatting(scoreboardObj.getDisplayName()).contains("SKYBLOCK");
                }

                inDungeon = inSkyBlock && ScoreboardUtils.scoreboardContains("The Catacombs") && !ScoreboardUtils.scoreboardContains("Queue") || ScoreboardUtils.scoreboardContains("Dungeon Cleared:");
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

}
