package $group__.common.registrables.creativetabs;

import $group__.common.registrables.creativetabs.bases.CreativeTabsBases;
import $group__.common.registrables.items.ItemsThis;
import $group__.utilities.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Supplier;

public enum CreativeTabsThis {
	/* MARK empty */;


	/* SECTION static variables */

	public static final CreativeTabs DEFAULT = new CreativeTabs(CreativeTabsBases.initBaseLabel(Constants.MOD_ID)) {
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() { return new ItemStack(ItemsThis.INSTANCE.wrench); }
	};

	/* REFERENCE creative tab template */

	// COMMENT using a function here to avoid registering the tab
	@SuppressWarnings("unused")
	private static final Supplier<CreativeTabs> TEMPLATE_ = () -> new CreativeTabs(CreativeTabsBases.initBaseLabel("template_")) {
		@Override
		@SideOnly(Side.CLIENT) // COMMENT IMPORTANT!
		public ItemStack getTabIconItem() { return new ItemStack(Items.AIR); }
		// COMMENT see superclass for optional overrides
	}.setNoTitle()
			.setNoScrollbar()
			.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL)
			.setBackgroundImageName("template_");
}
