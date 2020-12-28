package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;

public interface IUIEventTarget {
	boolean addEventListener(IIdentifier type, IUIEventListener<?> listener, boolean useCapture);

	boolean removeEventListener(IIdentifier type, IUIEventListener<?> listener, boolean useCapture);

	boolean dispatchEvent(IUIEvent event);

	boolean isActive();

	boolean isFocusable();

	@FunctionalInterface
	interface Functional
			extends IUIEventTarget {
		@Override
		default boolean addEventListener(IIdentifier type, IUIEventListener<?> listener, boolean useCapture) { return false; }

		@Override
		default boolean removeEventListener(IIdentifier type, IUIEventListener<?> listener, boolean useCapture) { return false; }

		@Override
		default boolean isActive() { return true; }

		@Override
		default boolean isFocusable() { return false; }
	}
}
