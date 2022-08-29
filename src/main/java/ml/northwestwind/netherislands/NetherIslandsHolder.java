package ml.northwestwind.netherislands;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

public class NetherIslandsHolder {
    public static DimensionSettings cached = null;
    public static DimensionStructuresSettings netherSettings = null;
    public static BlockState netherBlock = null;
    public static BlockState netherFluid = null;
}
