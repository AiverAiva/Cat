package me.weikuwu.cute.handlers;

import me.weikuwu.cute.KeyBindings;
import me.weikuwu.cute.modules.macros.AutoFish;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleAutofish.isPressed()) {
            AutoFish.Enabled = !AutoFish.Enabled;
            AutoFish.Status++;
        }
    }
}
