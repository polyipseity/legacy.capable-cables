package $group__.$modId__.common.registrable.blocks.templates;

import $group__.$modId__.common.registrable.utilities.constructs.IForgeRegistryEntryExtension;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import static $group__.$modId__.common.registrable.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;

public class BlockDefault extends Block implements IForgeRegistryEntryExtension<Block> {
	/* SECTION constructors */

	public BlockDefault(Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }

	public BlockDefault(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		if (getRegistryName() != null) setUnlocalizedName(getRegistryName().getResourcePath());
	}


	/* SECTION methods */

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public Block setUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public Block setRegistryAndUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(name).setRegistryName(name); }
}
