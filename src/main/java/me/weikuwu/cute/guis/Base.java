package me.weikuwu.cute.guis;

import me.weikuwu.cute.events.RenderEvent;
import me.weikuwu.cute.events.ScreenOpenEvent;
import me.weikuwu.cute.events.Stage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;


public class Base extends GuiScreen implements BlurScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent(Stage.START, partialTicks));
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        MinecraftForge.EVENT_BUS.post(new RenderEvent(Stage.END, partialTicks));
    }

    @Override
    public void initGui() {
        MinecraftForge.EVENT_BUS.post(new ScreenOpenEvent(this));
    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        MinecraftForge.EVENT_BUS.post(new ScreenOpenEvent(null));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public boolean hasBackgroundBlur() {
        return true;
    }
}
