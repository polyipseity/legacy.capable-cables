package $group__.$modId__.common.inventory;

import $group__.$modId__.client.gui.GuiWrench;
import $group__.$modId__.client.gui.themes.EnumTheme;
import $group__.$modId__.common.gui.GuiHandler;
import $group__.$modId__.utilities.concurrent.MutatorMutable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.Optional;

import static $group__.$modId__.common.inventory.bases.ContainerBases.canInteractWithBase;
import static $group__.$modId__.common.inventory.bases.ContainerBases.transferStackInSlotBase;
import static $group__.$modId__.Globals.LOGGER;

public class ContainerWrench<T extends ContainerWrench<T>> extends Container {
	/* SECTION static variables */

	@SuppressWarnings("NewExpressionSideOnly")
	public static final int ID = GuiHandler.INSTANCE.registerGui((side, id, player, world, hand, u, u1) -> Optional.of(side.isClient() ? new GuiWrench<>(new ContainerWrench<>(), 0, EnumTheme.NONE, MutatorMutable.INSTANCE, LOGGER) : new ContainerWrench<>()));


	/* SECTION constructors */

	protected ContainerWrench() { super(); }


	/* SECTION methods */

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) { return canInteractWithBase(); }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) { return transferStackInSlotBase(this, playerIn, index, (p0, p1) -> (p2, p3) -> mergeItemStack(p0, p1, p2, p3)); }
}
