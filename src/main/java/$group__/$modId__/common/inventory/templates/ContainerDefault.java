package $group__.$modId__.common.inventory.templates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerDefault extends Container {
	/* SECTION methods */

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot s = inventorySlots.get(index);
		if (s != null && s.getHasStack()) {
			ItemStack ss = s.getStack();
			ItemStack r = ss.copy();

			int is = inventorySlots.size();
			int cs = is - playerIn.inventory.mainInventory.size();
			boolean reverse = index < cs;
			if (!mergeItemStack(ss, reverse ? cs : 0, reverse ? is : cs, reverse)) return ItemStack.EMPTY;

			if (ss.getCount() == 0) s.putStack(ItemStack.EMPTY);
			else s.onSlotChanged();

			if (ss.getCount() == r.getCount()) return ItemStack.EMPTY;

			s.onTake(playerIn, ss);
			return r;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) { return true; }
}
