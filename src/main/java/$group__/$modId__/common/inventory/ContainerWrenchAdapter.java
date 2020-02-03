package $group__.$modId__.common.inventory;

import $group__.$modId__.client.gui.GuiWrench;
import $group__.$modId__.client.gui.utilities.constructs.IContainerWrapped;
import $group__.$modId__.common.gui.GuiHandler;
import $group__.$modId__.common.inventory.bases.ContainerAdapter;
import net.minecraft.util.EnumHand;

import java.util.Optional;

public class ContainerWrenchAdapter<C extends ContainerWrenchAdapter.ContainerWrench<C>> extends ContainerAdapter<C, ContainerWrenchAdapter<C>> {
	/* SECTION static variables */

	public static final ContainerWrenchAdapter<?> CONTAINER = new ContainerWrenchAdapter<>();
	@SuppressWarnings("NewExpressionSideOnly")
	public static final int ID = GuiHandler.INSTANCE.registerGui((side, id, player, world, hand, u, u1) -> Optional.of(side.isClient() ? new GuiWrench(CONTAINER.copy(), player.getHeldItem(EnumHand.values()[hand])) : CONTAINER.copy()));


	/* SECTION constructors */

	public ContainerWrenchAdapter() { super(new ContainerWrench<>()); }


	/* SECTION static classes */

	protected static class ContainerWrench<T extends ContainerWrench<T>> extends IContainerWrapped.Impl<T> {}
}
