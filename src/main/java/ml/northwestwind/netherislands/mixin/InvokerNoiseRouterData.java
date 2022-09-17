package ml.northwestwind.netherislands.mixin;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseRouterData.class)
public interface InvokerNoiseRouterData {
    /*@Invoker("overworld")
    public static NoiseRouter invokeOverworld(Registry<DensityFunction> registry, boolean large, boolean amplified) {
        throw new AssertionError();
    }

    @Invoker("nether")
    public static NoiseRouter invokeNether(Registry<DensityFunction> registry) {
        throw new AssertionError();
    }

    @Invoker("caves")
    public static NoiseRouter invokeCaves(Registry<DensityFunction> registry) {
        throw new AssertionError();
    }*/

    @Invoker("floatingIslands")
    public static NoiseRouter invokeFloatingIslands(Registry<DensityFunction> registry) {
        throw new AssertionError();
    }

    /*@Invoker("end")
    public static NoiseRouter invokeEnd(Registry<DensityFunction> registry) {
        throw new AssertionError();
    }

    @Invoker("none")
    public static NoiseRouter invokeNone() {
        throw new AssertionError();
    }*/
}
