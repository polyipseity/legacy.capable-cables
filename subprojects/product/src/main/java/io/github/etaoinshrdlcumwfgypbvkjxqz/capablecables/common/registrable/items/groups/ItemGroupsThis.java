package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.groups;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModThis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ItemsThis;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ItemGroupsThis {
	;

	private static final ItemGroup DEFAULT = new ItemGroup(ModThis.getNamespacePrefixedString(".", "default")) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() { return new ItemStack(ItemsThis.getWrench().orElseThrow(InternalError::new)); }
	}.setTabPath("default");

	public static ItemGroup getDefault() {
		return DEFAULT;
	}
}
