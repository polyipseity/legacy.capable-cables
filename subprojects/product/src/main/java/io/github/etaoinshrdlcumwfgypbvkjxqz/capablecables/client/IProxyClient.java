package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.IProxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
@OnlyIn(Dist.CLIENT)
public interface IProxyClient extends IProxy<FMLClientSetupEvent> {
	@Override
	default void onSetupSided(FMLClientSetupEvent event) {}
}
