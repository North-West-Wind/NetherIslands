package ml.northwestwind.netherislands.mixin;

import com.google.gson.JsonObject;
import ml.northwestwind.netherislands.NetherIslandsConfig;
import ml.northwestwind.netherislands.NetherIslandsHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NoiseGeneratorSettings.class)
public abstract class MixinNoiseGeneratorSettings {
    @Shadow
    public static Holder<NoiseGeneratorSettings> register(Registry<NoiseGeneratorSettings> registry, ResourceKey<NoiseGeneratorSettings> key, NoiseGeneratorSettings settings) {
        return null;
    }

    @Shadow @Final public static ResourceKey<NoiseGeneratorSettings> NETHER;

    @Shadow
    public static NoiseGeneratorSettings nether() {
        return null;
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;<init>(Lnet/minecraft/world/level/levelgen/NoiseSettings;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/levelgen/NoiseRouter;Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;Ljava/util/List;IZZZZ)V"), index = 0)
    private static NoiseSettings useCustomSettingsForData(NoiseSettings settings) {
        if (!NetherIslandsConfig.isEnabled()) return settings;
        if (NetherIslandsHolder.cached == null) {
            NetherIslandsHolder.cached = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NetherIslandsConfig.getReplacement());
            if (NetherIslandsHolder.cached != null) settings = NetherIslandsHolder.cached.noiseSettings();
        } else settings = NetherIslandsHolder.cached.noiseSettings();
        JsonObject override = NetherIslandsConfig.getNoiseOverride();
        if (override == null) return settings;
        int height = override.has("height") ? override.get("height").getAsInt() : settings.height();
        return new NoiseSettings(settings.minY(), height, settings.noiseSizeHorizontal(), settings.noiseSizeVertical());
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;<init>(Lnet/minecraft/world/level/levelgen/NoiseSettings;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/levelgen/NoiseRouter;Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;Ljava/util/List;IZZZZ)V"), index = 3)
    private static NoiseRouter useCustomRouter(NoiseRouter router) {
        if (!NetherIslandsConfig.isEnabled()) return router;
        //Optional<RegistryObject<NoiseRouter>> optional = NetherIslands.NOISE_ROUTER.getEntries().stream().filter(obj -> obj.getId().equals(NetherIslandsConfig.getReplacement())).findFirst();
        //if (optional.isPresent()) router = optional.get().get();
        return InvokerNoiseRouterData.invokeFloatingIslands(BuiltinRegistries.DENSITY_FUNCTION);
    }

    @Redirect(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)Lnet/minecraft/core/Holder;", ordinal = 3))
    private static Holder<NoiseGeneratorSettings> dontRegister(Registry<NoiseGeneratorSettings> registry, ResourceKey<NoiseGeneratorSettings> key, NoiseGeneratorSettings settings) {
        return null;
    }

    @Redirect(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)Lnet/minecraft/core/Holder;", ordinal = 6))
    private static Holder<NoiseGeneratorSettings> registerNether(Registry<NoiseGeneratorSettings> registry, ResourceKey<NoiseGeneratorSettings> key, NoiseGeneratorSettings settings) {
        Holder<NoiseGeneratorSettings> floatingIslands = register(registry, key, settings);
        register(registry, NETHER, nether());
        return floatingIslands;
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;<init>(Lnet/minecraft/world/level/levelgen/NoiseSettings;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/levelgen/NoiseRouter;Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;Ljava/util/List;IZZZZ)V"), index = 6)
    private static int changeSeaLevel(int seaLevel) {
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override == null) return seaLevel;
        return override.has("seaLevel") ? override.get("seaLevel").getAsInt() : seaLevel;
    }
}
