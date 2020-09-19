package $group__.server;

import $group__.ModConfiguration;
import $group__.proxies.Proxy;
import $group__.utilities.templates.CommonConfigurationTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.slf4j.Logger;

import java.util.ResourceBundle;

@OnlyIn(Dist.DEDICATED_SERVER)
public final class ProxyServer extends Proxy implements IProxyServer {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());

	@Override
	public boolean onModLifecycle(ModLifecycleEvent event) {
		if (super.onModLifecycle(event))
			return true;
		else if (event instanceof FMLDedicatedServerSetupEvent)
			return processEvent(getResourceBundle().getString("event.server_setup.name"), event, this::onSetupSided);
		return false;
	}

	public ProxyServer(Logger logger) { super(logger); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
