package etaoinshrdlcumwfgypbvkjxqz.$modId__.proxies;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.ModOwn;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.NumberRelativeDisplay;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public final class ProxyClient extends Proxy {
	/* SECTION methods */

	@Override
	public void preInitialize(ModOwn mod, FMLPreInitializationEvent e) {
		super.preInitialize(mod, e);
		EVENT_BUS.register(NumberRelativeDisplay.class);
	}


	/* SECTION static variables */

	public static final String CLASS_SIMPLE_NAME = "ProxyClient";
}
