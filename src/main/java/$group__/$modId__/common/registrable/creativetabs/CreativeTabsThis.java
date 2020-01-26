package $group__.$modId__.common.registrable.creativetabs;

import $group__.$modId__.common.registrable.creativetabs.templates.CreativeTabsDefault;
import $group__.$modId__.common.registrable.items.ItemsThis;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

import static $group__.$modId__.utilities.variables.Constants.MOD_ID;

public enum CreativeTabsThis {
	/* MARK empty */ ;


	/* SECTION static variables */

	public static final CreativeTabs DEFAULT = new CreativeTabsDefault(MOD_ID) {
		/** {@inheritDoc} */
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() { return new ItemStack(ItemsThis.INSTANCE.wrench); }
	};


	/* REFERENCE creative tab template */

	// COMMENT using a function here to avoid registering the tab
	@SuppressWarnings("unused")
	private static final Function<CreativeTabsThis, CreativeTabs> TEMPLATE_ = t -> new CreativeTabsDefault("template_") {
		/** {@inheritDoc} */
		@Override
		@SideOnly(Side.CLIENT) // COMMENT IMPORTANT!
		public ItemStack getTabIconItem() { return new ItemStack(Items.AIR); }
		// COMMENT see superclass for optional overrides
	}.setNoTitle()
			.setNoScrollbar()
			.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL)
			.setBackgroundImageName("template_");
}
