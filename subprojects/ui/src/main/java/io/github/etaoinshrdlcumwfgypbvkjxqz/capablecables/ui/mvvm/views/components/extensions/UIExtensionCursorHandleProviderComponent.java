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
				.flatMap(view -> IUIViewComponent.StaticHolder.createComponentContextWithManager(view)
						.flatMap(context -> {
							Optional<Long> ret = Optional.empty();
							try (IUIComponentContext ctx = context) {
								view.getPathResolver().resolvePath(ctx, (Point2D) cursorPosition.clone(), true);
								while (!ctx.getPath().isEmpty()) {
									IUIComponent component = ctx.getPath().getPathEnd().orElseThrow(AssertionError::new);
									if ((ret = CastUtilities.castChecked(IUIComponentCursorHandleProvider.class, component)
											.flatMap(ec -> ec.getCursorHandle(ctx).map(Function.identity())))
											.isPresent())
										break;
									ctx.pop();
								}
							}
							return ret;
						}));
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIView<?>> getType() { return TYPE.getValue(); }
}
