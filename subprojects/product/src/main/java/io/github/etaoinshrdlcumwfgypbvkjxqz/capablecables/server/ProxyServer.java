package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.server;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.Proxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import java.util.ResourceBundle;

@OnlyIn(Dist.DEDICATED_SERVER)
public final class ProxyServer extends Proxy implements IProxyServer {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());
	private static final Supplier<ProxyServer> INSTANCE = Suppliers.memoize(ProxyServer::new);

	@Override
	public boolean onModLifecycle(ModLifecycleEvent event) {
		if (super.onModLifecycle(event))
			return true;
		else if (event instanceof FMLDedicatedServerSetupEvent)
			return processEvent(getResourceBundle().getString("event.server_setup.name"), event, this::onSetupSided);
		return false;
	}

	private ProxyServer() {}

	public static ProxyServer getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
