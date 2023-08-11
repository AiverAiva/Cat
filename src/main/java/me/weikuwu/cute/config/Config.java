package me.weikuwu.cute.config;

import me.weikuwu.cute.config.settings.Property;

public class Config {

    @Property(type = Property.Type.BOOLEAN, name = "Show Candies", note="Show candies on pet.")
    public static boolean showCandies = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Close Secret Chests")
    public static boolean closeSecretChests = false;

    @Property(type = Property.Type.NUMBER, name = "Close Delay", parent = "Auto Close Secret Chests", max = 20, step = 1, suffix = " tick")
    public static int closeSecretChestsDelay = 1;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Fish")
    public static boolean autofish = false;

    @Property(type = Property.Type.NUMBER, name = "Recast Delay", parent = "Auto Fish", max = 1000, step = 50, suffix = " ms")
    public static int recastDelay = 350;

    @Property(type = Property.Type.NUMBER, name = "Aim Speed", parent = "Auto Fish", max = 1000, step = 50, suffix = " ms")
    public static int aimSpeed = 250;

    @Property(type = Property.Type.NUMBER, name = "Hyperion Damage", parent = "Auto Fish", max = 3000000, step = 250000)
    public static int hypDamage = 1000000;

    @Property(type = Property.Type.NUMBER, name = "Sea Creature Range", parent = "Auto Fish", max = 10, step = 1, suffix = " blocks")
    public static int scRange = 6;

    @Property(type = Property.Type.CHECKBOX, name = "Insta-kill Sea Creatures", parent = "Auto Fish")
    public static boolean killPrio = true;

    @Property(type = Property.Type.CHECKBOX, name = "Anti AFK", parent = "Auto Fish")
    public static boolean antiAfk = true;

    @Property(type = Property.Type.CHECKBOX, name = "Slug Mode", parent = "Auto Fish")
    public static boolean slugMode = false;

    @Property(type = Property.Type.CHECKBOX, name = "Sneak", parent = "Auto Fish")
    public static boolean sneak = false;

    @Property(type = Property.Type.CHECKBOX, name = "Debug Mode", parent = "Auto Fish")
    public static boolean debugMode = false;

//    @Setting(name = "PetSwap", description = "Activates PetSwap on bobber in water")
//    private BooleanSetting petSwap;
//    @Setting(name = "AssistMode", description = "Will only reel in.")
//    private BooleanSetting assistMode;

}
