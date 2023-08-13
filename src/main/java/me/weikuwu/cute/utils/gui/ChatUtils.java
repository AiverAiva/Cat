package me.weikuwu.cute.utils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
    private static final String PREFIX = "§d§lCat§r ";

    public static void send(String text) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + text));
        }
    }
}
