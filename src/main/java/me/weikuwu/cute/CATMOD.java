package me.weikuwu.cute;

// import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import me.weikuwu.cute.commands.Config;
import me.weikuwu.cute.utils.fonts.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "catmod", name = "catmod", version = "1.0.0", clientSideOnly = true)
public class CATMOD {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static GuiScreen gui = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // DiscordRPC.start();
        Fonts.bootstrap();
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new Config());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (gui != null) {
            mc.displayGuiScreen(gui);
            gui = null;
        }
    }
}
