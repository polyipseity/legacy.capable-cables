package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.server;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.IProxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
@OnlyIn(Dist.DEDICATED_SERVER)
public interface IProxyServer extends IProxy<FMLDedicatedServerSetupEvent> {
	@Override
	default void onSetupSided(FMLDedicatedServerSetupEvent event) {}
}
