package me.weikuwu.cute.guis;

import me.weikuwu.cute.utils.fonts.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class Button extends GuiButton {
    private static final int DEFAULT_COLOR = 0xFFAAAAAA;
    private static final int HOVER_COLOR = 0xFFCF8E8E;
    private static final long ANIMATION_DURATION = 500;

    private long animationStartTime;
    private boolean isAnimationRunning;

    public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.isAnimationRunning = false;
    }

    private static int interpolateColor(int endColor, float progress) {
        int startAlpha = (Button.DEFAULT_COLOR >> 24) & 0xFF;
        int startRed = (Button.DEFAULT_COLOR >> 16) & 0xFF;
        int startGreen = (Button.DEFAULT_COLOR >> 8) & 0xFF;
        int startBlue = Button.DEFAULT_COLOR & 0xFF;

        int endAlpha = (endColor >> 24) & 0xFF;
        int endRed = (endColor >> 16) & 0xFF;
        int endGreen = (endColor >> 8) & 0xFF;
        int endBlue = endColor & 0xFF;

        int alpha = (int) (startAlpha + (endAlpha - startAlpha) * progress);
        int red = (int) (startRed + (endRed - startRed) * progress);
        int green = (int) (startGreen + (endGreen - startGreen) * progress);
        int blue = (int) (startBlue + (endBlue - startBlue) * progress);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            boolean isMouseOverButton = mouseX >= this.xPosition && mouseY >= this.yPosition
                    && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            if (isAnimationRunning) {
                float progress = calculateAnimationProgress();
                int color = interpolateColor(isMouseOverButton ? HOVER_COLOR : DEFAULT_COLOR, progress);
                drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);
            } else {
                int color = isMouseOverButton ? HOVER_COLOR : DEFAULT_COLOR;
                drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);
            }

            String buttonText = this.displayString;
            double textWidth = Fonts.Inter.getStringWidth(buttonText);
            double textX = this.xPosition + (this.width - textWidth) / 2;
            int textY = this.yPosition + (this.height - 8) / 2;
            Fonts.Inter.drawString(buttonText, textX, textY, 0xFFFFFFFF);
        }
    }

    private float calculateAnimationProgress() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - animationStartTime;

        if (elapsedTime >= ANIMATION_DURATION) {
            isAnimationRunning = false;
            return 1.0f;
        } else {
            return (float) elapsedTime / ANIMATION_DURATION;
        }
    }

    public void startAnimation() {
        animationStartTime = System.currentTimeMillis();
        isAnimationRunning = true;
    }
}
