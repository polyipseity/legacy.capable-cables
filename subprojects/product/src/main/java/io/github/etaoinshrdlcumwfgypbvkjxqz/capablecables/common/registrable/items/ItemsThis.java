package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks.BlocksThis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.groups.ItemGroupsThis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIDebugMinecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public enum ItemsThis {
	;

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MOD_ID);

	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", ItemWrench::new);

	public static final RegistryObject<Item> CABLE = ITEMS.register(BlocksThis.CABLE.getId().getPath(), () -> new BlockItem(BlocksThis.CABLE.orElseThrow(InternalError::new), new Item.Properties().group(ItemGroupsThis.DEFAULT)));

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Item> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? ITEMS.register(UIDebugMinecraft.getPath(), () -> new BlockItem(UIDebugMinecraft.getBlockEntry(), new Item.Properties().group(ItemGroupsThis.DEFAULT))) : null;
	}
}
