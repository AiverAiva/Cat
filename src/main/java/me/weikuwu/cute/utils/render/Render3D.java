package me.weikuwu.cute.utils.render;

import me.weikuwu.cute.mixin.RenderManagerAccessor;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.world.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.entity.*;
import java.awt.*;
import java.util.*;

public class Render3D {
    private static Minecraft mc;

    public static void renderBoundingBox(final Entity entity, final float partialTicks, final int color) {
        final RenderManagerAccessor rm = (RenderManagerAccessor)Render3D.mc.getRenderManager();
        final double renderPosX = rm.getRenderPosX();
        final double renderPosY = rm.getRenderPosY();
        final double renderPosZ = rm.getRenderPosZ();
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - renderPosZ;
        final AxisAlignedBB bbox = entity.getEntityBoundingBox();
        AxisAlignedBB aabb = new AxisAlignedBB(bbox.minX - entity.posX + x, bbox.minY - entity.posY + y, bbox.minZ - entity.posZ + z, bbox.maxX - entity.posX + x, bbox.maxY - entity.posY + y, bbox.maxZ - entity.posZ + z);
        if (entity instanceof EntityArmorStand) {
            aabb = aabb.expand(0.3, 2.0, 0.3);
        }
        drawFilledBoundingBox(aabb, color);
    }

    public static void drawFilledBoundingBox(final AxisAlignedBB aabb, final int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
//        final ESP esp = (ESP)CF4M.INSTANCE.moduleManager.getModule("ESP");
//        final float opacity = esp.boxOpacity.getCurrent();
        final float opacity = 0.5F;
        GlStateManager.color(r, g, b, a * opacity);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();
        GlStateManager.color(r, g, b, a * opacity);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();
        GlStateManager.color(r, g, b, a * opacity);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        tessellator.draw();
        GlStateManager.color(r, g, b, a);
        RenderGlobal.drawSelectionBoundingBox(aabb);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
