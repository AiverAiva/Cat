package me.weikuwu.cute.events;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.jetbrains.annotations.Nullable;

public class ScreenOpenEvent extends Event {
    @Nullable
    public final GuiScreen screen;

    public ScreenOpenEvent(@Nullable GuiScreen screen) {
        this.screen = screen;
    }
}
