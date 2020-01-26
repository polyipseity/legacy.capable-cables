package $group__.$modId__.common.inventory;

import $group__.$modId__.client.gui.GuiWrench;
import $group__.$modId__.common.gui.GuiHandler;
import $group__.$modId__.common.inventory.templates.ContainerDefault;
import net.minecraft.util.EnumHand;

import java.util.Optional;

public class ContainerWrench extends ContainerDefault {
	/* SECTION static variables */

	@SuppressWarnings("NewExpressionSideOnly")
	public static final int ID = GuiHandler.INSTANCE.registerGui((side, id, player, world, hand, u, u1) -> Optional.of(side.isClient() ? new GuiWrench(new ContainerWrench(), player.getHeldItem(EnumHand.values()[hand])) : new ContainerWrench()));
}
