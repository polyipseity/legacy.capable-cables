package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.client.ModClientProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.IProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.server.ModServerProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.NamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.ConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.LoggingThrowableHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThreadLocalThrowableHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.util.ResourceBundle;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks.BlocksThis.BLOCKS;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory.ContainersThis.CONTAINERS;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ItemsThis.ITEMS;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.tileentities.TileEntityTypesThis.TILE_ENTITIES;

@Mod(ModConstants.MOD_ID)
public final class ModThis {
	@Nullable
	private static volatile ModThis instance = null;
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

	private final IProxy<?> proxy = DistExecutor.unsafeRunForDist(
			() -> DistLambdaHolder.Client::getProxyClient,
			() -> DistLambdaHolder.Server::getProxyServer);

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public ModThis() {
		// COMMENT entry point
		IEventBus eventBusMod = AssertionUtilities.assertNonnull(Bus.MOD.bus().get());
		eventBusMod.register(this);
		BLOCKS.register(eventBusMod);
		ITEMS.register(eventBusMod);
		TILE_ENTITIES.register(eventBusMod);
		CONTAINERS.register(eventBusMod);

		setInstance(this); // COMMENT would really love to have a way to specify an alternative way to provide the instance
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static ResourceLocation createResourceLocation(@NonNls String path) { return new ResourceLocation(ModConstants.MOD_ID, path); }

	public static String getNamespacePrefixedString(@NonNls String separator, @NonNls String string) { return NamespaceUtilities.getNamespacePrefixedString(separator, ModConstants.MOD_ID, string); }

	public IProxy<?> getProxy() { return proxy; }

	public static ModThis getInstance() { return AssertionUtilities.assertNonnull(instance); }

	private static void setInstance(@Nullable ModThis instance) { ModThis.instance = instance; }

	@SubscribeEvent
	protected void onModLifecycleEvent(ModLifecycleEvent event) {
		if (!getProxy().onModLifecycle(event))
			ModConfiguration.getInstance().getLogger()
					.atInfo()
					.addMarker(ModMarkers.getInstance().getMarkerModLifecycle())
					.addKeyValue("event", event)
					.log(() -> getResourceBundle().getString("event.lifecycle.unhandled"));
	}

	private enum DistLambdaHolder {
		;

		private enum Client {
			;

			private static IProxy<?> getProxyClient() { return ModClientProxy.getInstance(); }
		}

		private enum Server {
			;

			private static IProxy<?> getProxyServer() { return ModServerProxy.getInstance(); }
		}
	}
}
