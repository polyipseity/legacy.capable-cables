package $group__.common.inventory;

import $group__.client.gui.GuiWrench;
import $group__.client.gui.themes.EnumTheme;
import $group__.common.gui.GuiHandler;
import $group__.utilities.concurrent.MutatorMutable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.Optional;

import static $group__.Globals.LOGGER;
import static $group__.common.inventory.bases.ContainerBases.canInteractWithBase;
import static $group__.common.inventory.bases.ContainerBases.transferStackInSlotBase;

public class ContainerWrench<T extends ContainerWrench<T>> extends Container {
	@SuppressWarnings("NewExpressionSideOnly")
	public static final int ID =
			GuiHandler.INSTANCE.registerGui((side, id, player, world, hand, u, u1) -> Optional.of(side.isClient() ?
					new GuiWrench<>(new ContainerWrench<>(), 0, EnumTheme.NONE, MutatorMutable.INSTANCE, LOGGER) :
					new ContainerWrench<>()));


	protected ContainerWrench() { super(); }


	@Override
	public boolean canInteractWith(EntityPlayer playerIn) { return canInteractWithBase(); }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return transferStackInSlotBase(this,
				playerIn, index, (p0, p1) -> (p2, p3) -> mergeItemStack(p0, p1, p2, p3));
	}
}
