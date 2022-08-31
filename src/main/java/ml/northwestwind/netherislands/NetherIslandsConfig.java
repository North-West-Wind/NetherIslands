package ml.northwestwind.netherislands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.northwestwind.netherislands.utils.FileWatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Path;

public class NetherIslandsConfig {
    private static final Path CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("netherislands.json");
    private static boolean ENABLED = true, NOISE_OVERRIDE = true, DIMENSION_OVERRIDE = false;
    private static String NOISE_GEN_REPLACEMENT = "minecraft:floating_islands";
    private static JsonObject NOISE_SETTINGS = null, DIMENSION_SETTINGS = null;

    static {
        NetherIslands.LOGGER.info("Doing early config reading");
        readEverything();
        new FileWatcher(CONFIG_FILE, NetherIslandsConfig::readEverything).start();
    }

    private static void readEverything() {
        boolean needRewrite = false;
        if (!CONFIG_FILE.toFile().exists()) {
            needRewrite = true;
        } else {
            try {
                JsonObject json = (JsonObject) new JsonParser().parse(new FileReader(CONFIG_FILE.toFile()));
                if (json.has("enabled")) ENABLED = json.get("enabled").getAsBoolean();
                else needRewrite = true;
                if (json.has("replacement")) NOISE_GEN_REPLACEMENT = json.get("replacement").getAsString();
                else needRewrite = true;
                if (json.has("noise_override")) NOISE_OVERRIDE = json.get("noise_override").getAsBoolean();
                else needRewrite = true;
                if (NOISE_OVERRIDE) {
                    if (json.has("noise_settings")) NOISE_SETTINGS = json.getAsJsonObject("noise_settings");
                    else needRewrite = true;
                }
                if (json.has("dimension_override")) DIMENSION_OVERRIDE = json.get("dimension_override").getAsBoolean();
                else needRewrite = true;
                if (DIMENSION_OVERRIDE) {
                    if (json.has("dimension_settings")) DIMENSION_SETTINGS = json.getAsJsonObject("dimension_settings");
                    else needRewrite = true;
                }
            } catch (FileNotFoundException ignored) { }
        }
        if (needRewrite) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject obj = new JsonObject();
                obj.addProperty("enabled", ENABLED);
                obj.addProperty("replacement", NOISE_GEN_REPLACEMENT);
                obj.addProperty("noise_override", NOISE_OVERRIDE);
                obj.addProperty("dimension_override", DIMENSION_OVERRIDE);
                JsonObject noise = new JsonObject();
                noise.addProperty("height", 256);
                if (NOISE_OVERRIDE) NOISE_SETTINGS = noise;
                obj.add("noise_settings", noise);
                JsonObject dimension = new JsonObject();
                dimension.addProperty("seaLevel", 0);
                dimension.addProperty("bedrockRoofPosition", -10);
                dimension.addProperty("bedrockFloorPosition", -10);
                if (DIMENSION_OVERRIDE) DIMENSION_SETTINGS = dimension;
                obj.add("dimension_settings", dimension);

                PrintWriter writer = new PrintWriter(CONFIG_FILE.toFile());
                writer.write(gson.toJson(obj));
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

    public static JsonObject getDimensionSettings() {
        return DIMENSION_SETTINGS;
    }
}
