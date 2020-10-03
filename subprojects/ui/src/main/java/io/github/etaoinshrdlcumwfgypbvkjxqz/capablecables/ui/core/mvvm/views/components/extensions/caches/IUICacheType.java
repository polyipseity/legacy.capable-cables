package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Optional;

public interface IUICacheType<V, C extends IExtensionContainer<INamespacePrefixedString>> {
	void invalidate(C container);

	INamespacePrefixedString getKey();

	Optional<? extends V> get(C container);

	enum StaticHolder {
		;

		public static void invalidateImpl(IExtensionContainer<INamespacePrefixedString> container, INamespacePrefixedString key) {
			IUICacheExtension.TYPE.getValue().find(container)
					.map(IUICacheExtension::getDelegated)
					.ifPresent(cache -> cache.invalidate(key));
		}

		public static void invalidateChildrenImpl(IUIComponentContainer container, IUICacheType<?, ? super IUIComponent> type) {
			container.getChildrenView().stream().unordered()
					.forEach(type::invalidate);
		}
	}
}
