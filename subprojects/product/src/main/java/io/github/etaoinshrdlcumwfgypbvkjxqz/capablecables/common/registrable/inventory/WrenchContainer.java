package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory.bases.ContainerBases;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;

public class WrenchContainer<T extends WrenchContainer<T>> extends Container {
	protected WrenchContainer(@Nullable ContainerType<?> type, int id) {
		super(type, id);
	}

	@Override
	public @Nonnull ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		return ContainerBases.transferStackInSlotBase(this,
				playerIn, index, (p0, p1) -> (p2, p3) -> mergeItemStack(p0, p1, p2, p3));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) { return ContainerBases.canInteractWithBase(); }
}
