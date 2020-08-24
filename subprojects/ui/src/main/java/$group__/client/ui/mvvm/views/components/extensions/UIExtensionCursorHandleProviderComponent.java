package $group__.client.ui.mvvm.views.components.extensions;

import $group__.client.ui.core.mvvm.extensions.cursors.IUIExtensionCursorHandleProvider;
import $group__.client.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.client.ui.core.mvvm.views.IUIView;
import $group__.client.ui.core.mvvm.views.components.IUIComponent;
import $group__.client.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.client.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.client.ui.core.mvvm.views.components.parsers.UIExtensionConstructor;
import $group__.utilities.CastUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.interfaces.INamespacePrefixedString;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UIExtensionCursorHandleProviderComponent<E extends IUIViewComponent<?, ?>>
		extends ExtensionContainerAware<INamespacePrefixedString, IUIView<?>, E>
		implements IUIExtensionCursorHandleProvider {
	@UIExtensionConstructor(type = UIExtensionConstructor.ConstructorType.EXTENDED_CLASS)
	public UIExtensionCursorHandleProviderComponent(Class<E> extendedClass) {
		super(CastUtilities.castUnchecked(IUIView.class), // COMMENT generics should not matter in this case
				extendedClass);
	}

	@Override
	public Optional<Long> getCursorHandle(Point2D cursorPosition) {
		return getContainer().map(c -> {
			Optional<Long> ret = Optional.empty();
			IAffineTransformStack stack = c.getManager().getCleanTransformStack();
			AtomicInteger popTimes = new AtomicInteger();
			for (IUIComponent e : c.getManager().getPathResolver().resolvePath(cursorPosition, true).asList()) {
				if (e instanceof IUIComponentCursorHandleProvider) {
					if ((ret = ((IUIComponentCursorHandleProvider) e).getCursorHandle(stack, cursorPosition))
							.isPresent())
						break;
				}
				if (popTimes.getAndDecrement() > 0)
					stack.getDelegated().pop();
			}
			IAffineTransformStack.popMultiple(stack, popTimes.get());
			return ret;
		}).orElseGet(Optional::empty);
	}

	@Override
	public IType<? extends INamespacePrefixedString, ?, ? extends IUIView<?>> getType() { return TYPE.getValue(); }
}
