package me.weikuwu.cute;

import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import me.weikuwu.cute.commands.OpenConfig;
import me.weikuwu.cute.modules.discord.DiscordRPC;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "catmod", clientSideOnly = true)
public class CatMod {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static GuiScreen gui = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws NoDiscordClientException {
        Fonts.bootstrap();
        DiscordRPC.start();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new OpenConfig());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (gui != null) {
            mc.displayGuiScreen(gui);
            gui = null;
        }
    }
}
