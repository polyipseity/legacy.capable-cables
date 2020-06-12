package $group__.common.registrables.blocks;

import $group__.common.registrables.blocks.bases.BlockBases;
import $group__.common.registrables.materials.Materials;
import $group__.common.registrables.traits.IForgeRegistryEntryExtension;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class BlockCable extends Block implements IForgeRegistryEntryExtension<Block> {
	/* SECTION constructors */

	protected BlockCable() {
		super(Materials.PETRIFIED_SKY);
	}

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public Block setUnlocalizedName(ResourceLocation name) { return BlockBases.setUnlocalizedNameBase(this, name); }
}
