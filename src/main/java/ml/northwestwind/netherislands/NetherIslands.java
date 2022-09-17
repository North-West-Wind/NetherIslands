package ml.northwestwind.netherislands;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(NetherIslands.MOD_ID)
public class NetherIslands {
    public static final String MOD_ID = "netherislands";
    public static final Logger LOGGER = LogManager.getLogger();
    //public static final DeferredRegister<NoiseRouter> NOISE_ROUTER = DeferredRegister.create(new ResourceLocation(MOD_ID, "noise_router"), "minecraft");

    public NetherIslands() {
        // Potentially useful, but not now
        /*NOISE_ROUTER.makeRegistry(RegistryBuilder::new);

        NOISE_ROUTER.register("overworld", () -> InvokerNoiseRouterData.invokeOverworld(BuiltinRegistries.DENSITY_FUNCTION, false, false));
        NOISE_ROUTER.register("overworld_large", () -> InvokerNoiseRouterData.invokeOverworld(BuiltinRegistries.DENSITY_FUNCTION, true, false));
        NOISE_ROUTER.register("overworld_amplified", () -> InvokerNoiseRouterData.invokeOverworld(BuiltinRegistries.DENSITY_FUNCTION, false, true));
        NOISE_ROUTER.register("overworld_large_amplified", () -> InvokerNoiseRouterData.invokeOverworld(BuiltinRegistries.DENSITY_FUNCTION, true, true));

        NOISE_ROUTER.register("nether", () -> InvokerNoiseRouterData.invokeNether(BuiltinRegistries.DENSITY_FUNCTION));
        NOISE_ROUTER.register("caves", () -> InvokerNoiseRouterData.invokeCaves(BuiltinRegistries.DENSITY_FUNCTION));
        NOISE_ROUTER.register("floating_islands", () -> InvokerNoiseRouterData.invokeFloatingIslands(BuiltinRegistries.DENSITY_FUNCTION));
        NOISE_ROUTER.register("end", () -> InvokerNoiseRouterData.invokeEnd(BuiltinRegistries.DENSITY_FUNCTION));
        NOISE_ROUTER.register("dummy", InvokerNoiseRouterData::invokeNone);
        NOISE_ROUTER.register(FMLJavaModLoadingContext.get().getModEventBus());*/
    }
}
