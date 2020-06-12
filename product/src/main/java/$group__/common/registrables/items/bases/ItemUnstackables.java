package $group__.common.registrables.items.bases;

import net.minecraft.item.Item;

@SuppressWarnings("SpellCheckingInspection")
public enum ItemUnstackables {
	/* MARK empty */;


	/* SECTION static methods */

	public static void initUnstackable(Item thisObj) { thisObj.setMaxStackSize(1); }
}
