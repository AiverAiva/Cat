package me.weikuwu.cute.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import me.weikuwu.cute.utils.font.*;
import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
    public static MinecraftFontRenderer Inter;
    public static MinecraftFontRenderer Description;
    public static MinecraftFontRenderer Title;

    private Fonts() {
    }

    private static Font getFont(final Map<String, Font> locationMap, final String location, final int size, final int style) {
        Font font;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(style, (float) size);
            } else {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("catmod", "fonts/" + location)).getInputStream();
                font = Font.createFont(style, is);
                locationMap.put(location, font);
                font = font.deriveFont(style, (float) size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", style, size);
        }
        return font;
    }

    public static void bootstrap() {
        final Map<String, Font> locationMap = new HashMap<>();
        Fonts.Inter = new MinecraftFontRenderer(getFont(locationMap, "Inter-VariableFont.ttf", 18, 0), true, false);
        Fonts.Description = new MinecraftFontRenderer(getFont(locationMap, "Inter-VariableFont.ttf", 16, 0), true, false);
    }
}
