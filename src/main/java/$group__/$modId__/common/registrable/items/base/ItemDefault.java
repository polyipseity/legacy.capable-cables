package $group__.$modId__.common.registrable.items.base;

import $group__.$modId__.common.registrable.utilities.constructs.IForgeRegistryEntryExtension;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static $group__.$modId__.common.registrable.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;

public class ItemDefault extends Item implements IForgeRegistryEntryExtension<Item> {
	/* SECTION methods */

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public Item setUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }
}
