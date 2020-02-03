package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IContainerWrapped {
	/* SECTION methods */

	Slot addSlotToContainerPublic(Slot slotIn);

	void clearContainerPublic(EntityPlayer playerIn, World worldIn, IInventory inventoryIn);

	boolean mergeItemStackPublic(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection);

	void resetDragPublic();

	void slotChangedCraftingGridPublic(World worldIn, EntityPlayer playerIn, InventoryCrafting craftMatrix, InventoryCraftResult result);


	/* SECTION static classes */

	class Impl<T extends Container> extends Container implements IContainerWrapped, ICloneable<T> {
		/* SECTION methods */

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) { return true; }

		@Override
		public Slot addSlotToContainerPublic(Slot slotIn) { return super.addSlotToContainer(slotIn); }

		@Override
		public void clearContainerPublic(EntityPlayer playerIn, World worldIn, IInventory inventoryIn) { super.clearContainer(playerIn, worldIn, inventoryIn); }

		@Override
		public boolean mergeItemStackPublic(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) { return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection); }

		@Override
		public void resetDragPublic() { super.resetDrag(); }

		@Override
		public void slotChangedCraftingGridPublic(World worldIn, EntityPlayer playerIn, InventoryCrafting craftMatrix, InventoryCraftResult result) { super.slotChangedCraftingGrid(worldIn, playerIn, craftMatrix, result); }


		@SuppressWarnings("Convert2MethodRef")
		@Override
		@OverridingMethodsMustInvokeSuper
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public T clone() { return ICloneable.clone(() -> super.clone()); }
	}
}
