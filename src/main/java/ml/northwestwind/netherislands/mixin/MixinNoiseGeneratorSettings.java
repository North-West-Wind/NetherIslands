package ml.northwestwind.netherislands.mixin;

import ml.northwestwind.netherislands.NetherIslandsConfig;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseGeneratorSettings.class)
public abstract class MixinNoiseGeneratorSettings {
    @Shadow
    public static void register(ResourceKey<NoiseGeneratorSettings> p_198263_, NoiseGeneratorSettings p_198264_) {
    }

    @Shadow @Final public static ResourceKey<NoiseGeneratorSettings> NETHER;

    @Shadow
    public static NoiseGeneratorSettings nether() {
        return null;
    }

    @Unique
    private static NoiseGeneratorSettings cached = null;

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseRouterData;nether(Lnet/minecraft/world/level/levelgen/NoiseSettings;)Lnet/minecraft/world/level/levelgen/NoiseRouterWithOnlyNoises;"), index = 0)
    private static NoiseSettings useCustomSettingsForData(NoiseSettings settings) {
        if (!NetherIslandsConfig.isEnabled()) return settings;
        if (cached == null) {
            cached = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NetherIslandsConfig.getReplacement());
            if (cached == null) return settings;
        }
        return cached.noiseSettings();
    }

    @ModifyArg(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;<init>(Lnet/minecraft/world/level/levelgen/NoiseSettings;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/levelgen/NoiseRouterWithOnlyNoises;Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;IZZZZ)V"), index = 0)
    private static NoiseSettings useCustomSettingsForGenerator(NoiseSettings settings) {
        if (!NetherIslandsConfig.isEnabled()) return settings;
        if (cached == null) {
            cached = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NetherIslandsConfig.getReplacement());
            if (cached == null) return settings;
        }
        return cached.noiseSettings();
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)V", ordinal = 3))
    private static void dontRegister(ResourceKey<NoiseGeneratorSettings> p_198263_, NoiseGeneratorSettings p_198264_) {
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerNether(CallbackInfo ci) {
        register(NETHER, nether());
    }
}
