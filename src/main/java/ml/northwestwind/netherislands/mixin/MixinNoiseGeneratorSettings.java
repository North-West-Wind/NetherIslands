package ml.northwestwind.netherislands.mixin;

import com.google.gson.JsonObject;
import ml.northwestwind.netherislands.NetherIslands;
import ml.northwestwind.netherislands.NetherIslandsConfig;
import ml.northwestwind.netherislands.NetherIslandsHolder;
import net.minecraft.block.BlockState;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimensionSettings.class)
public abstract class MixinNoiseGeneratorSettings {
    @Shadow
    private static DimensionSettings register(RegistryKey<DimensionSettings> p_242745_0_, DimensionSettings p_242745_1_) {
        return null;
    }

    @Shadow @Final public static RegistryKey<DimensionSettings> NETHER;

    @Shadow
    private static DimensionSettings nether(DimensionStructuresSettings p_242741_0_, BlockState p_242741_1_, BlockState p_242741_2_, ResourceLocation p_242741_3_) {
        return null;
    }

    @Shadow @Final private int seaLevel;

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/DimensionSettings;<init>(Lnet/minecraft/world/gen/settings/DimensionStructuresSettings;Lnet/minecraft/world/gen/settings/NoiseSettings;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;IIIZ)V"), index = 1)
    private static NoiseSettings useCustomSettingsForData(NoiseSettings settings) {
        if (!NetherIslandsConfig.isEnabled()) return settings;
        if (NetherIslandsHolder.cached == null) {
            NetherIslandsHolder.cached = WorldGenRegistries.NOISE_GENERATOR_SETTINGS.get(NetherIslandsConfig.getReplacement());
            if (NetherIslandsHolder.cached != null) settings = NetherIslandsHolder.cached.noiseSettings();
        } else settings = NetherIslandsHolder.cached.noiseSettings();
        JsonObject override = NetherIslandsConfig.getNoiseOverride();
        if (override == null) return settings;
        int height = override.has("height") ? override.get("height").getAsInt() : settings.height();
        return new NoiseSettings(height, settings.noiseSamplingSettings(), settings.topSlideSettings(), settings.bottomSlideSettings(), settings.noiseSizeHorizontal(), settings.noiseSizeVertical(), settings.densityFactor(), settings.densityOffset(), settings.useSimplexSurfaceNoise(), settings.randomDensityOffset(), settings.islandNoiseOverride(), settings.isAmplified());
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/DimensionSettings;register(Lnet/minecraft/util/RegistryKey;Lnet/minecraft/world/gen/DimensionSettings;)Lnet/minecraft/world/gen/DimensionSettings;", ordinal = 2))
    private static DimensionSettings dontRegister(RegistryKey<DimensionSettings> key, DimensionSettings settings) {
        NetherIslands.LOGGER.info("Getting key: {}", key.location());
        NetherIslandsHolder.netherSettings = settings.structureSettings();
        NetherIslandsHolder.netherBlock = settings.getDefaultBlock();
        NetherIslandsHolder.netherFluid = settings.getDefaultFluid();
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerNether(CallbackInfo ci) {
        register(NETHER, nether(NetherIslandsHolder.netherSettings, NetherIslandsHolder.netherBlock, NetherIslandsHolder.netherFluid, NETHER.location()));
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/DimensionSettings;<init>(Lnet/minecraft/world/gen/settings/DimensionStructuresSettings;Lnet/minecraft/world/gen/settings/NoiseSettings;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;IIIZ)V"), index = 4)
    private static int changeBedrockRoof(int bedrockRoof) {
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override == null) return bedrockRoof;
        return override.has("bedrockRoofPosition") ? override.get("bedrockRoofPosition").getAsInt() : bedrockRoof;
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/DimensionSettings;<init>(Lnet/minecraft/world/gen/settings/DimensionStructuresSettings;Lnet/minecraft/world/gen/settings/NoiseSettings;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;IIIZ)V"), index = 5)
    private static int changeBedrockFloor(int bedrockFloor) {
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override == null) return bedrockFloor;
        return override.has("bedrockFloorPosition") ? override.get("bedrockFloorPosition").getAsInt() : bedrockFloor;
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/DimensionSettings;<init>(Lnet/minecraft/world/gen/settings/DimensionStructuresSettings;Lnet/minecraft/world/gen/settings/NoiseSettings;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;IIIZ)V"), index = 6)
    private static int changeSeaLevel(int seaLevel) {
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override == null) return seaLevel;
        return override.has("seaLevel") ? override.get("seaLevel").getAsInt() : seaLevel;
    }
}
