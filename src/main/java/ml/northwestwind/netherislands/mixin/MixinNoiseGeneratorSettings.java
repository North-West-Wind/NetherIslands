package ml.northwestwind.netherislands.mixin;

import com.google.gson.JsonObject;
import ml.northwestwind.netherislands.NetherIslandsConfig;
import ml.northwestwind.netherislands.NetherIslandsHolder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseGeneratorSettings.class)
public abstract class MixinNoiseGeneratorSettings {
    @Shadow
    public static void register(ResourceKey<NoiseGeneratorSettings> key, NoiseGeneratorSettings settings) {
    }

    @Shadow @Final public static ResourceKey<NoiseGeneratorSettings> NETHER;

    @Shadow
    public static NoiseGeneratorSettings nether() {
        return null;
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;<init>(Lnet/minecraft/world/level/levelgen/NoiseSettings;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/levelgen/NoiseRouterWithOnlyNoises;Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;IZZZZ)V"), index = 0)
    private static NoiseSettings useCustomSettingsForData(NoiseSettings settings) {
        if (!NetherIslandsConfig.isEnabled()) return settings;
        if (NetherIslandsHolder.cached == null) {
            NetherIslandsHolder.cached = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NetherIslandsConfig.getReplacement());
            if (NetherIslandsHolder.cached != null) settings = NetherIslandsHolder.cached.noiseSettings();
        } else settings = NetherIslandsHolder.cached.noiseSettings();
        JsonObject override = NetherIslandsConfig.getNoiseOverride();
        if (override == null) return settings;
        int height = override.has("height") ? override.get("height").getAsInt() : settings.height();
        return new NoiseSettings(settings.minY(), height, settings.noiseSamplingSettings(), settings.topSlideSettings(), settings.bottomSlideSettings(), settings.noiseSizeHorizontal(), settings.noiseSizeVertical(), settings.terrainShaper());
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseRouterData;nether(Lnet/minecraft/world/level/levelgen/NoiseSettings;)Lnet/minecraft/world/level/levelgen/NoiseRouterWithOnlyNoises;"), index = 0)
    private static NoiseSettings useCustomSettingsForRouter(NoiseSettings settings) {
        if (!NetherIslandsConfig.isEnabled()) return settings;
        if (NetherIslandsHolder.cached == null) {
            NetherIslandsHolder.cached = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NetherIslandsConfig.getReplacement());
            if (NetherIslandsHolder.cached != null) settings = NetherIslandsHolder.cached.noiseSettings();
        } else settings = NetherIslandsHolder.cached.noiseSettings();
        JsonObject override = NetherIslandsConfig.getNoiseOverride();
        if (override == null) return settings;
        int minY = override.has("minY") ? override.get("minY").getAsInt() : settings.minY();
        int height = override.has("height") ? override.get("height").getAsInt() : settings.height();
        return new NoiseSettings(minY, height, settings.noiseSamplingSettings(), settings.topSlideSettings(), settings.bottomSlideSettings(), settings.noiseSizeHorizontal(), settings.noiseSizeVertical(), settings.terrainShaper());
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)V", ordinal = 3))
    private static void dontRegister(ResourceKey<NoiseGeneratorSettings> key, NoiseGeneratorSettings settings) {
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerNether(CallbackInfo ci) {
        register(NETHER, nether());
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;<init>(Lnet/minecraft/world/level/levelgen/NoiseSettings;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/levelgen/NoiseRouterWithOnlyNoises;Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;IZZZZ)V"), index = 5)
    private static int changeSeaLevel(int seaLevel) {
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override == null) return seaLevel;
        return override.has("seaLevel") ? override.get("seaLevel").getAsInt() : seaLevel;
    }
}
