package etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.concurrent.Immutable;

// Is this class of any use?
@SuppressWarnings("unused")
@Immutable
@SideOnly(Side.SERVER)
public final class ProxyServer extends Proxy {
    public static final String CLASS_SIMPLE_NAME = "ProxyServer";
    public ProxyServer() { super(); }
}
