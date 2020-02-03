package $group__.$modId__.common.inventory.bases;

import $group__.$modId__.client.gui.utilities.constructs.IContainerWrapped;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.util.List;

import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.Bulk.copyFields;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public class ContainerAdapter<C extends Container & IContainerWrapped, T extends ContainerAdapter<C, T>> extends Container implements IAdapter.IImmutable<C, T> {
	/* SECTION variables */

	protected final C container;


	/* SECTION constructors */

	public <CL extends ICloneable<C>> ContainerAdapter(CL container) {
		this.container = castUncheckedUnboxedNonnull(container);
		initializeInstance(this);
	}


	/* SECTION static methods */

	private static <T extends ContainerAdapter<?, ?>> T initializeInstance(T t) {
		copyFields(Container.class, t.getContainer(), t);
		return t;
	}


	/* SECTION methods */

	public C getContainer() { return container; }

	@Deprecated
	public void setContainer(@SuppressWarnings("unused") C value) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public C get() { return getContainer(); }

	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public void set(C value) { setContainer(value); }


	@Override
	protected Slot addSlotToContainer(Slot slotIn) { return getContainer().addSlotToContainerPublic(slotIn); }

	@Override
	public void addListener(IContainerListener listener) { getContainer().addListener(listener); }

	@Override
	public NonNullList<ItemStack> getInventory() { return getContainer().getInventory(); }

	@SideOnly(Side.CLIENT)
	@Override
	public void removeListener(IContainerListener listener) { getContainer().removeListener(listener); }

	@Override
	public void detectAndSendChanges() { getContainer().detectAndSendChanges(); }

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id) { return getContainer().enchantItem(playerIn, id); }

	@Nullable
	@Override
	public Slot getSlotFromInventory(IInventory inv, int slotIn) { return getContainer().getSlotFromInventory(inv, slotIn); }

	@Override
	public Slot getSlot(int slotId) { return getContainer().getSlot(slotId); }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Container c = getContainer();
		Slot s = c.inventorySlots.get(index);
		if (s != null && s.getHasStack()) {
			ItemStack ss = s.getStack();
			ItemStack r = ss.copy();

			int is = c.inventorySlots.size();
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
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) { return getContainer().slotClick(slotId, dragType, clickTypeIn, player); }

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) { return getContainer().canMergeSlot(stack, slotIn); }

	@Override
	public void onContainerClosed(EntityPlayer playerIn) { getContainer().onContainerClosed(playerIn); }

	@Override
	protected void clearContainer(EntityPlayer playerIn, World worldIn, IInventory inventoryIn) { getContainer().clearContainerPublic(playerIn, worldIn, inventoryIn); }

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) { getContainer().onCraftMatrixChanged(inventoryIn); }

	@Override
	public void putStackInSlot(int slotID, ItemStack stack) { getContainer().putStackInSlot(slotID, stack); }

	@Override
	@SideOnly(Side.CLIENT)
	public void setAll(List<ItemStack> stacks) { getContainer().setAll(stacks); }

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) { getContainer().updateProgressBar(id, data); }

	@Override
	@SideOnly(Side.CLIENT)
	public short getNextTransactionID(InventoryPlayer invPlayer) { return getContainer().getNextTransactionID(invPlayer); }

	@Override
	public boolean getCanCraft(EntityPlayer player) { return getContainer().getCanCraft(player); }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) { return getContainer().canInteractWith(playerIn); }

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) { return getContainer().mergeItemStackPublic(stack, startIndex, endIndex, reverseDirection); }

	@Override
	protected void resetDrag() { getContainer().resetDragPublic(); }

	@Override
	public boolean canDragIntoSlot(Slot slotIn) { return getContainer().canDragIntoSlot(slotIn); }

	@Override
	protected void slotChangedCraftingGrid(World worldIn, EntityPlayer playerIn, InventoryCrafting craftMatrix, InventoryCraftResult result) { getContainer().slotChangedCraftingGridPublic(worldIn, playerIn, craftMatrix, result); }


	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() {
		return getHashCode(this, super::hashCode);
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() { return initializeInstance(ICloneable.clone(() -> super.clone())); }
}
