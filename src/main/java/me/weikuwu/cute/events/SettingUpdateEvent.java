package me.weikuwu.cute.events;

import me.weikuwu.cute.config.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SettingUpdateEvent extends Event {
    public Setting setting;
    public Object oldValue;
    public Object newValue;

    public SettingUpdateEvent(Setting setting, Object oldValue, Object newValue) {
        this.setting = setting;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
