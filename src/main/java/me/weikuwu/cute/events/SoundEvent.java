package me.weikuwu.cute.events;

import net.minecraft.client.audio.ISound;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SoundEvent extends Event {
    public ISound sound;
    public String uuid;
    public String name;

    public SoundEvent(final ISound sound, final String uuid, final String name) {
        this.sound = sound;
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.sound + " - " + this.uuid + " - " + this.name;
    }
}
