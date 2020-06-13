package $group__.common.registrables.items;

import $group__.Globals;
import $group__.common.registrables.Registrable;
import $group__.common.registrables.creativetabs.CreativeTabsThis;
import $group__.utilities.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class ItemsThis extends Registrable<Item> {
	public static final ItemsThis INSTANCE = getSingletonInstance(ItemsThis.class, Globals.LOGGER);

	/* REFERENCE item template */
	@SuppressWarnings("unused")
	private static final Item TEMPLATE_ = new Item() {{
		// COMMENT below one is required
		setRegistryName(Constants.MOD_ID, "template_");
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


	@SuppressWarnings("unused")
	public final Item wrench =
			new ItemWrench().setRegistryAndUnlocalizedName(Constants.MOD_ID, "wrench").setCreativeTab(CreativeTabsThis.DEFAULT);


	protected ItemsThis() { super(Globals.LOGGER); }


	@SubscribeEvent
	public static void registerStatic(RegistryEvent.Register<Item> event) { INSTANCE.register(event, Globals.LOGGER); }
}
