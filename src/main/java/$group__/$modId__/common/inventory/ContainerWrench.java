package $group__.$modId__.common.inventory;

import $group__.$modId__.client.gui.GuiWrench;
import $group__.$modId__.common.gui.GuiHandler;
import $group__.$modId__.traits.IStructure;
import $group__.$modId__.traits.extensions.ICloneable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.Optional;

import static $group__.$modId__.common.inventory.bases.ContainerBases.canInteractWithBase;
import static $group__.$modId__.common.inventory.bases.ContainerBases.transferStackInSlotBase;

public class ContainerWrench<T extends ContainerWrench<T>> extends Container implements IStructure<T>, ICloneable<T> {
	/* SECTION static variables */

	public static final ContainerWrench<?> INSTANCE = new ContainerWrench<>();
	@SuppressWarnings("NewExpressionSideOnly")
	public static final int ID = GuiHandler.INSTANCE.registerGui((side, id, player, world, hand, u, u1) -> Optional.of(side.isClient() ? new GuiWrench(INSTANCE.copy(), player.getHeldItem(EnumHand.values()[hand]), ) : INSTANCE.copy()));


	/* SECTION constructors */

	protected ContainerWrench() { super(); }


	/* SECTION methods */

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) { return canInteractWithBase(); }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) { return transferStackInSlotBase(this, playerIn, index, (p0, p1) -> (p2, p3) -> mergeItemStack(p0, p1, p2, p3)); }
}
