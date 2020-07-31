package $group__.common.registrables.items.groups;

import $group__.common.registrables.items.ItemsThis;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.OnlyIn;

import static $group__.ModThis.getNamespacePrefixedString;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

public enum ItemGroupsThis {
	;

	public static final ItemGroup DEFAULT = new ItemGroup(getNamespacePrefixedString(".", "default")) {
		@Override
		@OnlyIn(CLIENT)
		public ItemStack createIcon() { return new ItemStack(ItemsThis.WRENCH.orElseThrow(BecauseOf::unexpected)); }
	}.setTabPath("default");
}
