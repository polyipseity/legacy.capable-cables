package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors;

import java.awt.geom.Point2D;
import java.util.Optional;

@FunctionalInterface
public interface ICursorHandleProvider {
	Optional<? extends Long> getCursorHandle(Point2D cursorPosition);
}
