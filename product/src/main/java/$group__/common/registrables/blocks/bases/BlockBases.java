package $group__.common.registrables.blocks.bases;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import static $group__.common.registrables.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;

public enum BlockBases {
	/* MARK empty */;


	/* SECTION static methods */

	public static Block setUnlocalizedNameBase(final Block thisObj, ResourceLocation name) { return thisObj.setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }
}
