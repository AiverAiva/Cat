package me.weikuwu.cute.modules.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.weikuwu.cute.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class DeveloperMode {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onItemTooltip(ItemTooltipEvent event) {
        if (Config.devMode) {
            List<String> newTooltip = new ArrayList<>();
            NBTTagCompound tag = event.itemStack.getTagCompound();

            if(tag == null || !tag.hasKey("ExtraAttributes")) return;
            if(!tag.getCompoundTag("ExtraAttributes").hasKey("attributes")) return;

            NBTTagCompound attribues = tag.getCompoundTag("ExtraAttributes").getCompoundTag("attributes");


            event.toolTip.addAll(newTooltip);
        }
    }
}
