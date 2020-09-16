package $group__.ui.mvvm.views.components.extensions;

import $group__.ui.core.mvvm.extensions.cursors.IUIExtensionCursorHandleProvider;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPath;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.core.structures.IAffineTransformStack;
import $group__.utilities.CastUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.Lists;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.function.Function;

public class UIExtensionCursorHandleProviderComponent<E extends IUIViewComponent<?, ?>>
		extends ExtensionContainerAware<INamespacePrefixedString, IUIView<?>, E>
		implements IUIExtensionCursorHandleProvider {
	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public UIExtensionCursorHandleProviderComponent(Class<E> containerClass) {
		super(CastUtilities.castUnchecked(IUIView.class), // COMMENT generics should not matter in this case
				containerClass);
	}

	@Override
	public Optional<? extends Long> getCursorHandle(Point2D cursorPosition) {
		return getContainer()
				.flatMap(v -> {
					Optional<Long> ret = Optional.empty();
					IUIComponentPath<IUIComponent> cp = v.getPathResolver().resolvePath(cursorPosition, true);
					try (IAffineTransformStack stack = cp.getTransformStackView()) {
						for (IUIComponent e : Lists.reverse(cp.asList())) {
							if ((ret = CastUtilities.castChecked(IUIComponentCursorHandleProvider.class, e)
									.flatMap(ec -> ec.getCursorHandle(stack, cursorPosition).map(Function.identity())))
									.isPresent())
								break;
							if (!stack.isClean())
								stack.pop();
						}
					}
					return ret;
				});
	}

	@Override
	public IType<? extends INamespacePrefixedString, ?, ? extends IUIView<?>> getType() { return TYPE.getValue(); }
}
