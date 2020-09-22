package $group__.ui.core.mvvm.views.components.extensions.caches;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.structures.INamespacePrefixedString;

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
