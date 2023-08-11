package me.weikuwu.cute.guis;


import com.google.common.eventbus.Subscribe;
import me.weikuwu.cute.events.RenderEvent;
import me.weikuwu.cute.events.ScreenOpenEvent;
import me.weikuwu.cute.events.Stage;
import me.weikuwu.cute.handlers.BlurHandler;
import me.weikuwu.cute.mixin.ShaderGroupAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Blur implements BlurHandler {
    private final ResourceLocation blurShader = new ResourceLocation("shaders/fade_in_blur.json");
    private final Logger logger = LogManager.getLogger("Catmod - blur");
    private long start;
    private float progress = 0;

    @SubscribeEvent
    private void onGuiChange(ScreenOpenEvent event) {
        reloadBlur(event.screen);
    }

    @SubscribeEvent
    private void onRenderTick(RenderEvent event) {
        if (event.stage != Stage.END) {
            return;
        }

        if (Minecraft.getMinecraft().currentScreen == null) {
            return;
        }

        if (!isShaderActive()) {
            return;
        }
        if (progress >= 5) return;
        progress = getBlurStrengthProgress();

        // This is hilariously bad, and could cause frame issues on low-end computers.
        // Why is this being computed every tick? Surely there is a better way?
        // This needs to be optimized.
        try {
            final List<Shader> listShaders = ((ShaderGroupAccessor) Minecraft.getMinecraft().entityRenderer.getShaderGroup()).getListShaders();

            // Should not happen. Something bad happened.
            if (listShaders == null) {
                return;
            }

            // Iterate through the list of shaders.
            for (Shader shader : listShaders) {
                ShaderUniform su = shader.getShaderManager().getShaderUniform("Progress");

                if (su == null) {
                    continue;
                }

                // All this for this.
                su.set(progress);
            }
        } catch (IllegalArgumentException ex) {
            this.logger.error("An error.png occurred while updating OneConfig's blur. Please report this!", ex);
        }
    }

    public void reloadBlur(Object gui) {
        // Don't do anything if no world is loaded
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        // If a shader is not already active and the UI is
        // a one of ours, we should load our own blur!

        if (!isShaderActive() && (gui instanceof BlurScreen && ((BlurScreen) gui).hasBackgroundBlur())) {
            //#if FABRIC==1
            //$$ ((GameRendererAccessor) UMinecraft.getMinecraft().gameRenderer).invokeLoadShader(this.blurShader);
            //#else
            Minecraft.getMinecraft().entityRenderer.loadShader(this.blurShader);
            //#endif

            this.start = System.currentTimeMillis();
            this.progress = 0;

            // If a shader is active and the incoming UI is null or we have blur disabled, stop using the shader.
        } else if (isShaderActive() && (gui == null || (gui instanceof BlurScreen && ((BlurScreen) gui).hasBackgroundBlur()))) {
            String name = Minecraft.getMinecraft().entityRenderer.getShaderGroup().getShaderGroupName();

            // Only stop our specific blur ;)
            if (!name.endsWith("fade_in_blur.json")) {
                return;
            }

            Minecraft.getMinecraft().entityRenderer.stopUseShader();
        }
    }

    private float getBlurStrengthProgress() {
        return Math.min((System.currentTimeMillis() - this.start) / 50F, 5.0F);
    }

    private boolean isShaderActive() {
        return Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null
                //#if MC<=11202
                && net.minecraft.client.renderer.OpenGlHelper.shadersSupported
                //#endif
                ;
    }
}
