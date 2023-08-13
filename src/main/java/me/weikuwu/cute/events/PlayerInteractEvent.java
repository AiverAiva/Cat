package me.weikuwu.cute.events;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PlayerInteractEvent extends Event {
    public Action action;
    public BlockPos pos;

    public PlayerInteractEvent(Action action, BlockPos pos) {
        this.action = action;
        this.pos = pos;
    }
}
