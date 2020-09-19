package $group__;

import $group__.client.ProxyClient;
import $group__.proxies.IProxy;
import $group__.server.ProxyServer;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.UtilitiesConfiguration;
import $group__.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.templates.ConfigurationTemplate;
import com.google.common.base.Suppliers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.jetbrains.annotations.NonNls;

import java.util.ResourceBundle;
import java.util.function.Supplier;

import static $group__.ModConstants.MOD_ID;
import static $group__.common.registrable.blocks.BlocksThis.BLOCKS;
import static $group__.common.registrable.inventory.ContainersThis.CONTAINERS;
import static $group__.common.registrable.items.ItemsThis.ITEMS;
import static $group__.common.registrable.tileentities.TileEntityTypesThis.TILE_ENTITIES;

@Mod(MOD_ID)
public final class ModThis
		extends Singleton {
	private static final Supplier<ModThis> INSTANCE = Suppliers.memoize(() -> Singleton.getSingletonInstance(ModThis.class, ModConfiguration.getInstance().getLogger()));
	private static final ResourceBundle RESOURCE_BUNDLE;

	static {
		ConfigurationTemplate.configureSafe(UtilitiesConfiguration.getInstance(),
				() -> new UtilitiesConfiguration.ConfigurationData(null, MinecraftLocaleUtilities::getCurrentLocale)); // COMMENT configure as early as possible
		ConfigurationTemplate.configureSafe(ModConfiguration.getInstance(),
				() -> new ModConfiguration.ConfigurationData(null, null));
		RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());
	}

	private final EventHandler eventHandler = Singleton.getSingletonInstance(EventHandler.class, ModConfiguration.getInstance().getLogger());
	private final IProxy proxy = DistExecutor.unsafeRunForDist(
			() -> DistLambdaHolder.Client::getProxyClient,
			() -> DistLambdaHolder.Server::getProxyServer);

	public ModThis() {
		// COMMENT entry point
		super(ModConfiguration.getInstance().getLogger());
		IEventBus eventBusMod = AssertionUtilities.assertNonnull(Bus.MOD.bus().get());
		eventBusMod.register(getEventHandler());
		BLOCKS.register(eventBusMod);
		ITEMS.register(eventBusMod);
		TILE_ENTITIES.register(eventBusMod);
		CONTAINERS.register(eventBusMod);
	}

	private EventHandler getEventHandler() { return eventHandler; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static ResourceLocation createResourceLocation(@NonNls String path) { return new ResourceLocation(MOD_ID, path); }

	public static String getNamespacePrefixedString(@NonNls String separator, @NonNls String string) { return NamespaceUtilities.getNamespacePrefixedString(separator, MOD_ID, string); }

	public IProxy getProxy() { return proxy; }

	public static ModThis getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	private enum DistLambdaHolder {
		;

		private enum Client {
			;

			private static IProxy getProxyClient() { return new ProxyClient(ModConfiguration.getInstance().getLogger()); }
		}

		private enum Server {
			;

			private static IProxy getProxyServer() { return new ProxyServer(ModConfiguration.getInstance().getLogger()); }
		}
	}

	private final class EventHandler extends Singleton {
		private EventHandler() { super(ModConfiguration.getInstance().getLogger()); }

		@SubscribeEvent
		protected void onModLifecycleEvent(ModLifecycleEvent event) {
			if (!getProxy().onModLifecycle(event))
				ModConfiguration.getInstance().getLogger().atInfo()
						.addKeyValue("event", event)
						.log(getResourceBundle().getString("event.lifecycle.unhandled"));
		}
	}
}
