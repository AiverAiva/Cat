package me.weikuwu.cute.utils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
    private static final String PREFIX;

    public static void send(String text) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(ChatUtils.PREFIX + text));
    }

    static {
        PREFIX = "§d§lCat§r ";
    }
}
