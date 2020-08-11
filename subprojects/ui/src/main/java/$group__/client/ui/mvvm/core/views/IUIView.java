package $group__.client.ui.mvvm.core.views;

import $group__.client.ui.mvvm.core.IUICommon;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.utilities.extensions.IExtensionContainer;
import net.minecraft.util.ResourceLocation;

import java.awt.geom.Point2D;

// TODO mark as only UI thread
public interface IUIView<SD extends IShapeDescriptor<?, ?>>
		extends IUICommon, IUIReshapeExplicitly<SD>, IHasBinding, IExtensionContainer<ResourceLocation, IUIExtension<? extends IUIView<?>>> {
	IUIEventTarget getTargetAtPoint(Point2D point);

	IUIEventTarget getFocus();

	boolean changeFocus(boolean next);

	SD getShapeDescriptor();
}
