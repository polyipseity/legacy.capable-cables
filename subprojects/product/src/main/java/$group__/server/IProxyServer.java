package $group__.server;

import $group__.proxies.IProxy;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import static net.minecraftforge.api.distmarker.Dist.DEDICATED_SERVER;

@OnlyIn(DEDICATED_SERVER)
public interface IProxyServer extends IProxy {
	default void onSetupServer(FMLDedicatedServerSetupEvent event) {}

	@Override
	default <T extends ModLifecycleEvent> void onSetupSided(T event) { onSetupServer((FMLDedicatedServerSetupEvent) event); }
}
