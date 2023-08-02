package me.weikuwu.cute.remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.utils.network.Requests;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.codec.digest.DigestUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CapeLoader {
    public static final File capesDir = new File(CatMod.dir, "cape-cache");
    private static final HashMap<String, String> playerCapes = new HashMap<>();
    private static final HashMap<String, ResourceLocation> capes = new HashMap<>();
    private static final HashMap<String, ArrayList<ResourceLocation>> animatedCapes = new HashMap<>();

    public static ResourceLocation getCape(NetworkPlayerInfo player) {
        String userHash = DigestUtils.sha256Hex(player.getGameProfile().getId().toString());
//        System.out.println(userHash);
        String capeName = playerCapes.get(userHash);
        if (capeName == null) return null;

        if (capeName.startsWith("animated")) return getAnimatedCape(capeName); System.out.println("anim");
        return capes.get(capeName);
    }

    private static ResourceLocation getAnimatedCape(String capeName) {
        ArrayList<ResourceLocation> capeFrames = animatedCapes.get(capeName);
        if (capeFrames == null) return null;
        return capeFrames.get((int) (System.currentTimeMillis() / (1000 / 12) % capeFrames.size()));
    }

    public static void load() {
        System.out.println("Downloading capes...");
        capesDir.mkdirs();

        try {
            String capeJson = Requests.get("https://cdn.weikuwu.me/src/capes/capes.json");

            JsonObject json = new Gson().fromJson(capeJson, JsonObject.class);
            JsonObject jsonCapes = json.get("capes").getAsJsonObject();
            JsonObject jsonOwners = json.get("players").getAsJsonObject();

            for (Map.Entry<String, JsonElement> element : jsonCapes.entrySet()) {
                String capeName = element.getKey();
                String capeUrl = element.getValue().getAsString();

                System.out.println("Loading cape: " + capeName + " (" + capeUrl + ")");

                if (capeName.startsWith("animated")) {
                    animatedCapes.put(capeName, animatedCapeFromFile(capeName, capeUrl));
                } else {
                    capes.put(capeName, capeFromFile(capeName, capeUrl));
                }
            }

            for (Map.Entry<String, JsonElement> capeOwner : jsonOwners.entrySet()) {
                playerCapes.put(capeOwner.getKey(), capeOwner.getValue().getAsString());
            }
        } catch (Exception exception) {
            System.out.println("Error downloading capes");
            exception.printStackTrace();
        }
    }

    private static ResourceLocation capeFromFile(String capeName, String capeUrl) {
        try {
            File file = new File(capesDir, capeName + ".png");
            if (!file.exists()) Files.copy(new URL(capeUrl).openStream(), file.toPath());

            return CatMod.mc.getTextureManager().getDynamicTextureLocation("catmod", new DynamicTexture(ImageIO.read(file)));
        } catch (Exception exception) {
            System.out.println("Error loading cape from file/URL");
            exception.printStackTrace();
        }

        return null;
    }

    private static ArrayList<ResourceLocation> animatedCapeFromFile(String capeName, String capeUrl) {
        try {
            File file = new File(capesDir, capeName + ".gif");
            if (!file.exists()) Files.copy(new URL(capeUrl).openStream(), file.toPath());

            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream stream = ImageIO.createImageInputStream(file);
            reader.setInput(stream);

            ArrayList<ResourceLocation> frames = new ArrayList<>();

            int frameCount = reader.getNumImages(true);
            for (int i = 0; i < frameCount; i++) {
                BufferedImage frame = reader.read(i);
                ResourceLocation frameImage = CatMod.mc.getTextureManager().getDynamicTextureLocation("catmod", new DynamicTexture(frame));
                frames.add(frameImage);
            }

            return frames.isEmpty() ? null : frames;
        } catch (Exception exception) {
            System.out.println("Error loading animated cape from file/URL");
            exception.printStackTrace();
        }

        return null;
    }
}
