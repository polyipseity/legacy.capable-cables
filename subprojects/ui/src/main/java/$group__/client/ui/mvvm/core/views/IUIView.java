package $group__.client.ui.mvvm.core.views;

import $group__.client.ui.core.IShapeDescriptor;
import $group__.client.ui.mvvm.core.IUICommon;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.utilities.extensions.IExtensionContainer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Optional;

/**
 * Operations not thread-safe.
 */
public interface IUIView<SD extends IShapeDescriptor<?>>
		extends IUICommon, IUIReshapeExplicitly<SD>, IHasBinding, IExtensionContainer<ResourceLocation, IUIExtension<ResourceLocation, ? extends IUIView<?>>> {
	IUIEventTarget getTargetAtPoint(Point2D point);

	Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);

	SD getShapeDescriptor();
}
