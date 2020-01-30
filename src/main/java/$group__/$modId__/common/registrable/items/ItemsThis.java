package $group__.$modId__.common.registrable.items;

import $group__.$modId__.common.registrable.Registrable;
import $group__.$modId__.common.registrable.creativetabs.CreativeTabsThis;
import $group__.$modId__.common.registrable.items.templates.ItemDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static $group__.$modId__.utilities.variables.Constants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class ItemsThis extends Registrable<Item> {
	/* SECTION static variables */

	public static final ItemsThis INSTANCE = getInstance(ItemsThis.class);

	/* REFERENCE item template */
	@SuppressWarnings("unused")
	private static final Item TEMPLATE_ = new ItemDefault() {{
		// COMMENT below one is required
		setRegistryName(MOD_ID, "template_");
		// COMMENT all below are optional
		setUnlocalizedName("template_");
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(69);
		setFull3D();
		// COMMENT subclass is probably needed for below properties
		setHasSubtypes(true);
		setMaxDamage(1337);
		setNoRepair();
		setContainerItem(Items.BUCKET);
		setHarvestLevel("template_", 9001);
		// COMMENT overrides are required for properties unlisted here
	}};


	/* SECTION variables */

	@SuppressWarnings("unused")
	public final Item wrench = new ItemWrench().setRegistryAndUnlocalizedName(MOD_ID, "wrench").setCreativeTab(CreativeTabsThis.DEFAULT);


	/* SECTION static methods */

	@SubscribeEvent
	public static void registerStatic(RegistryEvent.Register<Item> event) { INSTANCE.register(event); }
}
