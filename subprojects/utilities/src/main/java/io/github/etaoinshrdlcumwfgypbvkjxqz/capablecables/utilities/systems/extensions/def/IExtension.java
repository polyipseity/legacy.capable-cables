package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;

public interface IExtension<K, C extends IExtensionContainer<? super K>>
		extends ITypeCapture {
	default void onExtensionAdded(C container) {}

	default void onExtensionRemoved() {}

	IExtensionType<K, ?, C> getType();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<? extends C> getTypeToken();
}
