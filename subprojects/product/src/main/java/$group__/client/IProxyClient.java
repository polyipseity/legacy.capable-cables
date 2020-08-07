package $group__.client;

import $group__.proxies.IProxy;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

@OnlyIn(Dist.CLIENT)
public interface IProxyClient extends IProxy {
	@Override
	default <T extends ModLifecycleEvent> void onSetupSided(T event) { onSetupClient((FMLClientSetupEvent) event); }

	default void onSetupClient(FMLClientSetupEvent event) {}
}
