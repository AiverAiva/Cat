package me.weikuwu.cute.utils.fonts;


import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
    public static MinecraftFontRenderer Inter;

    private Fonts() {
    }

    private static Font getFont(final Map<String, Font> locationMap) {
        Font font;
        try {
            if (locationMap.containsKey("Inter-Regular.ttf")) {
                font = locationMap.get("Inter-Regular.ttf").deriveFont(Font.PLAIN, (float) 18);
            } else {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("catmod", "fonts:Inter-VariableFont.ttf")).getInputStream();
                font = Font.createFont(0, is);
                locationMap.put("Inter-Regular.ttf", font);
                font = font.deriveFont(Font.PLAIN, (float) 18);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, 18);
        }
        return font;
    }

    public static void bootstrap() {
        final Map<String, Font> locationMap = new HashMap<>();
        Fonts.Inter = new MinecraftFontRenderer(getFont(locationMap), true, false);
    }
}
