package $group__.$modId__.proxies;

import $group__.$modId__.ModOwn;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References;
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
		EVENT_BUS.register(References.Client.class);
	}


	/* SECTION static variables */

	public static final String CLASS_SIMPLE_NAME = "ProxyClient";
}
