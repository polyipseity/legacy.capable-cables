package $group__.common.inventory.bases;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

public enum ContainerBases {
	/* MARK empty */;


	/* SECTION static methods */

	public static boolean canInteractWithBase() { return true; }

	public static ItemStack transferStackInSlotBase(Container container, EntityPlayer playerIn, int index,
	                                                BiFunction<ItemStack, Integer, BiFunction<Integer, Boolean,
			                                                Boolean>> mergeItemStackFunction) {
		List<Slot> inv = container.inventorySlots;
		Slot s = inv.get(index);
		if (s != null && s.getHasStack()) {
			ItemStack ss = s.getStack();
			ItemStack r = ss.copy();

			int is = inv.size();
			int cs = is - playerIn.inventory.mainInventory.size();
			boolean reverse = index < cs;
			if (!mergeItemStackFunction.apply(ss, reverse ? cs : 0).apply(reverse ? is : cs, reverse))
				return ItemStack.EMPTY;

			if (ss.getCount() == 0) s.putStack(ItemStack.EMPTY);
			else s.onSlotChanged();

			if (ss.getCount() == r.getCount()) return ItemStack.EMPTY;

			s.onTake(playerIn, ss);
			return r;
		}
		return ItemStack.EMPTY;
	}
}
