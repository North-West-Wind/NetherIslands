package ml.northwestwind.netherislands.mixin;

import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SurfaceRuleData.class)
public interface AccessorSurfaceRuleData {
    @Accessor("BEDROCK")
    static SurfaceRules.RuleSource BEDROCK() {
        throw new AssertionError();
    }

    @Accessor("NETHERRACK")
    static SurfaceRules.RuleSource NETHERRACK() {
        throw new AssertionError();
    }

    @Accessor("GRAVEL")
    static SurfaceRules.RuleSource GRAVEL() {
        throw new AssertionError();
    }

    @Accessor("BASALT")
    static SurfaceRules.RuleSource BASALT() {
        throw new AssertionError();
    }

    @Accessor("BLACKSTONE")
    static SurfaceRules.RuleSource BLACKSTONE() {
        throw new AssertionError();
    }

    @Accessor("SOUL_SAND")
    static SurfaceRules.RuleSource SOUL_SAND() {
        throw new AssertionError();
    }

    @Accessor("SOUL_SOIL")
    static SurfaceRules.RuleSource SOUL_SOIL() {
        throw new AssertionError();
    }

    @Accessor("WARPED_WART_BLOCK")
    static SurfaceRules.RuleSource WARPED_WART_BLOCK() {
        throw new AssertionError();
    }

    @Accessor("WARPED_NYLIUM")
    static SurfaceRules.RuleSource WARPED_NYLIUM() {
        throw new AssertionError();
    }

    @Accessor("NETHER_WART_BLOCK")
    static SurfaceRules.RuleSource NETHER_WART_BLOCK() {
        throw new AssertionError();
    }

    @Accessor("CRIMSON_NYLIUM")
    static SurfaceRules.RuleSource CRIMSON_NYLIUM() {
        throw new AssertionError();
    }

    @Accessor("LAVA")
    static SurfaceRules.RuleSource LAVA() {
        throw new AssertionError();
    }
}
