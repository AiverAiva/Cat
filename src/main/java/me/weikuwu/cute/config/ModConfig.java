package me.weikuwu.cute.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ModConfig {
    public static Configuration config;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        config.load();

        boolean enableFeature = config.getBoolean("EnableFeature", "general", true, "Enable the new feature");
        int maxNumberOfItems = config.getInt("MaxNumberOfItems", "general", 64, 1, 64, "Maximum number of items in a stack");

        if (config.hasChanged()) {
            config.save();
        }
    }

    private void saveConfig() {
        if (config.hasChanged()) {
            config.save();
        }
    }

}
