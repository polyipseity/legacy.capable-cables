package $group__.client;

import $group__.proxies.IProxy;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public interface IProxyClient extends IProxy {
	default void onSetupClient(FMLClientSetupEvent event) {}

	@Override
	default <T extends ModLifecycleEvent> void onSetupSided(T event) { onSetupClient((FMLClientSetupEvent) event); }
}
