package etaoinshrdlcumwfgypbvkjxqz.$modId__.common.inventory;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.GuiWrench;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.gui.GuiHandler;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.inventory.templates.ContainerDefault;
import net.minecraft.util.EnumHand;

public class ContainerWrench extends ContainerDefault {
	/* SECTION static variables */

	@SuppressWarnings("NewExpressionSideOnly")
	public static final int ID = GuiHandler.INSTANCE.registerGui((side, id, player, world, hand, u, u1) -> side.isClient() ? new GuiWrench(new ContainerWrench(), player.getHeldItem(EnumHand.values()[hand])) : new ContainerWrench());
}
