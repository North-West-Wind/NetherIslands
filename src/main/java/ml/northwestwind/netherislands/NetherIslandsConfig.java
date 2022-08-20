package ml.northwestwind.netherislands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class NetherIslandsConfig {
    private static final File CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("netherislands.json").toFile();
    private static boolean ENABLED = true;
    private static String NOISE_GEN_REPLACEMENT = "minecraft:floating_islands";

    static {
        NetherIslands.LOGGER.info("Doing early config reading");
        boolean needRewrite = false;
        if (!CONFIG_FILE.exists()) {
            needRewrite = true;
        } else {
            try {
                JsonObject json = (JsonObject) JsonParser.parseReader(new FileReader(CONFIG_FILE));
                if (json.has("enabled")) ENABLED = json.get("enabled").getAsBoolean();
                else needRewrite = true;
                if (json.has("replacement")) NOISE_GEN_REPLACEMENT = json.get("replacement").getAsString();
                else needRewrite = true;
            } catch (FileNotFoundException ignored) { }
        }
        if (needRewrite) {
            try {
                JsonObject obj = new JsonObject();
                obj.addProperty("enabled", ENABLED);
                obj.addProperty("replacement", NOISE_GEN_REPLACEMENT);

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


}
