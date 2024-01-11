package me.weikuwu.cute;

import me.weikuwu.cute.commands.OpenConfig;
import me.weikuwu.cute.config.Config;
import me.weikuwu.cute.config.ConfigManager;
import me.weikuwu.cute.config.settings.Setting;
import me.weikuwu.cute.events.TickEndEvent;
import me.weikuwu.cute.handlers.BlurHandler;
import me.weikuwu.cute.handlers.KeyInputHandler;
import me.weikuwu.cute.modules.dungeons.GhostPickaxe;
import me.weikuwu.cute.modules.misc.DeveloperMode;
import me.weikuwu.cute.modules.misc.DiscordRPC;
import me.weikuwu.cute.modules.dungeons.AutoCloseChest;
import me.weikuwu.cute.modules.macros.AutoFish;
import me.weikuwu.cute.modules.misc.ShowCandies;
import me.weikuwu.cute.utils.remote.CapeLoader;
import me.weikuwu.cute.utils.font.Fonts;
import me.weikuwu.cute.utils.skyblock.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.util.ArrayList;

@Mod(modid = "catmod", clientSideOnly = true)
public class CatMod {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static GuiScreen gui = null;

    public static final File dir = new File(new File(mc.mcDataDir, "config"), "Cat");
    public static ArrayList<Setting> settings = new ArrayList<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (!dir.exists()) dir.mkdirs();
        settings = ConfigManager.collect(Config.class);
        ConfigManager.load();
        Fonts.bootstrap();
        DiscordRPC.start();
        KeyBindings.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new OpenConfig());
        MinecraftForge.EVENT_BUS.register(this);
        EventManager.registerEvents(
                new Location(),
                new TickEndEvent(),
                new AutoCloseChest(),
                new AutoFish(),
                new KeyInputHandler(),
                new ShowCandies(),
                new DeveloperMode(),
                new GhostPickaxe(),
                BlurHandler.INSTANCE
        );
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CapeLoader.load();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (gui != null) {
            mc.displayGuiScreen(gui);
            gui = null;
        }
    }
}
