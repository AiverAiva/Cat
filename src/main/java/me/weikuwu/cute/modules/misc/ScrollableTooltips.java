package me.weikuwu.cute.modules.misc;

import gg.essential.universal.UKeyboard;
import gg.essential.universal.UMatrixStack;
import me.weikuwu.cute.config.Config;
import org.lwjgl.input.Mouse;

import java.util.List;

public class ScrollableTooltips {
    public static int scrollY = 0;
    public static boolean allowScrolling;
    public static int scrollX = 0;
    public static double zoomFactor = 1.0;

    public static void drawHoveringText(UMatrixStack matrixStack, List<String> textLines, int screenHeight, int tooltipY, int tooltipHeight) {
        if (!allowScrolling) {
            scrollX = 0;
            if (tooltipY < 0 && Config.startAtTop) {
                scrollY = -tooltipY + 6;
            } else {
                scrollY = 0;
            }
            zoomFactor = 1.0;
        }

        if (!Config.masterToggle) return;
        allowScrolling = tooltipY < 0;
        if (allowScrolling) {
            int eventDWheel = Mouse.getDWheel();
            if (UKeyboard.isCtrlKeyDown() && Config.zoom) {
                zoomFactor *= (1.0 + 0.1 * Integer.signum(eventDWheel));
            } else if (UKeyboard.isShiftKeyDown() && Config.horizontalScrolling) {
                if (eventDWheel < 0) {
                    scrollX += 10;
                } else if (eventDWheel > 0) {
                    scrollX -= 10;
                }
            } else if (Config.verticalScrolling) {
                if (eventDWheel < 0) {
                    scrollY -= 10;
                } else if (eventDWheel > 0) {
                    scrollY += 10;
                }
            }

            if (scrollY + tooltipY > 6) {
                scrollY = -tooltipY + 6;
            } else if (scrollY + tooltipY + tooltipHeight + 6 < screenHeight) {
                scrollY = screenHeight - 6 - tooltipY - tooltipHeight;
            }
        }

        matrixStack.translate(scrollX, scrollY, 0);
        matrixStack.scale(zoomFactor, zoomFactor, 0.0);
        matrixStack.applyToGlobalState();
    }
}