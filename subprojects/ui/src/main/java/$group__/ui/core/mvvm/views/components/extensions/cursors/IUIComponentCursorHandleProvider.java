package $group__.ui.core.mvvm.views.components.extensions.cursors;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;

import java.awt.geom.Point2D;
import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentCursorHandleProvider {
	Optional<? extends Long> getCursorHandle(IAffineTransformStack stack, Point2D cursorPosition);
}
