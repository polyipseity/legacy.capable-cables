package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.inventory;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.GuiWrench;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.gui.GuiHandler;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.inventory.templates.ContainerDefault;
import net.minecraft.util.EnumHand;

public class ContainerWrench extends ContainerDefault {
	@SuppressWarnings("NewExpressionSideOnly")
	public static final int ID = GuiHandler.INSTANCE.registerGui((side, ID, player, world, hand, u, u1) -> side.isClient() ? new GuiWrench(new ContainerWrench(), player.getHeldItem(EnumHand.values()[hand])) : new ContainerWrench());

	public ContainerWrench() {
	}
}
