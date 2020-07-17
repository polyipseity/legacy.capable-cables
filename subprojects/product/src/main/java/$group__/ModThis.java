package $group__;

import $group__.client.ProxyClient;
import $group__.proxies.IProxy;
import $group__.server.ProxyServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import static $group__.Constants.MOD_ID;
import static $group__.Globals.LOGGER;
import static $group__.common.registrables.blocks.BlocksThis.BLOCKS;
import static $group__.common.registrables.items.ItemsThis.ITEMS;
import static $group__.utilities.helpers.specific.Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.utilities.helpers.specific.Loggers.EnumMessages.PREFIX_MOD_LIFECYCLE_MESSAGE;

@Mod(MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public final class ModThis {
	public final IProxy proxy = DistExecutor.runForDist(() -> () -> new ProxyClient(LOGGER), () -> () -> new ProxyServer(LOGGER));

	private ModThis() {
		Bus.MOD.bus().get().register(this);
		BLOCKS.register(Bus.MOD.bus().get());
		ITEMS.register(Bus.MOD.bus().get());
	}

	public static ResourceLocation getResourceLocation(String path) { return new ResourceLocation(MOD_ID, path); }

	@SubscribeEvent
	private void onModLifecycleEvent(ModLifecycleEvent event) {
		if (!proxy.onModLifecycle(event))
			LOGGER.info(() -> PREFIX_MOD_LIFECYCLE_MESSAGE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Event '{}' received, but processing of the event is NOT implemented", event)).getFormattedMessage());
	}
}
