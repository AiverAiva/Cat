package me.weikuwu.cute.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class Render3DEvent extends Event {
    public float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
