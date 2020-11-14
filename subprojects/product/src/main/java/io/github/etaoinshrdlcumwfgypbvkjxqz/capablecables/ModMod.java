package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.client.ModClientProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.core.IProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.server.ModServerProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.NamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.ConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.LoggingThrowableHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThreadLocalThrowableHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.jetbrains.annotations.NonNls;

import java.util.ResourceBundle;

@Mod(ModConstants.MOD_ID)
public final class ModMod {
	static {
		ConfigurationTemplate.configureSafe(UtilitiesConfiguration.getInstance(),
				() -> new UtilitiesConfiguration.ConfigurationData(UtilitiesConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), UtilitiesConfiguration.getBootstrapLogger()),
						MinecraftLocaleUtilities::getCurrentLocale)); // COMMENT configure as early as possible
		ConfigurationTemplate.configureSafe(ModConfiguration.getInstance(),
				() -> new ModConfiguration.ConfigurationData(ModConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), ModConfiguration.getBootstrapLogger()),
						MinecraftLocaleUtilities::getCurrentLocale));
	}

	public ModMod() {
		// COMMENT entry point
		// COMMENT by doing this, we can adopt safe construction instead of resorting to 'instance = this' ugliness
		getInstance().initialize();
	}

	public static ModMod.Instance getInstance() { return Instance.INSTANCE; }

	public static ResourceLocation createResourceLocation(@NonNls CharSequence path) { return new ResourceLocation(ModConstants.getModID(), path.toString()); }

	public static String getNamespacePrefixedString(@NonNls CharSequence separator, @NonNls CharSequence string) { return NamespaceUtilities.getNamespacePrefixedString(separator, ModConstants.getModID(), string); }

	public enum Instance {
		INSTANCE,
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());

		private final IProxy proxy = DistExecutor.unsafeRunForDist(
				() -> DistLambdaHolder.Client::getProxyClient,
				() -> DistLambdaHolder.Server::getProxyServer);

		private void initialize() {
			// COMMENT events
			Bus.MOD.bus().get().addListener(this::onModLifecycleEvent);
			onModLifecycleEvent(null); // COMMENT emulate construction event
		}

		private void onModLifecycleEvent(@Nullable ModLifecycleEvent event) {
			if (!getProxy().onModLifecycle(event))
				ModConfiguration.getInstance().getLogger()
						.atInfo()
						.addMarker(ModMarkers.getInstance().getMarkerLifecycle())
						.addKeyValue("event", event)
						.log(() -> getResourceBundle().getString("event.lifecycle.unhandled"));
		}

		public IProxy getProxy() { return proxy; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}

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
