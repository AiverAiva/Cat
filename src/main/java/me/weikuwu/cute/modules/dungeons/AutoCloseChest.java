package me.weikuwu.cute.modules.dungeons;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.config.Config;
import me.weikuwu.cute.utils.Utils;
import me.weikuwu.cute.utils.skyblock.Location;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoCloseChest {

    private int tickCounter = 0;
    private boolean shouldCloseScreen = false;

    /*
     @SubscribeEvent
     public static void onGuiOpen(GuiOpenEvent event) {
         if (event.gui instanceof GuiChest && Location.inSkyBlock) {
             if (Location.inDungeon && Config.closeSecretChests && Utils.getGuiName(event.gui).equals("Chest")) {
                 event.setCanceled(true);
             }
         }
     }
    */

    @SubscribeEvent
    public void onGuiBackgroundRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest && Location.isInSkyblock()
                && Location.isInDungeon() && Config.closeSecretChests && Utils.getGuiName(event.gui).equals("Chest")) {
            shouldCloseScreen = true;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (shouldCloseScreen) {
            if (++tickCounter >= Config.closeSecretChestsDelay) {
                CatMod.mc.thePlayer.closeScreen();
                tickCounter = 0;
                shouldCloseScreen = false;
            }
        }
    }
}
