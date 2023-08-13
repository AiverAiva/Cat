package me.weikuwu.cute.guis.config.elements;

import me.weikuwu.cute.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class Button extends GuiButton {
    private static final int DEFAULT_COLOR = 0xFFAAAAAA;
    private static final int HOVER_COLOR = 0xFFCF8E8E;
    private static final long ANIMATION_DURATION = 500;

    private long animationStartTime;

    public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    private static int interpolateColor(int startColor, int endColor, float progress) {
        int[] startColors = {startColor >> 24, (startColor >> 16) & 0xFF, (startColor >> 8) & 0xFF, startColor & 0xFF};
        int[] endColors = {endColor >> 24, (endColor >> 16) & 0xFF, (endColor >> 8) & 0xFF, endColor & 0xFF};
        int[] interpolatedColors = new int[4];

        for (int i = 0; i < 4; i++) {
            interpolatedColors[i] = (int) (startColors[i] + (endColors[i] - startColors[i]) * progress);
        }

        return (interpolatedColors[0] << 24) | (interpolatedColors[1] << 16) | (interpolatedColors[2] << 8) | interpolatedColors[3];
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) return;

        boolean isMouseOverButton = isMouseOver(mouseX, mouseY);
        float progress = calculateAnimationProgress();

        int color = interpolateColor(isMouseOverButton ? HOVER_COLOR : DEFAULT_COLOR, DEFAULT_COLOR, progress);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);

        String buttonText = this.displayString;
        double textWidth = Fonts.Inter.getStringWidth(buttonText);
        double textX = this.xPosition + (this.width - textWidth) / 2;
        int textY = this.yPosition + (this.height - 8) / 2;
        ConfigInput.drawStringWithDoubles(buttonText, textX, textY);
    }

    private float calculateAnimationProgress() {
        long elapsedTime = System.currentTimeMillis() - animationStartTime;
        boolean isAnimationRunning = elapsedTime < ANIMATION_DURATION;
        return isAnimationRunning ? (float) elapsedTime / ANIMATION_DURATION : 1.0f;
    }

    public void startAnimation() {
        animationStartTime = System.currentTimeMillis();
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

}
