package etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.blocks.templates;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.utilities.constructs.IForgeRegistryEntryExtension;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.utilities.helpers.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

public class BlockDefault extends Block implements IForgeRegistryEntryExtension<Block> {
	/* SECTION constructors */

	public BlockDefault(Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }

	public BlockDefault(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		if (getRegistryName() != null) setUnlocalizedName(getRegistryName().getResourcePath());
	}


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public Block setUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }

	/** {@inheritDoc} */
	@Override
	public Block setRegistryAndUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(name).setRegistryName(name); }
}
