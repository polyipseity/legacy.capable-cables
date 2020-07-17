package $group__.common.registrables.blocks;

import $group__.common.registrables.materials.Materials;
import net.minecraft.block.Block;

public class BlockCable extends Block {
	protected BlockCable() {
		super(Properties.create(Materials.PETRIFIED_SKY));
	}
}
