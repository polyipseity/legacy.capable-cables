package $group__.ui.core.mvvm.views;

import $group__.ui.core.mvvm.IUISubInfrastructure;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

/**
 * Operations not thread-safe.
 */
public interface IUIView<S extends Shape>
		extends IUISubInfrastructure, IUIReshapeExplicitly<S>, IHasBinding, IExtensionContainer<INamespacePrefixedString> {
	IUIEventTarget getTargetAtPoint(Point2D point);

	Optional<? extends IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);
}
