package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.client;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.Proxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIDebugMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.ConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.LoggingThrowableHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThreadLocalThrowableHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import java.util.ResourceBundle;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class ProxyClient extends Proxy implements IProxyClient {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());
	private static final Supplier<ProxyClient> INSTANCE = Suppliers.memoize(ProxyClient::new);

	private ProxyClient() {}

	public static ProxyClient getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

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
