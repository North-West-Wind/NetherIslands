package ml.northwestwind.netherislands.mixin;

import com.google.gson.JsonObject;
import ml.northwestwind.netherislands.NetherIslandsConfig;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SurfaceRuleData.class)
public class MixinSurfaceRuleData {
    @Redirect(method = "nether", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/SurfaceRules;verticalGradient(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/VerticalAnchor;Lnet/minecraft/world/level/levelgen/VerticalAnchor;)Lnet/minecraft/world/level/levelgen/SurfaceRules$ConditionSource;", ordinal = 0))
    private static SurfaceRules.ConditionSource changeBedrockFloorPosition(String key, VerticalAnchor anchorLow, VerticalAnchor anchorHigh) {
        JsonObject override = NetherIslandsConfig.getDimensionSettings();
        if (override == null || !override.has("bedrockFloorPosition")) return SurfaceRules.verticalGradient(key, anchorLow, anchorHigh);
        int position = override.get("bedrockFloorPosition").getAsInt();
        return SurfaceRules.verticalGradient(key, VerticalAnchor.absolute(position), VerticalAnchor.absolute(position + 5));
    }
}
