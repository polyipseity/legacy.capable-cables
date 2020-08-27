package $group__.server;

import $group__.proxies.Proxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.DEDICATED_SERVER)
public final class ProxyServer extends Proxy implements IProxyServer {
	public ProxyServer(Logger logger) { super(logger); }

	@Override
	public boolean onModLifecycle(ModLifecycleEvent event) {
		if (super.onModLifecycle(event))
			return true;
		else if (event instanceof FMLDedicatedServerSetupEvent)
			return processEvent("Server setup", event, this::onSetupSided);
		return false;
	}
}
