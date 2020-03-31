package $group__.$modId__.common.registrables.blocks;

import $group__.$modId__.common.registrables.materials.Materials;
import $group__.$modId__.common.registrables.traits.IForgeRegistryEntryExtension;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import static $group__.$modId__.common.registrables.blocks.bases.BlockBases.setUnlocalizedNameBase;

public class BlockCable extends Block implements IForgeRegistryEntryExtension<Block> {
	/* SECTION constructors */

	protected BlockCable() {
		super(Materials.PETRIFIED_SKY);
	}

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public Block setUnlocalizedName(ResourceLocation name) { return setUnlocalizedNameBase(this, name); }
}
