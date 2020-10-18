package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.groups;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModMod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ModItemGroups {
	;

	private static final ItemGroup DEFAULT = new ItemGroup(ModMod.getNamespacePrefixedString(".", "default")) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() { return new ItemStack(ModItems.getWrench().orElseThrow(InternalError::new)); }
	}.setTabPath("default");

	public static ItemGroup getDefault() {
		return DEFAULT;
	}
}
