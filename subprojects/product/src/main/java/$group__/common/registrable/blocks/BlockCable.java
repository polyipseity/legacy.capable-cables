package $group__.common.registrable.blocks;

import $group__.common.registrable.materials.Materials;
import net.minecraft.block.Block;

public class BlockCable extends Block {
	protected BlockCable() {
		super(Properties.create(Materials.PETRIFIED_SKY));
	}
}
