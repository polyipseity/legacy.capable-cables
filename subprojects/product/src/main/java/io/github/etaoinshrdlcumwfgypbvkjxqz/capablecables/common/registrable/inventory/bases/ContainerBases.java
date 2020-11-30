package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory.bases;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IFunction4;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;

@Deprecated
public enum ContainerBases {
	;

	@SuppressWarnings("SameReturnValue")
	public static boolean canInteractWithBase() { return true; }

	public static <TH extends Throwable> ItemStack transferStackInSlotBase(Container container,
	                                                                       PlayerEntity playerIn,
	                                                                       int index,
	                                                                       IFunction4<@Nonnull ? super ItemStack, @Nonnull ? super Integer, @Nonnull ? super Integer, @Nonnull ? super Boolean, @Nonnull ? extends Boolean, ? extends TH> mergeItemStackFunction)
			throws TH {
		List<Slot> inv = container.inventorySlots;
		@Nullable Slot s = inv.get(index);
		if (s != null && s.getHasStack()) {
			ItemStack ss = s.getStack();
			ItemStack r = ss.copy();

			int is = inv.size();
			int cs = is - playerIn.inventory.mainInventory.size();
			boolean reverse = index < cs;
			if (!mergeItemStackFunction.apply(ss, suppressBoxing(reverse ? cs : 0), suppressBoxing(reverse ? is : cs), suppressBoxing(reverse)))
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
