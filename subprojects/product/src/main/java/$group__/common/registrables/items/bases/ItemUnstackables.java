package $group__.common.registrables.items.bases;

import net.minecraft.item.Item;

@SuppressWarnings("SpellCheckingInspection")
public enum ItemUnstackables {
	/* MARK empty */;


	public static void initUnstackable(Item thisObj) { thisObj.setMaxStackSize(1); }
}
