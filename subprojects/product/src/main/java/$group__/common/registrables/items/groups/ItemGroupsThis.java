package $group__.common.registrables.items.groups;

import $group__.common.registrables.items.ItemsThis;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.OnlyIn;

import static $group__.ModThis.getNamespacePrefixedString;

public enum ItemGroupsThis {
	;

	public static final ItemGroup DEFAULT = new ItemGroup(getNamespacePrefixedString(".", "default")) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() { return new ItemStack(ItemsThis.WRENCH.orElseThrow(InternalError::new)); }
	}.setTabPath("default");
}
