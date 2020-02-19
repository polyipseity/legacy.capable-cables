package $group__.$modId__.common.registrables.items.bases;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static $group__.$modId__.common.registrables.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;

public enum ItemBases {
	/* MARK empty */;


	/* SECTION static methods */

	@SuppressWarnings("SpellCheckingInspection")
	public static Item setUnlocalizedNameBase(final Item thisObj, ResourceLocation name) { return thisObj.setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }
}
