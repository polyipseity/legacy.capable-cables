package $group__.common.registrables.items;

import $group__.common.registrables.items.groups.ItemGroupsThis;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static $group__.Constants.MOD_ID;
import static $group__.common.registrables.blocks.BlocksThis.CABLE;

public enum ItemsThis {
	;

	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", ItemWrench::new);

	public static final RegistryObject<Item> CABLE_ITEM = ITEMS.register(CABLE.getId().getPath(), () -> new BlockItem(CABLE.orElseThrow(BecauseOf::unexpected), new Item.Properties().group(ItemGroupsThis.DEFAULT)));
}
