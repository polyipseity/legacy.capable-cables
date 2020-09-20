package $group__.ui.mvvm.views.components.extensions;

import $group__.ui.core.mvvm.extensions.cursors.IUIExtensionCursorHandleProvider;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.utilities.CastUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.structures.INamespacePrefixedString;

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
				.flatMap(view -> {
					Optional<Long> ret = Optional.empty();
					try (IUIComponentContext context = view.createContext()) {
						view.getPathResolver().resolvePath(context, cursorPosition, true);
						while (!context.getPath().isRoot()) {
							IUIComponent component = context.getPath().getPathEnd();
							if ((ret = CastUtilities.castChecked(IUIComponentCursorHandleProvider.class, component)
									.flatMap(ec -> ec.getCursorHandle(context, cursorPosition).map(Function.identity())))
									.isPresent())
								break;
							context.pop();
						}
					}
					return ret;
				});
	}

	@Override
	public IType<? extends INamespacePrefixedString, ?, ? extends IUIView<?>> getType() { return TYPE.getValue(); }
}
