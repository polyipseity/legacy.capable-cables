package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def;

import org.reactivestreams.Publisher;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface INotifier<T> {
	Publisher<T> getNotifier();
}
