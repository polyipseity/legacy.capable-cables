package $group__.client.ui.mvvm.core.extensions.cursors;

import java.awt.geom.Point2D;
import java.util.Optional;

@FunctionalInterface
public interface ICursorHandleProvider {
	Optional<Long> getCursorHandle(Point2D cursorPosition);
}
