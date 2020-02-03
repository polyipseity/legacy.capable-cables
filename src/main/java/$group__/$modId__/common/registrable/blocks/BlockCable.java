package $group__.$modId__.common.registrable.blocks;

import $group__.$modId__.common.registrable.materials.Materials;
import net.minecraft.block.Block;

import static $group__.$modId__.utilities.helpers.Throwables.requireRunOnceOnly;

public class BlockCable extends Block {
	/* SECTION constructors */

	protected BlockCable() {
		super(Materials.PETRIFIED_SKY);
		requireRunOnceOnly();
	}
}
