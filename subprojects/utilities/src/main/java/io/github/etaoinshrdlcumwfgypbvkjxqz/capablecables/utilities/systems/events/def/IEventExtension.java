package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;

public interface IEventExtension<C extends IExtensionContainer<? super Class<?>>>
		extends IExtension<Class<?>, C> {}
