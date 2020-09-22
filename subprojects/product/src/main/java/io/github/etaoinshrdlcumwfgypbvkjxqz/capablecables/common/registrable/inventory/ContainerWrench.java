package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory.bases.ContainerBases;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ContainerWrench<T extends ContainerWrench<T>> extends Container {
	protected ContainerWrench(@Nullable ContainerType<?> type, int id) {
		super(type, id);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		return ContainerBases.transferStackInSlotBase(this,
				playerIn, index, (p0, p1) -> (p2, p3) -> mergeItemStack(p0, p1, p2, p3));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) { return ContainerBases.canInteractWithBase(); }
}
