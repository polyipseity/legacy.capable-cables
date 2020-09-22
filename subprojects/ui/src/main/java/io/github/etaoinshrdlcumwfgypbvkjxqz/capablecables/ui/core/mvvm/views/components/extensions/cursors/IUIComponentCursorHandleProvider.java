package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;

import java.awt.geom.Point2D;
import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentCursorHandleProvider {
	Optional<? extends Long> getCursorHandle(IUIComponentContext context, Point2D cursorPosition);
}
