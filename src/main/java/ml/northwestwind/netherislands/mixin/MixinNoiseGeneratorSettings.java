package ml.northwestwind.netherislands.mixin;

import com.google.gson.JsonObject;
import ml.northwestwind.netherislands.NetherIslands;
import ml.northwestwind.netherislands.NetherIslandsConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(NoiseGeneratorSettings.class)
public abstract class MixinNoiseGeneratorSettings {
    @Shadow @Final public static ResourceKey<NoiseGeneratorSettings> NETHER;

    @Unique
    private static NoiseSettings getNoiseSettings(BootstapContext<?> context, NoiseSettings settings) {
        if (!NetherIslandsConfig.isEnabled()) return settings;
        NetherIslands.LOGGER.info("Replacing NoiseSettings");
        AtomicReference<NoiseSettings> replacement = new AtomicReference<>(null);
        context.registryLookup(Registries.NOISE_SETTINGS).flatMap(lookup -> lookup.get(ResourceKey.create(Registries.NOISE_SETTINGS, NetherIslandsConfig.getReplacement()))).ifPresent(optional -> replacement.set(optional.get().noiseSettings()));
        NetherIslands.LOGGER.info("Found replacement with {}? {}", NetherIslandsConfig.getReplacement(), replacement.get() != null);
        if (replacement.get() != null) settings = replacement.get();
        JsonObject override = NetherIslandsConfig.getNoiseOverride();
        if (override == null) return settings;
        int height = override.has("height") ? override.get("height").getAsInt() : settings.height();
        return new NoiseSettings(settings.minY(), height, settings.noiseSizeHorizontal(), settings.noiseSizeVertical());
    }

    @Unique
    private static int getSeaLevel(int seaLevel) {
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override == null) return seaLevel;
        return override.has("seaLevel") ? override.get("seaLevel").getAsInt() : seaLevel;
    }
    
    /*@Unique
    private static SurfaceRules.RuleSource getNetherRule() {
        SurfaceRules.ConditionSource surfacerules$conditionsource = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(31), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(32), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource3 = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
        SurfaceRules.ConditionSource surfacerules$conditionsource4 = SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(5), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource5 = SurfaceRules.hole();
        SurfaceRules.ConditionSource surfacerules$conditionsource6 = SurfaceRules.noiseCondition(Noises.SOUL_SAND_LAYER, -0.012D);
        SurfaceRules.ConditionSource surfacerules$conditionsource7 = SurfaceRules.noiseCondition(Noises.GRAVEL_LAYER, -0.012D);
        SurfaceRules.ConditionSource surfacerules$conditionsource8 = SurfaceRules.noiseCondition(Noises.PATCH, -0.012D);
        SurfaceRules.ConditionSource surfacerules$conditionsource9 = SurfaceRules.noiseCondition(Noises.NETHERRACK, 0.54D);
        SurfaceRules.ConditionSource surfacerules$conditionsource10 = SurfaceRules.noiseCondition(Noises.NETHER_WART, 1.17D);
        SurfaceRules.ConditionSource surfacerules$conditionsource11 = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0D);
        SurfaceRules.RuleSource surfacerules$rulesource = SurfaceRules.ifTrue(surfacerules$conditionsource8, SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.ifTrue(surfacerules$conditionsource3, AccessorSurfaceRuleData.GRAVEL())));
        
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override != null && override.has("bedrockFloorPosition")) {
            int position = override.get("bedrockFloorPosition").getAsInt();
            return SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.absolute(position), VerticalAnchor.absolute(position + 5)), AccessorSurfaceRuleData.BEDROCK()), SurfaceRules.ifTrue(surfacerules$conditionsource4, AccessorSurfaceRuleData.NETHERRACK()), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BASALT_DELTAS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, AccessorSurfaceRuleData.BASALT()), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(surfacerules$rulesource, SurfaceRules.ifTrue(surfacerules$conditionsource11, AccessorSurfaceRuleData.BASALT()), AccessorSurfaceRuleData.BLACKSTONE())))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SOUL_SAND_VALLEY), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, AccessorSurfaceRuleData.SOUL_SAND()), AccessorSurfaceRuleData.SOUL_SOIL())), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(surfacerules$rulesource, SurfaceRules.ifTrue(surfacerules$conditionsource11, AccessorSurfaceRuleData.SOUL_SAND()), AccessorSurfaceRuleData.SOUL_SOIL())))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource1), SurfaceRules.ifTrue(surfacerules$conditionsource5, AccessorSurfaceRuleData.LAVA())), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARPED_FOREST), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource9), SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource10, AccessorSurfaceRuleData.WARPED_WART_BLOCK()), AccessorSurfaceRuleData.WARPED_NYLIUM())))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.CRIMSON_FOREST), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource9), SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource10, AccessorSurfaceRuleData.NETHER_WART_BLOCK()), AccessorSurfaceRuleData.CRIMSON_NYLIUM())))))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.NETHER_WASTES), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource6, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5), SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.ifTrue(surfacerules$conditionsource3, AccessorSurfaceRuleData.SOUL_SAND()))), AccessorSurfaceRuleData.NETHERRACK()))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.ifTrue(surfacerules$conditionsource3, SurfaceRules.ifTrue(surfacerules$conditionsource7, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource1, AccessorSurfaceRuleData.GRAVEL()), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5), AccessorSurfaceRuleData.GRAVEL())))))))), AccessorSurfaceRuleData.NETHERRACK());
        } else return SurfaceRules.sequence(
                SurfaceRules.ifTrue(surfacerules$conditionsource4, AccessorSurfaceRuleData.NETHERRACK()),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BASALT_DELTAS),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, AccessorSurfaceRuleData.BASALT()),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                        SurfaceRules.sequence(surfacerules$rulesource,
                                                SurfaceRules.ifTrue(surfacerules$conditionsource11, AccessorSurfaceRuleData.BASALT()), AccessorSurfaceRuleData.BLACKSTONE())))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SOUL_SAND_VALLEY),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING,
                                SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, AccessorSurfaceRuleData.SOUL_SAND()), AccessorSurfaceRuleData.SOUL_SOIL())),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                        SurfaceRules.sequence(surfacerules$rulesource,
                                                SurfaceRules.ifTrue(surfacerules$conditionsource11, AccessorSurfaceRuleData.SOUL_SAND()), AccessorSurfaceRuleData.SOUL_SOIL())))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource1),
                                SurfaceRules.ifTrue(surfacerules$conditionsource5, AccessorSurfaceRuleData.LAVA())),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARPED_FOREST),
                                        SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource9),
                                                SurfaceRules.ifTrue(surfacerules$conditionsource,
                                                        SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource10, AccessorSurfaceRuleData.WARPED_WART_BLOCK()), AccessorSurfaceRuleData.WARPED_NYLIUM())))),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.CRIMSON_FOREST),
                                        SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource9),
                                                SurfaceRules.ifTrue(surfacerules$conditionsource,
                                                        SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource10, AccessorSurfaceRuleData.NETHER_WART_BLOCK()), AccessorSurfaceRuleData.CRIMSON_NYLIUM())))))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.NETHER_WASTES),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                SurfaceRules.ifTrue(surfacerules$conditionsource6,
                                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5),
                                                SurfaceRules.ifTrue(surfacerules$conditionsource2,
                                                        SurfaceRules.ifTrue(surfacerules$conditionsource3, AccessorSurfaceRuleData.SOUL_SAND()))), AccessorSurfaceRuleData.NETHERRACK()))),
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                        SurfaceRules.ifTrue(surfacerules$conditionsource,
                                                SurfaceRules.ifTrue(surfacerules$conditionsource3,
                                                        SurfaceRules.ifTrue(surfacerules$conditionsource7,
                                                                SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource1, AccessorSurfaceRuleData.GRAVEL()),
                                                                        SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5),
                                                                                AccessorSurfaceRuleData.GRAVEL())))))))), AccessorSurfaceRuleData.NETHERRACK());
    }*/

    @Inject(method = "nether", at = @At("HEAD"), cancellable = true)
    private static void nether(BootstapContext<?> context, CallbackInfoReturnable<NoiseGeneratorSettings> cir) {
        NetherIslands.LOGGER.info("changing nether noise generator settings");
        cir.setReturnValue(new NoiseGeneratorSettings(
                getNoiseSettings(context, AccessorNoiseSettings.getNetherNoiseSettings()),
                Blocks.NETHERRACK.defaultBlockState(),
                Blocks.LAVA.defaultBlockState(),
                InvokerNoiseRouterData.invokeFloatingIslands(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE)),
                MixinSurfaceRuleData.nether(),
                List.of(),
                getSeaLevel(32),
                false,
                false,
                false,
                true
        ));
    }

    /*@Redirect(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/worldgen/BootstapContext;register(Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;)Lnet/minecraft/core/Holder$Reference;", ordinal = 3))
    private static <T> Holder.Reference<T> dontRegister(BootstapContext<T> instance, ResourceKey<T> p_255743_, T p_256121_) {
        return null;
    }

    @Redirect(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/worldgen/BootstapContext;register(Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;)Lnet/minecraft/core/Holder$Reference;", ordinal = 6))
    private static <T> Holder.Reference<T> registerNether(BootstapContext<T> instance, ResourceKey<T> key, T settings) {
        Holder.Reference<T> floatingIslands = instance.register(key, settings);
        instance.register((ResourceKey<T>) NETHER, settings);
        return floatingIslands;
    }*/
}
