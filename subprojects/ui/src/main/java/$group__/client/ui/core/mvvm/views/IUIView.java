package $group__.client.ui.core.mvvm.views;

import $group__.client.ui.core.mvvm.IUICommon;
import $group__.client.ui.core.mvvm.binding.IHasBinding;
import $group__.client.ui.core.mvvm.extensions.IUIExtension;
import $group__.client.ui.core.mvvm.views.events.IUIEventTarget;
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
		extends IUICommon, IUIReshapeExplicitly<S>, IHasBinding, IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? extends IUIView<?>>> {
	IUIEventTarget getTargetAtPoint(Point2D point);

	Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);
}
