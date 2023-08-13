package me.weikuwu.cute.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
    public static MinecraftFontRenderer Inter;
    public static MinecraftFontRenderer Description;

    private Fonts() {
    }

    private static Font getFont(final Map<String, Font> locationMap, final String location, final int size, final int style) {
        try {
            if (!locationMap.containsKey(location)) {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("catmod", "fonts/" + location)).getInputStream();
                Font font = Font.createFont(style, is);
                locationMap.put(location, font);
            }
            return locationMap.get(location).deriveFont(style, (float) size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("Error loading font: " + e);
            return new Font("default", style, size);
        }
    }

    public static void bootstrap() {
        Map<String, Font> locationMap = new HashMap<>();
        Inter = new MinecraftFontRenderer(getFont(locationMap, "Inter-VariableFont.ttf", 18, 0), true, false);
        Description = new MinecraftFontRenderer(getFont(locationMap, "Inter-VariableFont.ttf", 16, 0), true, false);
    }
}
