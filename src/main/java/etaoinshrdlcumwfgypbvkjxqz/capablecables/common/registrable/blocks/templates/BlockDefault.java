package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks.templates;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.constructs.IForgeRegistryEntryExtension;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.helpers.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

public class BlockDefault extends Block implements IForgeRegistryEntryExtension<Block> {
	public BlockDefault(Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }

	public BlockDefault(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		if (getRegistryName() != null) setUnlocalizedName(getRegistryName().getResourcePath());
	}

	/* IForgeRegistryEntryExtension */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block setUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block setRegistryAndUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(name).setRegistryName(name); }
}
