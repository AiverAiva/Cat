package me.weikuwu.cute.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickEndEvent extends Event {
    private static int staticCount = 0;
    public int count;

    public TickEndEvent() {
        count = staticCount;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MinecraftForge.EVENT_BUS.post(new TickEndEvent());
            staticCount++;
        }
    }
}
