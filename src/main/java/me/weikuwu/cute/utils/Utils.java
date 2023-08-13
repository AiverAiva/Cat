package me.weikuwu.cute.utils;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

public class Utils {
    public static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    public static String getSkyBlockID(ItemStack item) {
        NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
        return extraAttributes != null && extraAttributes.hasKey("id") ? extraAttributes.getString("id") : "";
    }

    public static String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest) ((GuiChest) gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    public static boolean isInteractable(Block block) {
        return Arrays.asList(Blocks.chest, Blocks.lever, Blocks.trapped_chest, Blocks.wooden_button, Blocks.stone_button, Blocks.skull).contains(block);
    }
}
