package $group__.common.registrables.items;

import $group__.client.gui.debug.GuiComponentDebug;
import $group__.common.registrables.blocks.BlocksThis;
import $group__.common.registrables.items.groups.ItemGroupsThis;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static $group__.Constants.MOD_ID;

public enum ItemsThis {
	;

	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", ItemWrench::new);

	public static final RegistryObject<Item> CABLE = ITEMS.register(BlocksThis.CABLE.getId().getPath(), () -> new BlockItem(BlocksThis.CABLE.orElseThrow(InternalError::new), new Item.Properties().group(ItemGroupsThis.DEFAULT)));

	// todo add debug flag
	@SuppressWarnings("unused")
	private static final RegistryObject<Item> DEBUG_GUI_COMPONENT = ITEMS.register(GuiComponentDebug.PATH, () -> new BlockItem(GuiComponentDebug.getBlockEntry(), new Item.Properties().group(ItemGroupsThis.DEFAULT)));
}
