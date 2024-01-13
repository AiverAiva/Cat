package me.weikuwu.cute.config;

import me.weikuwu.cute.config.settings.Property;

public class Config {
    @Property(
            name = "Scrollable Tooltips",
            note = "u scroll",
            type = Property.Type.BOOLEAN
    )
    public static boolean masterToggle = true;

    @Property(
            parent = "Scrollable Tooltips",
            name = "Horizontal Scrolling",
            note = "Turns horizontal scrolling on/off.",
            type = Property.Type.BOOLEAN
    )
    public static boolean horizontalScrolling = true;

    @Property(
            parent = "Scrollable Tooltips",
            name = "Vertical Scrolling",
            note = "Turns vertical scrolling on/off.",
            type = Property.Type.BOOLEAN
    )
    public static boolean verticalScrolling = true;

    @Property(
            parent = "Scrollable Tooltips",
            name = "Tooltip Zooming",
            note = "Turns zooming on tooltips on/off.",
            type = Property.Type.BOOLEAN
    )
    public static boolean zoom = true;

    @Property(
            parent = "Scrollable Tooltips",
            name = "Start at the Top of Tooltips",
            note = "Changes tooltips to always show the top.",
            type = Property.Type.BOOLEAN
    )
    public static boolean startAtTop = true;

    @Property(
            name = "Show Candies",
            note = "Show candies on pet.",
            type = Property.Type.BOOLEAN
    )
    public static boolean showCandies = false;

    @Property(
            name = "Hide Armor",
            type = Property.Type.BOOLEAN
    )
    public static boolean hideArmor = false;

    @Property(
            name = "Auto Close Secret Chests",
            type = Property.Type.BOOLEAN
    )
    public static boolean closeSecretChests = false;

    @Property(
            parent = "Auto Close Secret Chests",
            name = "Close Delay",
            max = 20, suffix = " ticks",
            type = Property.Type.NUMBER
    )
    public static int closeSecretChestsDelay = 1;

    @Property(
            name = "Ghost Pickaxe",
            note = "Create ghost blocks when breaking.",
            type = Property.Type.BOOLEAN
    )
    public static boolean ghostPickaxe = false;

    @Property(
            parent = "Ghost Pickaxe",
            name = "On Shift",
            type = Property.Type.BOOLEAN
    )
    public static boolean ghostPickaxeOnShift = false;

    @Property(
            parent = "Ghost Pickaxe",
            name = "Only in Dungeons",
            type = Property.Type.BOOLEAN
    )
    public static boolean ghostPickaxeOnlyInDungeon = false;

    @Property(
            name = "Auto Fish",
            type = Property.Type.BOOLEAN
    )
    public static boolean autoFish = false;

    @Property(
            parent = "Auto Fish",
            name = "Recast Delay",
            max = 1000, step = 50, suffix = " ms",
            type = Property.Type.NUMBER
    )
    public static int recastDelay = 350;

    @Property(
            parent = "Auto Fish",
            name = "Aim Speed",
            max = 1000, step = 50, suffix = " ms",
            type = Property.Type.NUMBER
    )
    public static int aimSpeed = 250;

    @Property(
            parent = "Auto Fish",
            name = "Hyperion Damage",
            max = 3000000, step = 250000,
            type = Property.Type.NUMBER
    )
    public static int hypDamage = 1000000;

    @Property(
            parent = "Auto Fish",
            name = "Sea Creature Range",
            max = 10, suffix = " blocks",
            type = Property.Type.NUMBER
    )
    public static int scRange = 6;

    @Property(
            parent = "Auto Fish",
            name = "Instakill Sea Creatures",
            type = Property.Type.CHECKBOX
    )
    public static boolean killPrio = true;

    @Property(
            parent = "Auto Fish",
            name = "Anti AFK",
            type = Property.Type.CHECKBOX
    )
    public static boolean antiAfk = true;

    @Property(
            parent = "Auto Fish",
            type = Property.Type.CHECKBOX,
            name = "Slug Mode"
    )
    public static boolean slugMode = false;

    @Property(
            parent = "Auto Fish",
            name = "Sneak",
            type = Property.Type.CHECKBOX
    )
    public static boolean sneak = false;

    @Property(
            parent = "Auto Fish",
            name = "Debug Mode",
            type = Property.Type.CHECKBOX
    )
    public static boolean debugMode = false;

    @Property(
            name = "Developer Mode",
            note = "weiku is cute",
            type = Property.Type.BOOLEAN
    )
    public static boolean devMode = false;
/*
    @Setting(name = "PetSwap", description = "Activates PetSwap on bobber in water")
    private BooleanSetting petSwap;
    @Setting(name = "AssistMode", description = "Will only reel in.")
    private BooleanSetting assistMode;
*/

}
