package ml.northwestwind.netherislands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class NetherIslandsConfig {
    private static final File CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("netherislands.json").toFile();
    private static boolean ENABLED = true, NOISE_OVERRIDE = true;
    private static String NOISE_GEN_REPLACEMENT = "minecraft:floating_islands";
    private static JsonObject NOISE_SETTINGS = null;

    static {
        NetherIslands.LOGGER.info("Doing early config reading");
        boolean needRewrite = false;
        if (!CONFIG_FILE.exists()) {
            needRewrite = true;
        } else {
            try {
                JsonObject json = (JsonObject) new JsonParser().parse(new FileReader(CONFIG_FILE));
                if (json.has("enabled")) ENABLED = json.get("enabled").getAsBoolean();
                else needRewrite = true;
                if (json.has("replacement")) NOISE_GEN_REPLACEMENT = json.get("replacement").getAsString();
                else needRewrite = true;
                if (json.has("noise_override")) NOISE_OVERRIDE = json.get("noise_override").getAsBoolean();
                else needRewrite = true;
                if (NOISE_OVERRIDE) NOISE_SETTINGS = json.getAsJsonObject("noise_settings");
            } catch (FileNotFoundException ignored) { }
        }
        if (needRewrite) {
            try {
                JsonObject obj = new JsonObject();
                obj.addProperty("enabled", ENABLED);
                obj.addProperty("replacement", NOISE_GEN_REPLACEMENT);
                obj.addProperty("noise_override", NOISE_OVERRIDE);
                JsonObject noise = new JsonObject();
                noise.addProperty("height", 256);

                PrintWriter writer = new PrintWriter(CONFIG_FILE);
                writer.write(obj.toString());
                writer.flush();
                writer.close();
            } catch (Exception ignored) { }
        }
    }

    public static boolean isEnabled() {
        return ENABLED;
    }

    public static ResourceLocation getReplacement() {
        return new ResourceLocation(NOISE_GEN_REPLACEMENT);
    }

    public static JsonObject getNoiseOverride() {
        return NOISE_SETTINGS;
    }
}
