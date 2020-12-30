package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.def;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ISubscribeEventProvider {
	Optional<? extends SubscribeEvent> getSubscribeEvent();
}
