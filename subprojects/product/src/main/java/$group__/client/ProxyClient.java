package $group__.client;

import $group__.ModConfiguration;
import $group__.proxies.Proxy;
import $group__.ui.UIConfiguration;
import $group__.ui.UIConstants;
import $group__.ui.debug.UIDebugMinecraft;
import $group__.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.templates.ConfigurationTemplate;
import $group__.utilities.throwable.LoggingThrowableHandler;
import $group__.utilities.throwable.ThreadLocalThrowableHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.slf4j.Logger;

import java.util.ResourceBundle;

@OnlyIn(Dist.CLIENT)
public final class ProxyClient extends Proxy implements IProxyClient {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());

	public ProxyClient(Logger logger) { super(logger); }

	@Override
	public boolean onModLifecycle(ModLifecycleEvent event) {
		if (super.onModLifecycle(event))
			return true;
		else if (event instanceof FMLClientSetupEvent)
			return processEvent(getResourceBundle().getString("event.client_setup.name"), event, this::onSetupSided);
		return false;
	}

	@Override
	public void onSetupClient(FMLClientSetupEvent event) {
		ConfigurationTemplate.configureSafe(UIConfiguration.getInstance(),
				() -> new UIConfiguration.ConfigurationData(UIConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), UIConfiguration.getBootstrapLogger()),
						MinecraftLocaleUtilities::getCurrentLocale));
		if (UIConstants.BUILD_TYPE.isDebug())
			UIDebugMinecraft.registerUIFactory();
	}

	@Override
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		UIConfiguration.MinecraftSpecific.loadComplete();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
