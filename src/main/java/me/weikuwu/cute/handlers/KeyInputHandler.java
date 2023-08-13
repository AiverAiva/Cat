package me.weikuwu.cute.handlers;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.KeyBindings;
import me.weikuwu.cute.guis.profileviewer.ProfileViewerGUI;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleAutofish.isPressed()) {
            CatMod.gui = new ProfileViewerGUI();
//            AutoFish.Enabled = !AutoFish.Enabled;
//            AutoFish.Status++;
        }
    }
}
