package me.weikuwu.cute.guis.config;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.config.ConfigManager;
import me.weikuwu.cute.config.settings.Setting;
import me.weikuwu.cute.config.types.Boolean;
import me.weikuwu.cute.events.RenderEvent;
import me.weikuwu.cute.events.ScreenOpenEvent;
import me.weikuwu.cute.events.Stage;
import me.weikuwu.cute.guis.BlurScreen;
import me.weikuwu.cute.guis.config.elements.ConfigInput;
import me.weikuwu.cute.guis.config.elements.ScrollBar;
import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class CatGUI extends GuiScreen implements BlurScreen {
    public static ArrayList<Setting> settings = new ArrayList<>();
    private final int columnWidth = 300;
    private final int headerHeight = 100 + 9;
    private int prevMouseY;
    private int scrollOffset = 0;
    private boolean scrolling = false;

    public CatGUI() {
        settings = getFilteredSettings();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent(Stage.START, partialTicks));
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        mouseMoved(mouseY);

        int x = getOffset();
        int viewport = height - headerHeight - 9;
        int contentHeight = settings.size() * 15;
        int scrollbarX = x + columnWidth + 10;
        int y = headerHeight - scrollOffset;

        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            x += setting.getIndent(0);
            char color = 'f';
            if (setting instanceof Boolean && setting.get(java.lang.Boolean.class)) color = 'a';

            Fonts.Inter.drawString("§" + color + setting.name, x, y + (i * 15) + 1, 0xFFFFFFFF);
            if (setting.note != null) {
                int settingNameWidth = (int) Fonts.Inter.getStringWidth(setting.name + " ");
                GlStateManager.translate(0, 1.8, 0);
                Fonts.Description.drawString("§7" + setting.note, x + settingNameWidth, y + (i * 15), 0xFFFFFFFF);
                GlStateManager.translate(0, -1.8, 0);
            }
        }

        ScrollBar scrollbar = new ScrollBar(headerHeight, viewport, contentHeight, scrollOffset, scrollbarX, scrolling);
        buttonList.add(scrollbar);

        MinecraftForge.EVENT_BUS.post(new RenderEvent(Stage.END, partialTicks));
    }

    @Override
    public void initGui() {
        buttonList.clear();
        int x = getOffset() + columnWidth;
        int y = headerHeight - scrollOffset;

        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            buttonList.add(ConfigInput.buttonFromSetting(setting, x, y + (i * 15)));
        }

        MinecraftForge.EVENT_BUS.post(new ScreenOpenEvent(this));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        scrolling = button instanceof ScrollBar;
        if (!scrolling) {
            settings.clear();
            settings = getFilteredSettings();
        }
        initGui();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        scrolling = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    private void scrollScreen(int scrollAmount, boolean pixels) {
        int viewport = height - headerHeight - 9;
        int contentHeight = settings.size() * 15;
        if (!pixels) scrollAmount = (int) ((scrollAmount / (float) viewport) * contentHeight);
        if (contentHeight > viewport) {
            scrollOffset = MathHelper.clamp_int(scrollOffset + scrollAmount, 0, contentHeight - viewport);
            initGui();
        }
    }

    private void mouseMoved(int mouseY) {
        if (scrolling) scrollScreen(mouseY - prevMouseY, false);
        prevMouseY = mouseY;
    }

    private ArrayList<Setting> getFilteredSettings() {
        ArrayList<Setting> newSettings = new ArrayList<>();
        for (Setting setting : CatMod.settings) {
            if (setting.parent == null || (newSettings.contains(setting.parent) && setting.parent.get(java.lang.Boolean.class))) {
                newSettings.add(setting);
            }
        }
        return newSettings;
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (Mouse.getEventDWheel() != 0) {
            scrollScreen(Integer.signum(Mouse.getEventDWheel()) * -10, true);
        }
        super.handleMouseInput();
    }

    private int getOffset() {
        return (width - columnWidth) / 2;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        MinecraftForge.EVENT_BUS.post(new ScreenOpenEvent(null));
        ConfigManager.save();
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
