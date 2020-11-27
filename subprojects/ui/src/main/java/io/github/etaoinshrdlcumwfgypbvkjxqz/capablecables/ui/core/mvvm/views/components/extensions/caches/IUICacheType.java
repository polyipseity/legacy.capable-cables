package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.StackTraceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import org.jetbrains.annotations.NonNls;

import java.util.Optional;

public interface IUICacheType<V, C extends IExtensionContainer<INamespacePrefixedString>> {
	static void invalidateImpl(IExtensionContainer<INamespacePrefixedString> container, INamespacePrefixedString key) {
		IUICacheExtension.StaticHolder.getType().getValue().find(container)
				.map(IUICacheExtension::getDelegate)
				.ifPresent(cache -> cache.invalidate(key));
	}

	static void invalidateChildrenImpl(IUIComponent container, IUICacheType<?, ? super IUIComponent> type) {
		container.getChildrenView().stream().unordered()
				.forEach(type::invalidate);
	}

	void invalidate(C container);

	static INamespacePrefixedString generateKey(@NonNls CharSequence name) {
		return ImmutableNamespacePrefixedString.of(StaticHolder.getDefaultNamespace(), StackTraceUtilities.getCallerClass().getName() + '.' + name);
	}

	INamespacePrefixedString getKey();

	Optional<? extends V> get(C container);

	enum StaticHolder {
		;

		public static final @NonNls String DEFAULT_NAMESPACE = "default";
		public static final @NonNls String DEFAULT_PREFIX = DEFAULT_NAMESPACE + INamespacePrefixedString.StaticHolder.SEPARATOR;

		public static @NonNls String getDefaultNamespace() { return DEFAULT_NAMESPACE; }

		public static @NonNls String getDefaultPrefix() { return DEFAULT_PREFIX; }
	}
}
