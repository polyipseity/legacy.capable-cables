package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks.ModBlocks;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.groups.ModItemGroups;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIMinecraftDebug;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public enum ModItems {
	;

	private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.getModID());

	private static final RegistryObject<Item> WRENCH = getRegister().register("wrench", WrenchItem::new);

	private static final RegistryObject<Item> CABLE = getRegister().register(ModBlocks.getCable().getId().getPath(), () -> new BlockItem(ModBlocks.getCable().orElseThrow(InternalError::new), new Item.Properties().group(ModItemGroups.getDefault())));

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Item> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.getBuildType().isDebug()
				? getRegister().register(UIMinecraftDebug.getPath(), () -> new BlockItem(UIMinecraftDebug.getBlockEntry(), new Item.Properties().group(ModItemGroups.getDefault())))
				: null;
	}

	public static DeferredRegister<Item> getRegister() {
		return REGISTER;
	}

	public static RegistryObject<Item> getWrench() {
		return WRENCH;
	}

	public static RegistryObject<Item> getCable() {
		return CABLE;
	}

	@SuppressWarnings("unused")
	@Nullable
	private static RegistryObject<Item> getDebugUI() {
		return DEBUG_UI;
	}
}
