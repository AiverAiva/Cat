package me.weikuwu.cute.guis;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.events.RenderEvent;
import me.weikuwu.cute.events.ScreenOpenEvent;
import me.weikuwu.cute.events.Stage;
import me.weikuwu.cute.handlers.BlurHandler;
import me.weikuwu.cute.mixins.ShaderGroupAccessor;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.OpenGlHelper;

import java.util.List;

public class Blur implements BlurHandler {
    private final ResourceLocation blurShader = new ResourceLocation("catmod:shaders/post/fade_in_blur.json");
    private long start;
    private float progress = 0;

    @SubscribeEvent
    public void onGuiChange(ScreenOpenEvent event) {
        reloadBlur(event.screen);
    }

    @SubscribeEvent
    public void onRenderTick(RenderEvent event) {
        if (event.stage != Stage.END || CatMod.mc.currentScreen == null || !isShaderActive() || progress >= 5) {
            return;
        }

        try {
            final List<Shader> listShaders = ((ShaderGroupAccessor) CatMod.mc.entityRenderer.getShaderGroup()).getListShaders();
            if (listShaders != null) {
                for (Shader shader : listShaders) {
                    ShaderUniform su = shader.getShaderManager().getShaderUniform("Progress");
                    if (su != null) {
                        su.set(progress);
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("An error occurred while updating blur");
        }
    }

    public void reloadBlur(Object gui) {
        if (CatMod.mc.theWorld == null) {
            return;
        }

        if (!isShaderActive() && gui instanceof BlurScreen && ((BlurScreen) gui).hasBackgroundBlur()) {
            CatMod.mc.entityRenderer.loadShader(blurShader);
            start = System.currentTimeMillis();
            progress = 0;
        } else if (isShaderActive() && (gui == null || (gui instanceof BlurScreen && ((BlurScreen) gui).hasBackgroundBlur()))) {
            String name = CatMod.mc.entityRenderer.getShaderGroup().getShaderGroupName();
            if (name.endsWith("fade_in_blur.json")) {
                CatMod.mc.entityRenderer.stopUseShader();
            }
        }
    }

    private float getBlurStrengthProgress() {
        return Math.min((System.currentTimeMillis() - start) / 50F, 5.0F);
    }

    private boolean isShaderActive() {
        return CatMod.mc.entityRenderer.getShaderGroup() != null && OpenGlHelper.shadersSupported;
    }
}
