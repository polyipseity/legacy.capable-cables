package $group__.$modId__.common.registrable.blocks;

import $group__.$modId__.common.registrable.blocks.templates.BlockDefault;
import $group__.$modId__.common.registrable.materials.Materials;

import static $group__.$modId__.utilities.helpers.Throwables.requireRunOnceOnly;

public class BlockCable extends BlockDefault {
	/* SECTION constructors */

	protected BlockCable() {
		super(Materials.PETRIFIED_SKY);
		requireRunOnceOnly();
	}
}
