package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;

import java.util.Optional;

@FunctionalInterface
public interface ICursorHandleProvider {
	Optional<Long> getCursorHandle(IUIViewContext context);
}
