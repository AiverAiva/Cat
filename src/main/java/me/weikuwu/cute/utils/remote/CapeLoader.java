package me.weikuwu.cute.utils.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.weikuwu.cute.CatMod;
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

public class CapeLoader {
    private static final File capesDir = new File(CatMod.dir, "cape-cache");
    private static final HashMap<String, String> playerCapes = new HashMap<>();
    private static final HashMap<String, ResourceLocation> capes = new HashMap<>();
    private static final HashMap<String, ArrayList<ResourceLocation>> animatedCapes = new HashMap<>();

    public static ResourceLocation getCape(NetworkPlayerInfo player) {
        String userHash = DigestUtils.sha256Hex(player.getGameProfile().getId().toString());
        String capeName = playerCapes.get(userHash);
        if (capeName == null) return null;
        return capeName.startsWith("animated") ? getAnimatedCape(capeName) : capes.get(capeName);
    }

    private static ResourceLocation getAnimatedCape(String capeName) {
        ArrayList<ResourceLocation> capeFrames = animatedCapes.get(capeName);
        if (capeFrames == null) return null;
        return capeFrames.get((int) (System.currentTimeMillis() / (1000 / 12) % capeFrames.size()));
    }

    public static void load() {
        capesDir.mkdirs();

        try {
            JsonObject json = new Gson().fromJson(Requests.get("https://cdn.weikuwu.me/src/capes/capes.json"), JsonObject.class);
            JsonObject jsonCapes = json.getAsJsonObject("capes");
            JsonObject jsonOwners = json.getAsJsonObject("players");

            jsonCapes.entrySet().forEach(entry -> loadCape(entry.getKey(), entry.getValue().getAsString()));
            jsonOwners.entrySet().forEach(entry -> playerCapes.put(entry.getKey(), entry.getValue().getAsString()));
        } catch (Exception e) {
            System.out.println("Error downloading capes");
            e.printStackTrace();
        }
    }

    private static void loadCape(String capeName, String capeUrl) {
        if (capeName.startsWith("animated")) {
            animatedCapes.put(capeName, animatedCapeFromFile(capeName, capeUrl));
        } else {
            capes.put(capeName, capeFromFile(capeName, capeUrl));
        }
    }

    private static ResourceLocation capeFromFile(String capeName, String capeUrl) {
        try {
            File file = downloadFile(capeName, capeUrl, ".png");
            if (file == null) return null;
            return CatMod.mc.getTextureManager().getDynamicTextureLocation("catmod", new DynamicTexture(ImageIO.read(file)));
        } catch (Exception exception) {
            System.out.println("Error loading cape from file/URL");
            exception.printStackTrace();
        }
        return null;
    }

    private static ArrayList<ResourceLocation> animatedCapeFromFile(String capeName, String capeUrl) {
        try {
            File file = downloadFile(capeName, capeUrl, ".gif");
            if (file == null) return null;

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

    private static File downloadFile(String capeName, String capeUrl, String extension) {
        try {
            File file = new File(capesDir, capeName + extension);
            if (!file.exists()) Files.copy(new URL(capeUrl).openStream(), file.toPath());
            return file;
        } catch (Exception exception) {
            System.out.println("Error downloading file: " + extension);
            exception.printStackTrace();
            return null;
        }
    }
}
