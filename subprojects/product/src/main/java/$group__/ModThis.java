package $group__;

import $group__.client.ProxyClient;
import $group__.proxies.IProxy;
import $group__.server.ProxyServer;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.UtilitiesConfiguration;
import $group__.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.ConfigurationTemplate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import static $group__.ModConfiguration.LOGGER;
import static $group__.ModConstants.MOD_ID;
import static $group__.common.registrables.blocks.BlocksThis.BLOCKS;
import static $group__.common.registrables.inventory.ContainersThis.CONTAINERS;
import static $group__.common.registrables.items.ItemsThis.ITEMS;
import static $group__.common.registrables.tileentities.TileEntityTypesThis.TILE_ENTITIES;
import static $group__.utilities.LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.utilities.LoggerUtilities.EnumMessages.PREFIX_MOD_LIFECYCLE_MESSAGE;

@Mod(MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public final class ModThis extends Singleton {
	static {
		ConfigurationTemplate.configureSafe(UtilitiesConfiguration.getInstance(),
				() -> new UtilitiesConfiguration.ConfigurationData(null, MinecraftLocaleUtilities::getCurrentLocale)); // COMMENT configure as early as possible
		ConfigurationTemplate.configureSafe(ModConfiguration.getInstance(),
				() -> new ModConfiguration.ConfigurationData(null, MinecraftLocaleUtilities::getCurrentLocale));
	}

	public final IProxy proxy = DistExecutor.unsafeRunForDist(
			() -> DistLambdaHolder.Client::getProxyClient,
			() -> DistLambdaHolder.Server::getProxyServer);

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public ModThis() {
		super(LOGGER);
		IEventBus eventBusMod = Bus.MOD.bus().get();
		eventBusMod.register(this);
		BLOCKS.register(eventBusMod);
		ITEMS.register(eventBusMod);
		TILE_ENTITIES.register(eventBusMod);
		CONTAINERS.register(eventBusMod);
	}

	public static ResourceLocation getResourceLocation(String path) { return new ResourceLocation(MOD_ID, path); }

	public static String getNamespacePrefixedString(String separator, String string) { return NamespaceUtilities.getNamespacePrefixedString(separator, MOD_ID, string); }

	@SubscribeEvent
	protected void onModLifecycleEvent(ModLifecycleEvent event) {
		if (!proxy.onModLifecycle(event))
			LOGGER.info(() -> PREFIX_MOD_LIFECYCLE_MESSAGE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Event '{}' received, but processing of the event is NOT implemented", event)).getFormattedMessage());
	}

	private enum DistLambdaHolder {
		;

		private enum Client {
			;

			private static IProxy getProxyClient() { return new ProxyClient(LOGGER); }
		}

		private enum Server {
			;

			private static IProxy getProxyServer() { return new ProxyServer(LOGGER); }
		}
	}
}
