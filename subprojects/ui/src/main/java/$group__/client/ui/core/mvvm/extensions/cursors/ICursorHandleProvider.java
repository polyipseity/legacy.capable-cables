package $group__.client.ui.core.mvvm.extensions.cursors;

import java.awt.geom.Point2D;
import java.util.Optional;

@FunctionalInterface
public interface ICursorHandleProvider {
	Optional<Long> getCursorHandle(Point2D cursorPosition);
}
