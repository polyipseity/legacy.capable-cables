package $group__.client;

import $group__.Constants;
import $group__.client.ui.ConfigurationUI;
import $group__.client.ui.ConstantsUI;
import $group__.client.ui.debug.UIDebugMinecraft;
import $group__.proxies.Proxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public final class ProxyClient extends Proxy implements IProxyClient {
	public ProxyClient(Logger logger) { super(logger); }

	@Override
	public boolean onModLifecycle(ModLifecycleEvent event) {
		if (super.onModLifecycle(event)) return true;
		else if (event instanceof FMLClientSetupEvent)
			return processEvent("Client setup", event, this::onSetupSided);
		return false;
	}

	@Override
	public void onSetupClient(FMLClientSetupEvent event) {
		ConfigurationUI.setup(Constants.MOD_ID);

		if (ConstantsUI.BUILD_TYPE.isDebug())
			UIDebugMinecraft.registerUIFactory();
	}

	@Override
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		ConfigurationUI.loadComplete();
	}
}
