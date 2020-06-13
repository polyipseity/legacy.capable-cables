package $group__.common.registrables.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public enum Materials {
	/* MARK empty */;

	public static final Material PETRIFIED_SKY = new Material(MapColor.PURPLE);


	/* REFERENCE material template */

	@SuppressWarnings("unused")
	private static final Material TEMPLATE_ = new Material(MapColor.AIR)
			.setReplaceable(); // COMMENT overrides are required for unlisted here
}
