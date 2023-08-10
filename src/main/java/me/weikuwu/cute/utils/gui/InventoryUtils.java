package me.weikuwu.cute.utils.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
    public static int findHotbarItemSlot(EntityPlayer player, String[] itemNames) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null) {
                String displayName = stack.getDisplayName();
                for (String itemName : itemNames) {
                    if (displayName.contains(itemName)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

}
