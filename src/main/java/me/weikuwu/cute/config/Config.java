package me.weikuwu.cute.config;

import me.weikuwu.cute.config.settings.Property;

public class Config {
    @Property(
            name = "Scrollable Tooltips", note = "u scroll",
            type = Property.Type.BOOLEAN
    )
    public static boolean masterToggle = true;

    @Property(
            name = "Horizontal Scrolling", note = "Turns horizontal scrolling on/off.", parent = "Scrollable Tooltips",
            type = Property.Type.BOOLEAN
    )
    public static boolean horizontalScrolling = true;

    @Property(
            name = "Vertical Scrolling", note = "Turns vertical scrolling on/off.", parent = "Scrollable Tooltips",
            type = Property.Type.BOOLEAN
    )
    public static boolean verticalScrolling = true;

    @Property(
            name = "Tooltip Zooming", note = "Turns zooming on tooltips on/off.", parent = "Scrollable Tooltips",
            type = Property.Type.BOOLEAN
    )
    public static boolean zoom = true;

    @Property(
            name = "Start at the Top of Tooltips", note = "Changes tooltips to always show the top.", parent = "Scrollable Tooltips",
            type = Property.Type.BOOLEAN
    )
    public static boolean startAtTop = true;

    @Property(type = Property.Type.BOOLEAN, name = "Show Candies", note = "Show candies on pet.")
    public static boolean showCandies = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Armor")
    public static boolean hideArmor = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Close Secret Chests")
    public static boolean closeSecretChests = false;

    @Property(type = Property.Type.NUMBER, name = "Close Delay", parent = "Auto Close Secret Chests", max = 20, suffix = " ticks")
    public static int closeSecretChestsDelay = 1;

    @Property(type = Property.Type.BOOLEAN, name = "Ghost Pickaxe", note = "Create ghost blocks when breaking.")
    public static boolean ghostPickaxe = false;

    @Property(type = Property.Type.BOOLEAN, name = "On Shift", parent = "Ghost Pickaxe")
    public static boolean ghostPickaxeOnShift = false;

    @Property(type = Property.Type.BOOLEAN, name = "Only in Dungeons", parent = "Ghost Pickaxe")
    public static boolean ghostPickaxeOnlyInDungeon = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Fish")
    public static boolean autoFish = false;

    @Property(type = Property.Type.NUMBER, name = "Recast Delay", parent = "Auto Fish", max = 1000, step = 50, suffix = " ms")
    public static int recastDelay = 350;

    @Property(type = Property.Type.NUMBER, name = "Aim Speed", parent = "Auto Fish", max = 1000, step = 50, suffix = " ms")
    public static int aimSpeed = 250;

    @Property(type = Property.Type.NUMBER, name = "Hyperion Damage", parent = "Auto Fish", max = 3000000, step = 250000)
    public static int hypDamage = 1000000;

    @Property(type = Property.Type.NUMBER, name = "Sea Creature Range", parent = "Auto Fish", max = 10, suffix = " blocks")
    public static int scRange = 6;

    @Property(type = Property.Type.CHECKBOX, name = "Instakill Sea Creatures", parent = "Auto Fish")
    public static boolean killPrio = true;

    @Property(type = Property.Type.CHECKBOX, name = "Anti AFK", parent = "Auto Fish")
    public static boolean antiAfk = true;

    @Property(type = Property.Type.CHECKBOX, name = "Slug Mode", parent = "Auto Fish")
    public static boolean slugMode = false;

    @Property(type = Property.Type.CHECKBOX, name = "Sneak", parent = "Auto Fish")
    public static boolean sneak = false;

    @Property(type = Property.Type.CHECKBOX, name = "Debug Mode", parent = "Auto Fish")
    public static boolean debugMode = false;

    @Property(type = Property.Type.BOOLEAN, name = "Developer Mode", note = "weiku is cute")
    public static boolean devMode = false;
/*
    @Setting(name = "PetSwap", description = "Activates PetSwap on bobber in water")
    private BooleanSetting petSwap;
    @Setting(name = "AssistMode", description = "Will only reel in.")
    private BooleanSetting assistMode;
*/

}
