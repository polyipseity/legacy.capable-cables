package $group__.ui.core.mvvm.views.events;

import $group__.utilities.interfaces.INamespacePrefixedString;

public interface IUIEventTarget {
	boolean addEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture);

	boolean removeEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture);

	boolean dispatchEvent(IUIEvent event);

	boolean isActive();

	boolean isFocusable();

	@FunctionalInterface
	interface Functional
			extends IUIEventTarget {
		@Override
		default boolean addEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture) { return false; }

		@Override
		default boolean removeEventListener(INamespacePrefixedString type, IUIEventListener<?> listener, boolean useCapture) { return false; }

		@Override
		default boolean isActive() { return true; }

		@Override
		default boolean isFocusable() { return false; }
	}
}
