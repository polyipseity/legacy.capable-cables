package $group__.client.ui.mvvm.views.components.extensions;

import $group__.client.ui.mvvm.core.extensions.cursors.IUIExtensionCursorHandleProvider;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.IUIView;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIViewComponent;
import $group__.client.ui.mvvm.core.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.client.ui.mvvm.core.views.components.parsers.UIConstructor;
import $group__.utilities.CastUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import net.minecraft.util.ResourceLocation;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UIExtensionCursorHandleProviderComponent<E extends IUIViewComponent<?, ?>>
		extends ExtensionContainerAware<ResourceLocation, IUIView<?>, E>
		implements IUIExtensionCursorHandleProvider {
	@UIConstructor
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
	public IType<? extends ResourceLocation, ?, ? extends IUIView<?>> getType() { return TYPE.getValue(); }
}
