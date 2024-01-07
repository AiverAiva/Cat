package me.weikuwu.cute.mixins;

import me.weikuwu.cute.config.Config;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerCustomHead.class)
public class MixinLayerCustomHead {


    @Inject(method = "doRenderLayer", at = @At("HEAD"), cancellable = true)
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        if(entitylivingbaseIn instanceof EntityPlayerSP && Config.hideArmor) ci.cancel();
    }


}
