package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;

public interface IEventExtension<C extends IExtensionContainer<? super Class<?>>>
		extends IExtension<Class<?>, C> {}
