package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors.IUIExtensionCursorHandleProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.function.Function;

public class UIExtensionCursorHandleProviderComponent<E extends IUIViewComponent<?, ?>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIView<?>, E>
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
	public IExtensionType<INamespacePrefixedString, ?, IUIView<?>> getType() { return TYPE.getValue(); }
}
