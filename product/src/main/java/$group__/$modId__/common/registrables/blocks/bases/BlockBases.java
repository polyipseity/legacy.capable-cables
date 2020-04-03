package $group__.$modId__.common.registrables.blocks.bases;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import static $group__.$modId__.common.registrables.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;

public enum BlockBases {
	/* MARK empty */;


	/* SECTION static methods */

	@SuppressWarnings("SpellCheckingInspection")
	public static Block setUnlocalizedNameBase(final Block thisObj, ResourceLocation name) { return thisObj.setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }
}
