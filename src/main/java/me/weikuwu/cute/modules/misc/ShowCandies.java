package me.weikuwu.cute.modules.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.weikuwu.cute.config.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ShowCandies {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onItemTooltipLow(ItemTooltipEvent event) {
        if(Config.showCandies){
            List<String> newTooltip = new ArrayList<>();
            NBTTagCompound tag = event.itemStack.getTagCompound();


            if(tag!=null && tag.hasKey("ExtraAttributes")) {
                NBTTagCompound attributes = tag.getCompoundTag("ExtraAttributes");
                if(attributes.hasKey("petInfo")){
                    String petInfoJson = attributes.getString("petInfo");
                    try {
                        JsonObject petInfoObject = new JsonParser().parse(petInfoJson).getAsJsonObject();
                        int candyUsed = petInfoObject.get("candyUsed").getAsInt();
                        newTooltip.add(EnumChatFormatting.DARK_GREEN + "Candy used: " + EnumChatFormatting.GRAY + candyUsed);
                    } catch (JsonParseException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
            event.toolTip.addAll(newTooltip);
        }
    }
}
