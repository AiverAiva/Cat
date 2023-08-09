package me.weikuwu.cute.guis;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.config.ConfigManager;
import me.weikuwu.cute.config.settings.Setting;
import me.weikuwu.cute.config.types.*;
import me.weikuwu.cute.config.types.Boolean;
import me.weikuwu.cute.guis.elements.*;
import me.weikuwu.cute.utils.font.Fonts;
import me.weikuwu.cute.utils.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public class CatGUI extends GuiScreen {
    public static ArrayList<Setting> settings = new ArrayList<>();
    private final int columnWidth = 300;
    private final int headerHeight = 100 + 9;
    private int prevMouseY;
    private int scrollOffset = 0;
    private boolean scrolling = false;
    private Integer prevWidth = null;
    private Integer prevHeight = null;

    public CatGUI() {
        settings = getFilteredSettings();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Dark Background
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        mouseMoved(mouseY);

        // Settings
        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);

            int x = getOffset();
            int y = headerHeight + (i * 15) - scrollOffset;

            x += setting.getIndent(0);

            // Setting Border
//            if (setting.parent == null && i > 0) {
//                drawRect(x, y - 3, getOffset() + columnWidth, y - 2, ConfigInput.transparent.getRGB());
//            }

            // Setting Text
            char color = 'f'; // White
            if (setting instanceof Boolean && setting.get(java.lang.Boolean.class)) color = 'a'; // Green
//            if (setting instanceof Folder && ((Folder) setting).isChildEnabled()) color = 'a'; // Green

            Fonts.Inter.drawString("§" + color + setting.name, x, y + 1, 0xFFFFFFFF);
            if (setting.note != null) {
                int settingNameWidth = (int) Fonts.Inter.getStringWidth(setting.name + " ");
                GlStateManager.translate(0, 1.8, 0);
                Fonts.Description.drawString("§7" + setting.note, x + settingNameWidth, y, 0xFFFFFFFF);
                GlStateManager.translate(0, -1.8, 0);
            }
        }

        prevWidth = width;
        prevHeight = height;
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

        int viewport = height - headerHeight - 9;
        int contentHeight = settings.size() * 15;
        int scrollbarX = getOffset() + columnWidth + 10;

        ScrollBar scrollbar = new ScrollBar(headerHeight, viewport, contentHeight, scrollOffset, scrollbarX, scrolling);
        buttonList.add(scrollbar);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button instanceof ScrollBar) {
            scrolling = true;
        } else {
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

    // pixels - Whether to scroll that amount, or to convert to a percentage
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

            if (setting.parent == null) {
                newSettings.add(setting);
            } else {
                if (newSettings.contains(setting.parent) && setting.parent.get(java.lang.Boolean.class)) {
                    newSettings.add(setting);
                }
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
//    private static final float ASPECT_RATIO_WIDTH = 16f;
//    private static final float ASPECT_RATIO_HEIGHT = 9f;
//
//    private CatRenderer guiRenderer;
//
//    @Override
//    public void initGui() {
//        this.buttonList.add(new Button(1, this.width / 2 - 50, this.height / 2 - 20, 100, 40, "Click Me!"));
//    }
//
//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        drawDefaultBackground();
//
//        if (guiRenderer == null) {
//            guiRenderer = new CatRenderer(mc);
//        }
//        guiRenderer.renderGui(mouseX, mouseY, partialTicks);
//
//        super.drawScreen(mouseX, mouseY, partialTicks);
//    }



//
//    @Override
//    protected void keyTyped(char typedChar, int keyCode) throws IOException {
//        if (textField.textboxKeyTyped(typedChar, keyCode)) {
//            // what happens here?
//        } else {
//            super.keyTyped(typedChar, keyCode);
//        }
//    }
//
//    @Override
//    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//        super.mouseClicked(mouseX, mouseY, mouseButton);
//
//        textField.mouseClicked(mouseX, mouseY, mouseButton);
//    }
//
//    @Override
//    protected void actionPerformed(GuiButton button) {
//        if (guiRenderer != null) {
//            guiRenderer.handleActions(button.id);
//        }
//    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        ConfigManager.save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}

//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        drawDefaultBackground();
//        super.drawScreen(mouseX, mouseY, partialTicks);
//
//        int guiWidth = 1920;
//        int guiHeight = 1080;
//        int screenWidth = Minecraft.getMinecraft().displayWidth;
//        int screenHeight = Minecraft.getMinecraft().displayHeight;
//        double scaleFactor = 1920.0 / screenWidth;
//        int rectWidth = (int) (100 * scaleFactor);
//        int rectHeight = (int) (50 * scaleFactor);
//        int rectX = (screenWidth - rectWidth) / 2;
//        int rectY = (screenHeight - rectHeight) / 2;
//        Fonts.Inter.drawString("Custom GUI", width / 2, 20, 0xFFFFFFFF);
//        textField.drawTextBox();
//    }
