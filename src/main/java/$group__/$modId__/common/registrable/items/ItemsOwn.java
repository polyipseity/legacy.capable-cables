package $group__.$modId__.common.registrable.items;

import $group__.$modId__.common.registrable.Registrable;
import $group__.$modId__.common.registrable.creativetabs.CreativeTabsOwn;
import $group__.$modId__.common.registrable.items.templates.ItemDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static $group__.$modId__.utilities.variables.Constants.MOD_ID;

public final class ItemsOwn extends Registrable<Item> {
	/* SECTION variables */

	@SuppressWarnings("unused")
	public final Item wrench = new ItemWrench().setRegistryAndUnlocalizedName(MOD_ID, "wrench").setCreativeTab(CreativeTabsOwn.DEFAULT);


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	@SubscribeEvent
	public void register(RegistryEvent.Register<Item> e) { super.register(e); }


	/* SECTION static variables */

	public static final ItemsOwn INSTANCE = getInstance(ItemsOwn.class);


	/* REFERENCE item template */

	@SuppressWarnings("unused")
	private static final Item TEMPLATE_ = new ItemDefault()
			// COMMENT below one is required
			.setRegistryName(MOD_ID, "template_")
			// COMMENT all below are optional
			.setUnlocalizedName("template_")
			.setCreativeTab(CreativeTabs.MISC)
			.setMaxStackSize(69)
			.setFull3D()
			// COMMENT subclass is probably needed for below properties (including the static initializer)
			.setHasSubtypes(true)
			.setMaxDamage(1337)
			.setNoRepair()
			.setContainerItem(Items.BUCKET);

	static { TEMPLATE_.setHarvestLevel("template_", 9001); } // COMMENT overrides are required for properties unlisted here
}
