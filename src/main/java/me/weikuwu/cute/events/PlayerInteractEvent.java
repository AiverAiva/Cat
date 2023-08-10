package me.weikuwu.cute.events;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerInteractEvent extends Event {
    public net.minecraftforge.event.entity.player.PlayerInteractEvent.Action action;
    public BlockPos pos;

    public PlayerInteractEvent(final net.minecraftforge.event.entity.player.PlayerInteractEvent.Action action, final BlockPos pos) {
        this.action = action;
        this.pos = pos;
    }
}
