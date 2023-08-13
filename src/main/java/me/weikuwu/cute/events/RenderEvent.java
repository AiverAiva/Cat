package me.weikuwu.cute.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderEvent extends Event {
    public final Stage stage;
    public final float deltaTicks;

    public RenderEvent(Stage stage, float deltaTicks) {
        this.stage = stage;
        this.deltaTicks = deltaTicks;
    }
}