package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.client.ModClientProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.core.IProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.server.ModServerProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.NamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.ConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.LoggingThrowableHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThreadLocalThrowableHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.jetbrains.annotations.NonNls;

import java.util.ResourceBundle;

@Mod(ModConstants.MOD_ID)
public final class ModMod {
	@Nullable
	private static volatile ModMod instance = null;
	private static final ResourceBundle RESOURCE_BUNDLE;

	static {
		ConfigurationTemplate.configureSafe(UtilitiesConfiguration.getInstance(),
				() -> new UtilitiesConfiguration.ConfigurationData(UtilitiesConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), UtilitiesConfiguration.getBootstrapLogger()),
						MinecraftLocaleUtilities::getCurrentLocale)); // COMMENT configure as early as possible
		ConfigurationTemplate.configureSafe(ModConfiguration.getInstance(),
				() -> new ModConfiguration.ConfigurationData(ModConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), ModConfiguration.getBootstrapLogger()),
						MinecraftLocaleUtilities::getCurrentLocale));
		RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());
	}

	private final IProxy proxy = DistExecutor.unsafeRunForDist(
			() -> DistLambdaHolder.Client::getProxyClient,
			() -> DistLambdaHolder.Server::getProxyServer);

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public ModMod() {
		// COMMENT entry point
		setInstance(this); // COMMENT would really love to have a way to specify an alternative way to provide the instance

		// COMMENT events
		IEventBus modEventBus = AssertionUtilities.assertNonnull(Bus.MOD.bus().get());
		modEventBus.addListener(getInstance()::onModLifecycleEvent);
		onModLifecycleEvent(null); // COMMENT emulate construction event
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static ResourceLocation createResourceLocation(@NonNls CharSequence path) { return new ResourceLocation(ModConstants.getModID(), path.toString()); }

	public static String getNamespacePrefixedString(@NonNls CharSequence separator, @NonNls CharSequence string) { return NamespaceUtilities.getNamespacePrefixedString(separator, ModConstants.getModID(), string); }

	public static ModMod getInstance() { return AssertionUtilities.assertNonnull(instance); }

	private static void setInstance(ModMod instance) { ModMod.instance = instance; }

	private void onModLifecycleEvent(@Nullable ModLifecycleEvent event) {
		if (!getProxy().onModLifecycle(event))
			ModConfiguration.getInstance().getLogger()
					.atInfo()
					.addMarker(ModMarkers.getInstance().getMarkerModLifecycle())
					.addKeyValue("event", event)
					.log(() -> getResourceBundle().getString("event.lifecycle.unhandled"));
	}

	public IProxy getProxy() { return proxy; }

	private enum DistLambdaHolder {
		;

		private enum Client {
			;

			private static IProxy getProxyClient() { return ModClientProxy.getInstance(); }
		}

		private enum Server {
			;

			private static IProxy getProxyServer() { return ModServerProxy.getInstance(); }
		}
	}
}
