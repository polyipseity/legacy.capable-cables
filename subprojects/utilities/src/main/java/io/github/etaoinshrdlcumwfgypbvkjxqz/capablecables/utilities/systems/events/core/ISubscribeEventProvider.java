package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.core;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ISubscribeEventProvider {
	Optional<? extends SubscribeEvent> getSubscribeEvent();
}
