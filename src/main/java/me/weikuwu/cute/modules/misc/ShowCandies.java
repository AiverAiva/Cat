package me.weikuwu.cute.modules.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.weikuwu.cute.config.Config;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ShowCandies {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onItemTooltip(ItemTooltipEvent event) {
        if (Config.showCandies) {
            List<String> newTooltip = new ArrayList<>();
            JsonObject petInfo = getPetInfo(event.itemStack);
            if (petInfo != null) {
                int candyUsed = petInfo.get("candyUsed").getAsInt();
                newTooltip.add("§2Candy used: §7" + candyUsed);
            }
            event.toolTip.addAll(newTooltip);
        }
    }

    private JsonObject getPetInfo(ItemStack itemStack) {
        try {
            String petInfoJson = itemStack.getTagCompound().getCompoundTag("ExtraAttributes").getString("petInfo");
            return new JsonParser().parse(petInfoJson).getAsJsonObject();
        } catch (NullPointerException | JsonParseException e) {
            System.err.println("Exception while getting pet info");
            e.printStackTrace();
        }
        return null;
    }
}
