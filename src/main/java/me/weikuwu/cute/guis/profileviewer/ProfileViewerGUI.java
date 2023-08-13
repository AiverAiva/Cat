package me.weikuwu.cute.guis.profileviewer;

import me.weikuwu.cute.events.*;
import me.weikuwu.cute.guis.BlurScreen;
import net.minecraft.client.gui.*;
import net.minecraftforge.common.MinecraftForge;


public class ProfileViewerGUI extends GuiScreen implements BlurScreen {

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
