package $group__.ui.mvvm.views.components.extensions;

import $group__.ui.core.mvvm.extensions.cursors.IUIExtensionCursorHandleProvider;
import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPath;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.utilities.CastUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.Lists;

import java.awt.geom.Point2D;
import java.util.Optional;

public class UIExtensionCursorHandleProviderComponent<E extends IUIViewComponent<?, ?>>
		extends ExtensionContainerAware<INamespacePrefixedString, IUIView<?>, E>
		implements IUIExtensionCursorHandleProvider {
	@UIExtensionConstructor(type = UIExtensionConstructor.ConstructorType.CONTAINER_CLASS)
	public UIExtensionCursorHandleProviderComponent(Class<E> containerClass) {
		super(CastUtilities.castUnchecked(IUIView.class), // COMMENT generics should not matter in this case
				containerClass);
	}

	@Override
	public Optional<Long> getCursorHandle(Point2D cursorPosition) {
		return getContainer().map(c -> {
			Optional<Long> ret = Optional.empty();
			IUIComponentPath cp = c.getManager().getPathResolver().resolvePath(cursorPosition, true);
			try (IAffineTransformStack stack = cp.getTransformStackView()) {
				for (IUIComponent e : Lists.reverse(cp.asList())) {
					if ((ret = CastUtilities.castChecked(IUIComponentCursorHandleProvider.class, e)
							.flatMap(ec -> ec.getCursorHandle(stack, cursorPosition)))
							.isPresent())
						break;
					if (!stack.isClean())
						stack.getDelegated().pop();
				}
			}
			return ret;
		}).orElseGet(Optional::empty);
	}

	@Override
	public IType<? extends INamespacePrefixedString, ?, ? extends IUIView<?>> getType() { return TYPE.getValue(); }
}
