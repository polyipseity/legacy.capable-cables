package $group__.client.ui.mvvm.core.views.components.extensions.cursors;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;

import java.awt.geom.Point2D;
import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentCursorHandleProvider {
	Optional<Long> getCursorHandle(IAffineTransformStack stack, Point2D cursorPosition);
}
