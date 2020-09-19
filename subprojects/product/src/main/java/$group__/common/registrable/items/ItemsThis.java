package $group__.common.registrable.items;

import $group__.common.registrable.blocks.BlocksThis;
import $group__.common.registrable.items.groups.ItemGroupsThis;
import $group__.ui.UIConstants;
import $group__.ui.debug.UIDebugMinecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static $group__.ModConstants.MOD_ID;

public enum ItemsThis {
	;

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", ItemWrench::new);

	public static final RegistryObject<Item> CABLE = ITEMS.register(BlocksThis.CABLE.getId().getPath(), () -> new BlockItem(BlocksThis.CABLE.orElseThrow(InternalError::new), new Item.Properties().group(ItemGroupsThis.DEFAULT)));

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Item> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? ITEMS.register(UIDebugMinecraft.getPATH(), () -> new BlockItem(UIDebugMinecraft.getBlockEntry(), new Item.Properties().group(ItemGroupsThis.DEFAULT))) : null;
	}
}
