package me.weikuwu.cute.utils;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.events.TickEndEvent;
import net.minecraft.block.Block;
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

public class Utils {
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
}
