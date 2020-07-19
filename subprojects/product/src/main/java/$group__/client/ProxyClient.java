package $group__.client;

import $group__.client.gui.debug.GuiComponentDebug;
import $group__.proxies.Proxy;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
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
		// todo add debug flag
		GuiComponentDebug.registerGuiFactory();
	}
}
