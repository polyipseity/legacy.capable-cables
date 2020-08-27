package $group__.server;

import $group__.proxies.IProxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
@OnlyIn(Dist.DEDICATED_SERVER)
public interface IProxyServer extends IProxy {
	@Override
	default <T extends ModLifecycleEvent> void onSetupSided(T event) { onSetupServer((FMLDedicatedServerSetupEvent) event); }

	default void onSetupServer(FMLDedicatedServerSetupEvent event) {}
}
