package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.blocks.templates;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

public class BlockDefault extends Block {
    public BlockDefault(Material materialIn) { super(materialIn); }
    @SuppressWarnings("unused")
    public BlockDefault(Material blockMaterialIn, MapColor blockMapColorIn) { super(blockMaterialIn, blockMapColorIn); }

    @Override
    public Block setUnlocalizedName(String name) { return super.setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }
}
