package ml.northwestwind.netherislands.mixin;

import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseSettings.class)
public interface AccessorNoiseSettings {
    @Accessor("NETHER_NOISE_SETTINGS")
    static NoiseSettings getNetherNoiseSettings() {
        throw new AssertionError();
    }

    @Accessor("FLOATING_ISLANDS_NOISE_SETTINGS")
    static NoiseSettings getFloatingIslandsNoiseSettings() {
        throw new AssertionError();
    }
}
