package $group__.common.registrables.items.bases;

import $group__.common.registrables.utilities.helpers.Registries;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public enum ItemBases {
	/* MARK empty */;


	/* SECTION static methods */

	public static Item setUnlocalizedNameBase(final Item thisObj, ResourceLocation name) { return thisObj.setUnlocalizedName(Registries.getNamespacedUnlocalizedNameForRegistry(name)); }
}
