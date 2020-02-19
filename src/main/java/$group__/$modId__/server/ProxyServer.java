package $group__.$modId__.server;

import $group__.$modId__.proxies.Proxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public final class ProxyServer extends Proxy {
	/* SECTION static variables */

	public static final String CLASS_SIMPLE_NAME = "ProxyServer";
}
