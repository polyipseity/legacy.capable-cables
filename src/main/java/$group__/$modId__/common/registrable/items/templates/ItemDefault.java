package $group__.$modId__.common.registrable.items.templates;

import $group__.$modId__.common.registrable.utilities.constructs.IForgeRegistryEntryExtension;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static $group__.$modId__.common.registrable.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;

public class ItemDefault extends Item implements IForgeRegistryEntryExtension<Item> {
	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public Item setUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }

	/** {@inheritDoc} */
	@Override
	public Item setRegistryAndUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(name).setRegistryName(name); }
}
